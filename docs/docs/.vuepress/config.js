module.exports = {
    title: 'JT-framework',
    description: 'JT-framework',

    port: "9527",
    base: "/jt-framework/",
    markdown: {
        externalLinks: {
            target: '_blank', rel: 'noopener noreferrer'
        },
        anchor: {permalink: true},
        // markdown-it-toc 的选项
        toc: {includeLevel: [1]}
    },

    themeConfig: {
        navbar: navConfig(),
        sidebar: sidebarConfig(),
        sidebarDepth: 1,
        displayAllHeaders: false,
        activeHeaderLinks: true,
        lastUpdated: false,

        smoothScroll: true,

        repo: 'hylexus/jt-framework',
        repoLabel: '查看源码',
        // docsDir: 'docs/pages',
        docsBranch: 'master',
        // 默认是 false, 设置为 true 来启用
        editLinks: true,
        // 默认为 "Edit this page"
        editLinkText: '帮助我们改善此页面！'
    }
};

function sidebarConfig() {
    return {
        '/jt-808/guide/': jt808GuideSidebarConfig(),
        '/v2/jt-808/guide/': jt808GuideSidebarConfigV2(),
        '/jt-808/config/': jt808ConfSidebarConfig(),
        '/v2/jt-808/config/': jt808ConfSidebarConfigV2(),
        '/jt-808/': jt808GuideSidebarConfig(),
        '/jt-809/': jt809SidebarConfig()
    };
}

function jt808GuideSidebarConfigV2() {
    return [
        {
            title: '808协议入门',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                ['/v2/jt-808/guide/', '说明'],
                '/v2/jt-808/guide/basic/protocol-introduction',
                '/v2/jt-808/guide/basic/quick-start',
            ]
        },
        {
            title: "定制化",
            collapsable: false,
            sidebarDepth: 1,
            children: [
                ['/v2/jt-808/guide/customization/', '说明'],
                '/v2/jt-808/guide/customization/session-config',
                '/v2/jt-808/guide/customization/netty-config',
                '/v2/jt-808/guide/customization/escape-config',
                '/v2/jt-808/guide/customization/msg-type-config',
                '/v2/jt-808/guide/customization/msg-converter-config',
                '/v2/jt-808/guide/customization/msg-handler-config',
                '/v2/jt-808/guide/customization/terminal-validator-config',
                '/v2/jt-808/guide/customization/auth-validator-config',
                '/v2/jt-808/guide/customization/msg-builder.md',
            ]
        },
        {
            title: '注解驱动开发',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                ['/v2/jt-808/guide/annotation-based-dev/', '说明'],
                '/v2/jt-808/guide/annotation-based-dev/req-msg-mapping',
                '/v2/jt-808/guide/annotation-based-dev/msg-handler-register',
                '/v2/jt-808/guide/annotation-based-dev/resp-msg-mapping',
                '/v2/jt-808/guide/annotation-based-dev/msg-push',
                '/v2/jt-808/guide/annotation-based-dev/exception-handler',
                '/v2/jt-808/guide/annotation-based-dev/location-msg-parse-demo'
            ]
        },
        {
            title: '深入',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                '/v2/jt-808/guide/more/design-of-msg-processing',
                '/v2/jt-808/guide/more/component-order',
                '/v2/jt-808/guide/more/aware-interface'
            ]
        },
        {
            title: 'FAQ',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                '/v2/jt-808/guide/FAQ/package-parsing',
                '/v2/jt-808/guide/FAQ/debug'
            ]
        }
    ];
}

function jt808GuideSidebarConfig() {
    return [
        {
            title: '808协议入门',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                ['/jt-808/guide/', '说明'],
                '/jt-808/guide/basic/protocol-introduction',
                '/jt-808/guide/basic/quick-start',
            ]
        },
        {
            title: "定制化",
            collapsable: false,
            sidebarDepth: 1,
            children: [
                ['/jt-808/guide/customization/', '说明'],
                '/jt-808/guide/customization/session-config',
                '/jt-808/guide/customization/netty-config',
                '/jt-808/guide/customization/escape-config',
                '/jt-808/guide/customization/msg-type-config',
                '/jt-808/guide/customization/msg-converter-config',
                '/jt-808/guide/customization/msg-handler-config',
                '/jt-808/guide/customization/terminal-validator-config',
                '/jt-808/guide/customization/auth-validator-config',
                '/jt-808/guide/customization/msg-builder.md',
            ]
        },
        {
            title: '注解驱动开发',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                ['/jt-808/guide/annotation-based-dev/', '说明'],
                'annotation-based-dev/req-msg-mapping',
                'annotation-based-dev/msg-handler-register',
                'annotation-based-dev/resp-msg-mapping',
                'annotation-based-dev/msg-push',
                'annotation-based-dev/exception-handler',
                'annotation-based-dev/location-msg-parse-demo'
            ]
        },
        {
            title: '深入',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                // ['/jt-808/more/', '说明'],
                'more/design-of-msg-processing',
                'more/component-order',
                'more/aware-interface'
            ]
        },
        {
            title: 'FAQ',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                'FAQ/package-parsing',
                'FAQ/debug'
            ]
        }
    ];
}

function jt808ConfSidebarConfigV2() {
    return [
        {
            title: '配置',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                '/v2/jt-808/config/'
            ]
        }
    ];
}

function jt808ConfSidebarConfig() {
    return [
        {
            title: '配置',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                ''
            ]
        }
    ];
}

function jt809SidebarConfig() {
    return [
        {
            title: '809协议',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                ['', 'TODO'],
            ]
        }
    ];
}

function navConfig() {
    return [
        {text: '首页', link: '/'},
        {
            text: '指南',
            ariaLabel: '指南',
            children: [
                {text: 'V1.x', link: '/jt-808/guide/'},
                {text: 'V2.x', link: '/v2/jt-808/guide/'}
            ]
        },
        {
            text: '配置',
            ariaLabel: '配置',
            children: [
                {text: 'V1.x', link: '/jt-808/config/'},
                {text: 'V2.x', link: '/v2/jt-808/config/'}
            ]
        },
    ];
}
