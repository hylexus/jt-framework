import {path} from "vuepress/utils"
import {defineUserConfig} from 'vuepress'
import theme from "./theme"
import {viteBundler} from "@vuepress/bundler-vite"

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
            css: {
                postcss: {
                    plugins: [{
                        postcssPlugin: 'jt-docs-system-fonts',
                        OnceExit(styles) {
                            styles.walkAtRules(/^font-face$/i, (rule) => {
                                rule.walkDecls(/^font-family$/i, (declaration) => {
                                    // The theme's Crimson subset has unconfirmed font terms; use system fonts.
                                    if (declaration.value.replace(/["']/g, '').trim().toLowerCase() === 'crimson') {
                                        rule.remove()
                                    }
                                })
                            })
                        },
                    }],
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
