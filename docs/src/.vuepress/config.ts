import {path} from '@vuepress/utils'
import {defineUserConfig} from 'vuepress'
import theme from "./theme"
import {searchProPlugin} from "vuepress-plugin-search-pro"

export default defineUserConfig({
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
        ['meta', {name: 'application-name', content: 'jt808'}],
        ['meta', {name: 'apple-mobile-web-app-title', content: 'jt808'}],
        ['meta', {name: 'apple-mobile-web-app-status-bar-style', content: 'black'},],
        ['link', {rel: 'apple-touch-icon', href: `/img/icons/apple-touch-icon.png`},],
        ['link', {rel: 'mask-icon', href: '/img/icons/safari-pinned-tab.svg', color: '#3eaf7c',},],
        ['meta', {name: 'msapplication-TileColor', content: '#3eaf7c'}],
        ['meta', {name: 'theme-color', content: '#3eaf7c'}],
    ],

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

    theme,

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

    plugins: [
        searchProPlugin({
                // 索引全部内容
                indexContent: true,
                hotKeys: [{key: 'k', ctrl: true}],
                // 为分类和标签添加索引
                customFields: [
                    {
                        getter: (page) => page.frontmatter.category,
                        formatter: "分类：$content",
                    },
                    {
                        getter: (page) => page.frontmatter.tag,
                        formatter: "标签：$content",
                    },
                ],
            }
        ),
    ]
})
