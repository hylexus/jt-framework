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
        nav: navConfig(),
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
        '/jt-808/config/': jt808ConfSidebarConfig(),
        '/jt-808/best-practices/': jt808BestPracticesSidebarConfig(),
        '/jt-808/': jt808GuideSidebarConfig(),
        '/jt-809/': jt809SidebarConfig()
    };
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
                '/jt-808/guide/basic/customized'
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

function jt808BestPracticesSidebarConfig() {
    return [
        {
            title: '使用建议',
            collapsable: false,
            sidebarDepth: 1,
            children: [
                ['/jt-808/best-practices/', '说明'],
                '/jt-808/best-practices/msg-type',
                '/jt-808/best-practices/request-msg-body-converter',
                '/jt-808/best-practices/msg-handler',
                '/jt-808/best-practices/response-msg-body-converter',
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
        // {text: '指南', link: '/guide/'},
        {text: '指南', link: '/jt-808/guide/'},
        {text: '配置', link: '/jt-808/config/'},
        //{text: '使用建议', link: '/jt-808/best-practices/'},
        // {
        //     text: '协议类型',
        //     ariaLabel: '协议类型',
        //     items: [
        //         {text: 'Jt-808', link: '/jt-808/guide/'},
        //         {text: 'Jt-809', link: '/jt-809/'}
        //     ]
        // }
    ];
}
