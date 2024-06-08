import {sidebar} from "vuepress-theme-hope";

export const zhSidebar = sidebar({
        '/v2/jt-808/guide/': [
            {
                text: '入门',
                collapsible: true,
                icon: 'launch',
                children: [
                    '/v2/jt-808/guide/quick-start/quick-start.md',
                    '/v2/jt-808/guide/quick-start/compatibility.md',
                    '/v2/jt-808/guide/quick-start/terminology.md',
                    '/v2/jt-808/guide/quick-start/jt808-msg-type-parser.md',
                    '/v2/jt-808/guide/quick-start/building-from-source.md',
                    '/v2/jt-808/guide/quick-start/maven-samples.md',
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
                    // '/v2/jt-808/guide/basic/summary.md',
                ],
            },
            {
                text: "辅助工具",
                collapsible: true,
                icon: 'tool',
                children: [
                    '/v2/jt-808/guide/utilities/jt808-data-type-ops.md',
                    '/v2/jt-808/guide/utilities/msg-builder.md',
                    '/v2/jt-808/guide/utilities/bit-operator.md',
                    '/v2/jt-808/guide/utilities/byte-buf-container.md',
                    '/v2/jt-808/guide/utilities/byte-array-container.md',
                ]
            },
            {
                text: "注解驱动开发",
                icon: 'semantic',
                collapsible: true,
                children: [
                    '/v2/jt-808/guide/annotation-based-dev/data-type-mapping.md',
                    '/v2/jt-808/guide/annotation-based-dev/data-type-mapping-via-alias.md',
                    '/v2/jt-808/guide/annotation-based-dev/req-msg-mapping.md',
                    '/v2/jt-808/guide/annotation-based-dev/resp-msg-mapping.md',
                    '/v2/jt-808/guide/annotation-based-dev/annotation-alias.md',
                    '/v2/jt-808/guide/annotation-based-dev/custom-annotation.md',
                    '/v2/jt-808/guide/annotation-based-dev/exception-handler.md',
                    '/v2/jt-808/guide/annotation-based-dev/location-upload-msg-parse-demo.md',
                    '/v2/jt-808/guide/annotation-based-dev/location-batch-upload-msg-parse-demo.md',
                    '/v2/jt-808/guide/annotation-based-dev/extra-location-msg-paring.md',
                    '/v2/jt-808/guide/annotation-based-dev/notices-for-builtin-msg.md',
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
                    '/v2/jt-808/guide/customization/jt-808-request-filter.md',
                    '/v2/jt-808/guide/customization/msg-encryption.md',
                ]
            },
            {
                text: "协议扩展",
                collapsible: true,
                icon: 'extend',
                children: [
                    '/v2/jt-808/guide/protocol-extension/jangsu/index.md',
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
            {
                text: "升级指南",
                collapsible: true,
                icon: 'merge',
                children: [
                    '/v2/jt-808/guide/migration/migrating-from-2.0.x-to-2.1.x.md',
                ]
            },
        ],
        '/v2/release-notes': [
            '/v2/release-notes/latest.md',
            '/v2/release-notes/2.0.x.md',
            '/v2/release-notes/1.x.md',
        ],
        '/v2/jt-808/config/': [
            "/v2/jt-808/config/README.md",
            "/v2/jt-808/config/features.md",
            "/v2/jt-808/config/protocol.md",
            "/v2/jt-808/config/server.md",
            "/v2/jt-808/config/message-processor.md",
            "/v2/jt-808/config/sub-package.md",
            "/v2/jt-808/config/others.md",
            "/v2/jt-808/config/extension-jiangsu.md",
        ],
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
                    "/protocol-introduction/808/type-mapping.md",
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
