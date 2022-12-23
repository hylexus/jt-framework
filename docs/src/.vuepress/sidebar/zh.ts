import {sidebar} from "vuepress-theme-hope";

export const zhSidebar = sidebar({
        '/v2/jt-808/guide/': [
            {
                text: '入门',
                collapsible: true,
                icon: 'launch',
                children: [
                    '/v2/jt-808/guide/quick-start/quick-start.md',
                    '/v2/jt-808/guide/quick-start/terminology.md',
                    '/v2/jt-808/guide/quick-start/jt808-msg-type-parser.md',
                ],
            },
            {
                text: '基础',
                collapsible: true,
                icon: 'api',
                children: [
                    '/v2/jt-808/guide/basic/request-processing-overview.md',
                    '/v2/jt-808/guide/basic/request-msg-handler.md',
                    '/v2/jt-808/guide/basic/request-msg-handler-argument-resolver.md',
                    '/v2/jt-808/guide/basic/response-msg-handler.md',
                    '/v2/jt-808/guide/basic/command-sender.md',
                    '/v2/jt-808/guide/basic/request-sub-package.md',
                    '/v2/jt-808/guide/basic/response-sub-package.md',
                    '/v2/jt-808/guide/basic/handler-interceptor.md',
                    '/v2/jt-808/guide/basic/summary.md',
                ],
            },
            {
                text: "辅助工具",
                collapsible: true,
                icon: 'tool',
                children: [
                    '/v2/jt-808/guide/utilities/jt808-data-type-ops.md',
                    '/v2/jt-808/guide/utilities/msg-builder.md',
                ]
            },
            {
                text: "注解驱动开发",
                icon: 'semantic',
                collapsible: true,
                children: [
                    '/v2/jt-808/guide/annotation-based-dev/req-msg-mapping.md',
                    '/v2/jt-808/guide/annotation-based-dev/resp-msg-mapping.md',
                    '/v2/jt-808/guide/annotation-based-dev/exception-handler.md',
                    '/v2/jt-808/guide/annotation-based-dev/location-upload-msg-parse-demo.md',
                    '/v2/jt-808/guide/annotation-based-dev/location-batch-upload-msg-parse-demo.md',
                ]
            },
            {
                text: "定制化",
                collapsible: true,
                icon: 'customize',
                children: [
                    '/v2/jt-808/guide/customization/netty-config.md',
                    '/v2/jt-808/guide/customization/codec-config.md',
                    '/v2/jt-808/guide/customization/session-config.md',
                    '/v2/jt-808/guide/customization/sub-package-config.md',
                    '/v2/jt-808/guide/customization/request-lifecycle-listener.md',
                ]
            },
            {
                text: "深入",
                collapsible: true,
                icon: 'more',
                children: [
                    '/v2/jt-808/guide/more/aware-interface.md',
                ]
            },
        ],
        '/v2/jt-808/config/': ["/v2/jt-808/config/"],
        '/v1/jt-808/config/': ["/v1/jt-808/config/"],
        '/v1/jt-808/guide/': [
            {
                text: '基础',
                icon: 'launch',
                collapsible: true,
                children: [
                    '/v1/jt-808/guide/basic/quick-start.md',
                ],
            },
            {
                text: '注解驱动开发',
                collapsible: true,
                icon: 'semantic',
                children: [
                    '/v1/jt-808/guide/annotation-based-dev/req-msg-mapping',
                    '/v1/jt-808/guide/annotation-based-dev/msg-handler-register',
                    '/v1/jt-808/guide/annotation-based-dev/resp-msg-mapping',
                    '/v1/jt-808/guide/annotation-based-dev/msg-push',
                    '/v1/jt-808/guide/annotation-based-dev/exception-handler',
                    '/v1/jt-808/guide/annotation-based-dev/location-msg-parse-demo'
                ]
            },
            {
                text: "定制化",
                collapsible: true,
                icon: 'customize',
                children: [
                    '/v1/jt-808/guide/customization/session-config',
                    '/v1/jt-808/guide/customization/netty-config',
                    '/v1/jt-808/guide/customization/escape-config',
                    '/v1/jt-808/guide/customization/msg-type-config',
                    '/v1/jt-808/guide/customization/msg-converter-config',
                    '/v1/jt-808/guide/customization/msg-handler-config',
                    '/v1/jt-808/guide/customization/terminal-validator-config',
                    '/v1/jt-808/guide/customization/auth-validator-config',
                    '/v1/jt-808/guide/customization/msg-builder.md',
                ]
            },
            {
                text: '深入',
                collapsible: true,
                icon: 'more',
                children: [
                    '/v1/jt-808/guide/more/design-of-msg-processing',
                    '/v1/jt-808/guide/more/component-order',
                    '/v1/jt-808/guide/more/aware-interface'
                ]
            },
        ],
        '/protocol-introduction/': [
            {
                text: "JT/T 808",
                children: [
                    "/protocol-introduction/808/v2019.md",
                    "/protocol-introduction/808/v2013.md",
                    "/protocol-introduction/808/v2011.md",
                ]
            },

        ],
        '/frequently-asked-questions/': [
            "/frequently-asked-questions/debug.md",
            "/frequently-asked-questions/package-parsing.md",
            "/frequently-asked-questions/what-is-the-difference-between-v1-and-v2.md",
        ]
    }
)