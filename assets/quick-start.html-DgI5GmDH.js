import{_ as d}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as c,o as g,c as h,b as e,d as n,e as l,w as t,a as p}from"./app-Djo5EUbb.js";const m={},k=e("h1",{id:"快速开始",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#快速开始"},[e("span",null,"快速开始")])],-1),v=e("p",null,[n("此处将展示一个 最少配置 的 "),e("code",null,"808协议"),n(" 消息处理服务的搭建。")],-1),b={class:"hint-container tip"},_=e("p",{class:"hint-container-title"},"传送门",-1),x={href:"https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-bare",title:"jt-808-server-sample-bare",target:"_blank",rel:"noopener noreferrer"},y=e("h2",{id:"创建工程",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#创建工程"},[e("span",null,"创建工程")])],-1),f=e("p",null,[n("创建一个空的 "),e("code",null,"spring-boot"),n(" 工程。")],-1),j={class:"hint-container tip"},q=e("p",{class:"hint-container-title"},"传送门",-1),I={href:"https://start.spring.io",target:"_blank",rel:"noopener noreferrer"},w=e("h2",{id:"添加依赖",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#添加依赖"},[e("span",null,"添加依赖")])],-1),B=e("h3",{id:"spring-boot-2-x",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#spring-boot-2-x"},[e("span",null,"spring-boot-2.x")])],-1),A=e("p",null,[n("使用 "),e("code",null,"spring-boot-2.x"),n(" 的项目引入为 "),e("code",null,"808协议"),n(" 提供的 "),e("code",null,"jt-808-server-spring-boot-starter-boot2")],-1),S=e("div",{class:"language-xml line-numbers-mode","data-ext":"xml","data-title":"xml"},[e("pre",{class:"language-xml"},[e("code",null,[n(`
`),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"<"),n("dependency")]),e("span",{class:"token punctuation"},">")]),n(`
    `),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"<"),n("groupId")]),e("span",{class:"token punctuation"},">")]),n("io.github.hylexus.jt"),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"</"),n("groupId")]),e("span",{class:"token punctuation"},">")]),n(`
    `),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"<"),n("artifactId")]),e("span",{class:"token punctuation"},">")]),n("jt-808-server-spring-boot-starter-boot2"),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"</"),n("artifactId")]),e("span",{class:"token punctuation"},">")]),n(`
    `),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"<"),n("version")]),e("span",{class:"token punctuation"},">")]),n("2.3.0-rc.3"),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"</"),n("version")]),e("span",{class:"token punctuation"},">")]),n(`
`),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"</"),n("dependency")]),e("span",{class:"token punctuation"},">")]),n(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),T=e("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy","data-title":"groovy"},[e("pre",{class:"language-groovy"},[e("code",null,[n("implementation "),e("span",{class:"token string"},"'io.github.hylexus.jt:jt-808-server-spring-boot-starter-boot2:2.3.0-rc.3'"),n(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"})])],-1),C=e("h3",{id:"spring-boot-3-x",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#spring-boot-3-x"},[e("span",null,"spring-boot-3.x")])],-1),z=e("p",null,[n("使用 "),e("code",null,"spring-boot-3.x"),n(" 的项目引入为 "),e("code",null,"808协议"),n(" 提供的 "),e("code",null,"jt-808-server-spring-boot-starter")],-1),N=e("div",{class:"language-xml line-numbers-mode","data-ext":"xml","data-title":"xml"},[e("pre",{class:"language-xml"},[e("code",null,[n(`
`),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"<"),n("dependency")]),e("span",{class:"token punctuation"},">")]),n(`
    `),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"<"),n("groupId")]),e("span",{class:"token punctuation"},">")]),n("io.github.hylexus.jt"),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"</"),n("groupId")]),e("span",{class:"token punctuation"},">")]),n(`
    `),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"<"),n("artifactId")]),e("span",{class:"token punctuation"},">")]),n("jt-808-server-spring-boot-starter"),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"</"),n("artifactId")]),e("span",{class:"token punctuation"},">")]),n(`
    `),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"<"),n("version")]),e("span",{class:"token punctuation"},">")]),n("2.3.0-rc.3"),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"</"),n("version")]),e("span",{class:"token punctuation"},">")]),n(`
`),e("span",{class:"token tag"},[e("span",{class:"token tag"},[e("span",{class:"token punctuation"},"</"),n("dependency")]),e("span",{class:"token punctuation"},">")]),n(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"}),e("div",{class:"line-number"})])],-1),E=e("div",{class:"language-groovy line-numbers-mode","data-ext":"groovy","data-title":"groovy"},[e("pre",{class:"language-groovy"},[e("code",null,[n("implementation "),e("span",{class:"token string"},"'io.github.hylexus.jt:jt-808-server-spring-boot-starter:2.3.0-rc.3'"),n(`
`)])]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"})])],-1),L=p(`<h2 id="配置" tabindex="-1"><a class="header-anchor" href="#配置"><span>配置</span></a></h2><ul><li>application.yml</li></ul><div class="language-yaml line-numbers-mode" data-ext="yml" data-title="yml"><pre class="language-yaml"><code><span class="token key atrule">jt808</span><span class="token punctuation">:</span>
  <span class="token key atrule">built-components</span><span class="token punctuation">:</span>
    <span class="token key atrule">component-statistics</span><span class="token punctuation">:</span>
      <span class="token key atrule">enabled</span><span class="token punctuation">:</span> <span class="token boolean important">true</span>
    <span class="token key atrule">request-handlers</span><span class="token punctuation">:</span>
      <span class="token key atrule">enabled</span><span class="token punctuation">:</span> <span class="token boolean important">true</span>
<span class="token key atrule">logging</span><span class="token punctuation">:</span>
  <span class="token key atrule">level.root</span><span class="token punctuation">:</span> info
  <span class="token key atrule">level.io.github.hylexus</span><span class="token punctuation">:</span> info
  <span class="token key atrule">level.jt-808.request.decoder</span><span class="token punctuation">:</span> debug
  <span class="token key atrule">level.jt-808.response.encoder</span><span class="token punctuation">:</span> debug
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="测试" tabindex="-1"><a class="header-anchor" href="#测试"><span>测试</span></a></h2><h3 id="启动项目" tabindex="-1"><a class="header-anchor" href="#启动项目"><span>启动项目</span></a></h3><p>至此，对 <strong>808消息</strong> 的处理服务已经搭建完毕。启动 <code>spring-boot</code> 项目开始测试。</p>`,6),V={class:""},D=["src"],P=p('<p>可以从启动日志中的<code>组件统计信息</code>中看到内置了一些消息处理器：</p><ul><li><code>0x0100</code> 终端注册</li><li><code>0x0102</code> 终端鉴权</li><li><code>0x0200</code> 定位数据上报</li><li><code>0x0704</code> 定位数据批量上报</li></ul><p>所以现在可以测试接收内置的这些类型的消息了。下面以 <strong>2019</strong> 版的 <strong>终端注册消息</strong> 为例进行测试：</p><div class="hint-container tip"><p class="hint-container-title">配置提示</p><ul><li><code>组件统计信息</code> 的开关由配置项 <code>jt808.print-component-statistics = true|false</code> 来控制</li><li>默认的 <code>TCP</code> 端口为 <code>6808</code></li></ul></div><h3 id="发报文" tabindex="-1"><a class="header-anchor" href="#发报文"><span>发报文</span></a></h3>',5),R={class:"hint-container caution"},Z=e("p",{class:"hint-container-title"},"警告",-1),$=e("p",null,"注意：",-1),J=e("code",null,"十六进制格式",-1),M=e("ul",null,[e("li",null,"报文")],-1),O=e("div",{class:"language-text line-numbers-mode","data-ext":"text","data-title":"text"},[e("pre",{class:"language-text"},[e("code",null,`[7E010040560100000000013912344321007B000B0000313233353931323335393131323334353637383930313233343536373839303132333435363738393069643132333435363738393031323334353637383930313233343536373801B8CA4A2D3635343332313C7E]
`)]),e("div",{class:"line-numbers","aria-hidden":"true"},[e("div",{class:"line-number"})])],-1),W=e("ul",null,[e("li",null,"客户端")],-1),X={class:""},F=["src"],G=e("h3",{id:"服务端",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#服务端"},[e("span",null,"服务端")])],-1),H={class:""},K=["src"],Q={class:"hint-container tip"},U=e("p",{class:"hint-container-title"},"传送门",-1),Y={href:"https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-bare",title:"jt-808-server-sample-bare",target:"_blank",rel:"noopener noreferrer"};function ee(o,ne){const i=c("ExternalLinkIcon"),r=c("CodeTabs"),u=c("RouteLink");return g(),h("div",null,[k,v,e("div",b,[_,e("p",null,[n("本小节的示例可以在 "),e("a",x,[n("samples/jt-808-server-sample-bare"),l(i)]),n(" 下找到对应代码。")])]),y,f,e("div",j,[q,e("p",null,[n("可以使用 "),e("a",I,[n("Spring Initializer"),l(i)]),n(" 快速初始化一个 Spring Boot 工程。")])]),w,B,A,l(r,{id:"31",data:[{id:"maven"},{id:"gradle"}],active:1,"tab-id":"gradle"},{title0:t(({value:s,isActive:a})=>[n("maven")]),title1:t(({value:s,isActive:a})=>[n("gradle")]),tab0:t(({value:s,isActive:a})=>[S]),tab1:t(({value:s,isActive:a})=>[T]),_:1}),C,z,l(r,{id:"45",data:[{id:"maven"},{id:"gradle"}],active:1,"tab-id":"gradle"},{title0:t(({value:s,isActive:a})=>[n("maven")]),title1:t(({value:s,isActive:a})=>[n("gradle")]),tab0:t(({value:s,isActive:a})=>[N]),tab1:t(({value:s,isActive:a})=>[E]),_:1}),L,e("p",V,[e("img",{src:o.$withBase("/img/v2/quick-start/print-component-statistics.jpg")},null,8,D)]),P,e("div",R,[Z,$,e("ul",null,[e("li",null,[n("无论以什么发包工具发包，都请记得以 "),J,n(" 发送！可以参考 "),l(u,{to:"/frequently-asked-questions/debug.html"},{default:t(()=>[n("推荐发包工具")]),_:1}),n(" 。")])])]),M,O,W,e("p",X,[e("img",{src:o.$withBase("/img/v2/quick-start/register-msg-2019-client.png")},null,8,F)]),G,e("p",H,[e("img",{src:o.$withBase("/img/v2/quick-start/register-msg-2019-breakpoint.png")},null,8,K)]),e("div",Q,[U,e("p",null,[n("本小节的示例可以在 "),e("a",Y,[n("samples/jt-808-server-sample-bare"),l(i)]),n(" 下找到对应代码。")])])])}const ae=d(m,[["render",ee],["__file","quick-start.html.vue"]]),le=JSON.parse('{"path":"/v2/jt-808/guide/quick-start/quick-start.html","title":"快速开始","lang":"zh-CN","frontmatter":{"icon":"launch","description":"快速开始 此处将展示一个 最少配置 的 808协议 消息处理服务的搭建。 传送门 本小节的示例可以在 samples/jt-808-server-sample-bare 下找到对应代码。 创建工程 创建一个空的 spring-boot 工程。 传送门 可以使用 Spring Initializer 快速初始化一个 Spring Boot 工程。 添加依...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v2/jt-808/guide/quick-start/quick-start.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"快速开始"}],["meta",{"property":"og:description","content":"快速开始 此处将展示一个 最少配置 的 808协议 消息处理服务的搭建。 传送门 本小节的示例可以在 samples/jt-808-server-sample-bare 下找到对应代码。 创建工程 创建一个空的 spring-boot 工程。 传送门 可以使用 Spring Initializer 快速初始化一个 Spring Boot 工程。 添加依..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2024-12-11T12:36:30.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2024-12-11T12:36:30.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"快速开始\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2024-12-11T12:36:30.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"创建工程","slug":"创建工程","link":"#创建工程","children":[]},{"level":2,"title":"添加依赖","slug":"添加依赖","link":"#添加依赖","children":[{"level":3,"title":"spring-boot-2.x","slug":"spring-boot-2-x","link":"#spring-boot-2-x","children":[]},{"level":3,"title":"spring-boot-3.x","slug":"spring-boot-3-x","link":"#spring-boot-3-x","children":[]}]},{"level":2,"title":"配置","slug":"配置","link":"#配置","children":[]},{"level":2,"title":"测试","slug":"测试","link":"#测试","children":[{"level":3,"title":"启动项目","slug":"启动项目","link":"#启动项目","children":[]},{"level":3,"title":"发报文","slug":"发报文","link":"#发报文","children":[]},{"level":3,"title":"服务端","slug":"服务端","link":"#服务端","children":[]}]}],"git":{"createdTime":1671812085000,"updatedTime":1733920590000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":17},{"name":"zhangxiaobing0926","email":"1qaz@WSX","commits":1}]},"readingTime":{"minutes":1.83,"words":550},"filePathRelative":"v2/jt-808/guide/quick-start/quick-start.md","localizedDate":"2022年12月24日","autoDesc":true,"excerpt":"\\n<p>此处将展示一个 最少配置 的 <code>808协议</code> 消息处理服务的搭建。</p>\\n<div class=\\"hint-container tip\\">\\n<p class=\\"hint-container-title\\">传送门</p>\\n<p>本小节的示例可以在 <a href=\\"https://github.com/hylexus/jt-framework/tree/master/samples/jt-808-server-sample-bare\\" title=\\"jt-808-server-sample-bare\\" target=\\"_blank\\" rel=\\"noopener noreferrer\\">samples/jt-808-server-sample-bare</a> 下找到对应代码。</p>\\n</div>"}');export{ae as comp,le as data};