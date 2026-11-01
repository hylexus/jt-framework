/*
 * Portions correspond to element-plus-vite-starter.
 * Comparison snapshot: https://github.com/element-plus/element-plus-vite-starter/blob/fa10f32788a09b2faab73d3f9709f33bc7cf4d3f/
 * Local modifications by hylexus; source attribution restored 2026-10-02.
 * Upstream package metadata declares MIT; complete original notice remains
 * unresolved. See licenses/element-plus-vite-starter-NOTICE.txt in the repository.
 */

import { fileURLToPath, URL } from 'node:url'
import { readFileSync, writeFileSync } from 'node:fs'
import { resolve } from 'node:path'
import { createHash } from 'node:crypto'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'
import type { Plugin } from 'vite'

import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

import Unocss from 'unocss/vite'
import {
  presetAttributify,
  presetIcons,
  presetUno,
  transformerDirectives,
  transformerVariantGroup,
} from 'unocss'

// https://vitejs.dev/config/
function licenseMaterials(): Plugin {
  const sha256 = (data: string | Uint8Array) => createHash('sha256').update(data).digest('hex')
  return {
    name: 'jt-license-materials',
    apply: 'build',
    enforce: 'post',
    generateBundle() {
      const notices = readFileSync(fileURLToPath(new URL('../../THIRD-PARTY-FRONTEND-NOTICES.txt', import.meta.url)))
      const inventory = JSON.parse(readFileSync(fileURLToPath(new URL('../../licenses/frontend-inventory.json', import.meta.url)), 'utf8'))
      const lock = readFileSync(fileURLToPath(new URL('./pnpm-lock.yaml', import.meta.url)))
      if (sha256(lock) !== inventory.lockfileSha256) {
        this.error('Frontend lockfile changed; review and regenerate the third-party inventory and notices')
      }
      this.emitFile({ type: 'asset', fileName: 'THIRD-PARTY-NOTICES.txt', source: notices })
      this.emitFile({ type: 'asset', fileName: 'LICENSE.txt', source: readFileSync(fileURLToPath(new URL('./LICENSE', import.meta.url))) })
    },
    writeBundle(output, bundle) {
      if (!output.dir) this.error('Expected a directory for the frontend build')
      const inventoryData = readFileSync(fileURLToPath(new URL('../../licenses/frontend-inventory.json', import.meta.url)))
      const inventory = JSON.parse(inventoryData.toString('utf8'))
      // Hash files after Vite's final transformations and minification have been written.
      const assets = Object.values(bundle).map((entry) => ({
        file: entry.fileName,
        sha256: sha256(readFileSync(resolve(output.dir!, entry.fileName))),
      })).sort((left, right) => left.file.localeCompare(right.file))
      for (const file of ['favicon.svg', 'NOTICE.txt']) {
        assets.push({ file, sha256: sha256(readFileSync(resolve(output.dir, file))) })
      }
      assets.sort((left, right) => left.file.localeCompare(right.file))
      writeFileSync(resolve(output.dir, 'license-build-provenance.json'), JSON.stringify({
          lockfile: inventory.lockfile,
          lockfileSha256: sha256(readFileSync(fileURLToPath(new URL('./pnpm-lock.yaml', import.meta.url)))),
          inventorySha256: sha256(inventoryData),
          thirdPartyNoticesSha256: sha256(readFileSync(resolve(output.dir, 'THIRD-PARTY-NOTICES.txt'))),
          assets,
        }, null, 2) + '\n')
    },
  }
}

export default defineConfig({
  base: '/client/',
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `@use "@/styles/element/index.scss" as *;`,
      },
    },
  },
  plugins: [
    licenseMaterials(),
    vue(),
    vueJsx(),
    Components({
      // allow auto load markdown components under `./src/components/`
      extensions: ['vue', 'md'],
      // allow auto import and register components used in markdown
      include: [/\.vue$/, /\.vue\?vue/, /\.md$/],
      resolvers: [
        ElementPlusResolver({
          importStyle: 'sass',
        }),
      ],
      dts: 'src/components.d.ts',
    }),

    // https://github.com/antfu/unocss
    // see unocss.config.ts for config
    Unocss({
      presets: [
        presetUno(),
        presetAttributify(),
        presetIcons({
          scale: 1.2,
          warn: true,
        }),
      ],
      transformers: [
        transformerDirectives(),
        transformerVariantGroup(),
      ]
    }),
  ],
  server: {
    port: 3000,
    host: '127.0.0.1',
    open: false,
    https: false,
    proxy: {
      '/backend/': {
        target: 'http://node2:9090',
        ws: true,
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/backend/, '')
      }
    }
  }
})
