import type {NavbarConfig} from '@vuepress/theme-default'

export const zh: NavbarConfig = [
    {
        text: '指南',
        children: [
            {text: "v2.x", link: '/v2/jt-808/guide/'},
            {text: "v1.x", link: '/v1/jt-808/guide/'},
        ]
    },
    {
        text: '配置',
        children: [
            {text: "v2.x", link: '/v2/jt-808/config/'},
            {text: "v1.x", link: '/v1/jt-808/config/'},
        ]
    },
    {
        text: "协议扫盲", link: "/protocol-introduction/"
    },
    {
        text: "FAQ", link: "/frequently-asked-questions/"
    },
]
