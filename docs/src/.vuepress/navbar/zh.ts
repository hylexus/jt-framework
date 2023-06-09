import {navbar} from "vuepress-theme-hope";

export const zhNavbar = navbar([
        {
            text: '指南',
            icon: 'creative',
            link: '/v2/jt-808/guide/',
            // children: [
            //     {text: "v2.x", link: '/v2/jt-808/guide/quick-start/quick-start'},
            //     {text: "v1.x", link: '/v1/jt-808/guide/'},
            // ]
        },
        {
            text: '配置',
            icon: 'setting',
            link: '/v2/jt-808/config/',
            // children: [
            //     {text: "v2.x", link: '/v2/jt-808/config/'},
            //     {text: "v1.x", link: '/v1/jt-808/config/'},
            // ]
        },
        {
            text: "协议扫盲", link: "/protocol-introduction/",
            icon: 'info',
        },
        {
            text: "FAQ", link: "/frequently-asked-questions/",
            icon: 'question',
        },
        {
            text: '升级指南',
            link: '/v2/jt-808/guide/migration/migrating-from-2.0.x-to-2.1.x.md',
            icon: 'merge'
        },
        {
            text: '发版记录',
            link: '/v2/release-notes/latest.md',
            icon: 'branch'
        },
        {
            text: '历史版本',
            icon: 'list',
            children: [
                {
                    text: 'v-1.x',
                    children: [
                        {text: '指南', link: '/v1/jt-808/guide/', icon: 'creative',},
                        {text: '配置', link: '/v1/jt-808/config/', icon: 'config',},
                    ]
                }
            ]
        },
        {
            text: '致谢',
            icon: 'share',
            children: [
                {text: 'JTTools', link: 'https://jttools.smallchi.cn/jt808', icon: 'tool'},
                {text: 'Netty', link: 'https://github.com/netty/netty', icon: 'leaf'},
                {
                    text: 'Spring', children: [
                        {text: 'Spring Boot', icon: 'leaf', link: 'https://docs.spring.io/spring-boot/docs/2.5.8/reference/html/'},
                        {
                            text: 'Spring WebFlux',
                            icon: 'leaf',
                            link: 'https://docs.spring.io/spring-framework/docs/5.2.19.RELEASE/spring-framework-reference/web-reactive.html#spring-webflux'
                        },
                        {
                            text: 'Spring WebMVC',
                            icon: 'leaf',
                            link: 'https://docs.spring.io/spring-framework/docs/5.2.19.RELEASE/spring-framework-reference/web.html#spring-web'
                        },
                    ],
                },
                {
                    text: 'Vue', children: [
                        {text: 'VuePress', icon: 'vue', link: 'https://www.vuepress.cn/'},
                        {text: 'vuepress-theme-hope', icon: 'vue', link: 'https://theme-hope.vuejs.press/zh/'},
                    ],
                },
                {
                    text: 'User', children: [
                        {
                            text: '小蒲的大门牙',
                            icon: 'profile',
                            link: 'https://www.douyin.com/search/%E5%B0%8F%E8%92%B2%E7%9A%84%E5%A4%A7%E9%97%A8%E7%89%99?source=search_sug&aid=abd4e6d3-b10f-4374-a311-aaf7d5229e59&enter_from=recommend&gid=7218499611265191180&aweme_id=7218499611265191180'
                        },
                        {text: 'qianhongtang', icon: 'profile', link: 'https://github.com/qianhongtang'},
                        {text: 'pruidong', icon: 'profile', link: 'https://github.com/pruidong'},
                    ],
                },

            ]
        },
        {
            text: '关于我们', link: '/about.md', icon: 'home'
        }
    ]
)