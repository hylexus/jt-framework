module.exports = {
    title: 'JT-framework',
    description: 'JT-framework',

    port: "9527",
    dest: "docs",
    base: "/",
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
        sidebar: {
            '/': homeSidebarConfig()
        },
        sidebarDepth: 1,
        displayAllHeaders: true,
        activeHeaderLinks: true,
        lastUpdated: 'Last Updated',

        smoothScroll: true,

        repo: 'hylexus/jt-framework',
        repoLabel: '查看源码',
        docsDir: 'docs/pages',
        docsBranch: 'master',
        // 默认是 false, 设置为 true 来启用
        editLinks: true,
        // 默认为 "Edit this page"
        editLinkText: '帮助我们改善此页面！'
    }
};

function homeSidebarConfig() {
    return [
        ['/', '首页'],
        ['/guide/', '指南'],
        {
            title: '808协议',
            collapsable: true,
            children: [
                ['/jt-808/protocol-introduction/', '协议扫盲'],
                ['/jt-808/quick-start/', '快速开始'],
                ['/jt-808/customized/', '定制化'],
                ['/jt-808/FAQ/', 'FAQ']
            ]
        },
        ['/jt-809/', '809协议']
    ];
}

function navConfig() {
    return [
        {text: '首页', link: '/'},
        {text: '指南', link: '/guide/'},
        {
            text: '协议类型',
            ariaLabel: '协议类型',
            items: [
                {text: 'Jt-808', link: '/jt-808/'},
                {text: 'Jt-809', link: '/jt-809/'}
            ]
        }
    ];
}
