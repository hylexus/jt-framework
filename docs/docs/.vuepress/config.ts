import {defineUserConfig} from '@vuepress/cli'
import type {DefaultThemeOptions} from '@vuepress/theme-default'
import {path} from '@vuepress/utils'
import {navbar, sidebar} from './configs'

const isProd = process.env.NODE_ENV === 'production'

export default defineUserConfig<DefaultThemeOptions>({
    base: '/jt-framework/',
    head: [
        [
            'link',
            {
                rel: 'icon',
                type: 'image/png',
                sizes: '16x16',
                href: `/img/icons/favicon-16x16.png`,
            },
        ],
        [
            'link',
            {
                rel: 'icon',
                type: 'image/png',
                sizes: '32x32',
                href: `/img/icons/favicon-32x32.png`,
            },
        ],
        // ['link', {rel: 'manifest', href: '/manifest.webmanifest'}],
        ['meta', {name: 'application-name', content: 'VuePress'}],
        ['meta', {name: 'apple-mobile-web-app-title', content: 'VuePress'}],
        ['meta', {name: 'apple-mobile-web-app-status-bar-style', content: 'black'},],
        ['link', {rel: 'apple-touch-icon', href: `/img/icons/apple-touch-icon.png`},],
        ['link', {rel: 'mask-icon', href: '/img/icons/safari-pinned-tab.svg', color: '#3eaf7c',},],
        ['meta', {name: 'msapplication-TileColor', content: '#3eaf7c'}],
        ['meta', {name: 'theme-color', content: '#3eaf7c'}],
    ],

    // site-level locales config
    locales: {
        '/': {
            lang: 'zh-CN',
            title: 'jt-framework',
            description: 'Vue 驱动的静态网站生成器',
        },
        // '/en/': {
        //     lang: 'en-US',
        //     title: 'VuePress',
        //     description: 'Vue-powered Static Site Generator',
        // },
    },

    themeConfig: {
        logo: '/img/hero.png',
        repo: 'hylexus/jt-framework',
        docsDir: 'docs',

        // theme-level locales config
        locales: {
            '/': {
                // navbar
                navbar: navbar.zh,
                selectLanguageName: '简体中文',
                selectLanguageText: '选择语言',
                selectLanguageAriaLabel: '选择语言',

                // sidebar
                sidebar: sidebar.zh,

                // page meta
                editLinkText: '在 GitHub 上编辑此页',
                lastUpdatedText: '上次更新',
                contributorsText: '贡献者',

                // custom containers
                tip: '提示',
                warning: '注意',
                danger: '警告',

                // 404 page
                notFound: [
                    '这里什么都没有',
                    '我们怎么到这来了？',
                    '这是一个 404 页面',
                    '看起来我们进入了错误的链接',
                ],
                backToHome: '返回首页',

                // a11y
                openInNewWindow: '在新窗口打开',
                toggleDarkMode: '切换夜间模式',
                toggleSidebar: '切换侧边栏',
            },
            // '/en/': {
            //     // navbar
            //     navbar: navbar.en,
            //     // sidebar
            //     sidebar: sidebar.en,
            //     // page meta
            //     editLinkText: 'Edit this page on GitHub',
            // },
        },

        themePlugins: {
            // only enable git plugin in production mode
            git: isProd,
        },
    },

    markdown: {
        importCode: {
            handleImportPath: (str) =>
                str.replace(
                    /^@example-src/,
                    path.resolve(__dirname, '../../example-src/')
                ),
        },
    },

    plugins: [],
})
