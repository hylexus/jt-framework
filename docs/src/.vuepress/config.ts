import {path} from "vuepress/utils"
import {defineUserConfig} from 'vuepress'
import theme from "./theme"
import {viteBundler} from "@vuepress/bundler-vite"
import {docsLicenseMaterials, docsSystemFonts} from "../../../build-script/docs-license-materials.mjs"

const base = '/jt-framework/'

export default defineUserConfig({
    base,
    // base: '/',
    head: [
        [
            'link',
            {
                rel: 'icon',
                type: 'image/svg+xml',
                sizes: 'any',
                href: `${base}favicon.svg`,
            },
        ],
        [
            'link',
            {
                rel: 'icon',
                type: 'image/png',
                sizes: '16x16',
                href: `${base}img/icons/favicon-16x16.png`,
            },
        ],
        [
            'link',
            {
                rel: 'icon',
                type: 'image/png',
                sizes: '32x32',
                href: `${base}img/icons/favicon-32x32.png`,
            },
        ],
        ['link', {rel: 'shortcut icon', href: `${base}favicon.ico`}],
        ['meta', {name: 'application-name', content: 'jt-framework'}],
        ['meta', {name: 'theme-color', content: '#111E37'}],
    ],

    locales: {
        '/': {
            lang: 'zh-CN',
            title: 'jt-framework',
            description: 'JT/T 808',
        },
        // '/en/': {
        //     lang: 'en-US',
        //     title: 'VuePress',
        //     description: 'Vue-powered Static Site Generator',
        // },
    },

    theme,

    bundler: viteBundler({
        viteOptions: {
            plugins: [docsLicenseMaterials()],
            css: {
                postcss: {
                    plugins: [docsSystemFonts()],
                },
            },
        },
    }),

    pagePatterns: [
        "**/*.md",
        "!.vuepress",
        "!node_modules",
    ],

    markdown: {
        importCode: {
            handleImportPath: (str) =>
                str.replace(
                    /^@example-src/,
                    path.resolve(__dirname, '../../example-src/')
                ),
        },
    },

    plugins: []
})
