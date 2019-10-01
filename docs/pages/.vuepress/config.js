module.exports = {
    title: 'JT-framework',
    description: 'JT-framework',

    themeConfig: {
        nav: [
            {text: '首页', link: '/'},
            {text: '指南', link: '/guide/'},
            {
                text: '协议类型',
                ariaLabel: '协议类型',
                items: [
                    {text: 'Jt-808', link: '/jt-808/'},
                    {text: 'Jt-809', link: '/jt-809/'}
                ]
            },
            {text: '部标协议简介', link: '/basic/'}
        ],
        sidebar: [
            '/',
            ['/basic/', '部标协议简介'],
            ['/guide/', '指南'],
            ['/jt-808/', '808协议'],
            ['/jt-809/', '809协议']
        ],
        sidebarDepth: 1,
        displayAllHeaders: true,
        activeHeaderLinks: true,
        lastUpdated: 'Last Updated',

        smoothScroll: true,

        repo: 'hylexus/jt-framework',
        repoLabel: '查看源码',
        docsDir: 'docs/pages',
        docsBranch: 'master'
        // 默认是 false, 设置为 true 来启用
        // editLinks: true,
        // 默认为 "Edit this page"
        // editLinkText: '帮助我们改善此页面！'
    }
};
