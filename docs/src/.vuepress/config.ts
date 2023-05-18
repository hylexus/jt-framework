import {path} from '@vuepress/utils'
import {defineUserConfig} from 'vuepress'
import theme from "./theme"
import {searchProPlugin} from "vuepress-plugin-search-pro"

export default defineUserConfig({
    base: '/jt-framework/',
    // base: '/',
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
        ['meta', {name: 'application-name', content: 'jt808'}],
        ['meta', {name: 'theme-color', content: '#3eaf7c'}],
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
                // customFields: [
                //     {
                //         getter: (page) => page.frontmatter.category,
                //         formatter: "分类：$content",
                //     },
                //     {
                //         getter: (page) => page.frontmatter.tag,
                //         formatter: "标签：$content",
                //     },
                // ],
            }
        ),
    ]
})
