import{_ as l}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r,o as i,c as o,b as e,d as n,e as s,a}from"./app-Djo5EUbb.js";const c={},u=a('<h1 id="_2-0-x" tabindex="-1"><a class="header-anchor" href="#_2-0-x"><span>2.0.x</span></a></h1><h2 id="_2-0-3-release" tabindex="-1"><a class="header-anchor" href="#_2-0-3-release"><span>2.0.3-RELEASE</span></a></h2><h3 id="⭐-new-features" tabindex="-1"><a class="header-anchor" href="#⭐-new-features"><span>⭐ New Features</span></a></h3><ul><li><code>Jt808Session</code> 新增如下方法: <ul><li><code>setAttribute</code></li><li><code>getAttribute</code></li><li><code>getRequiredAttribute</code></li><li><code>removeAttribute</code></li></ul></li><li><code>Jt808ServerExchange</code> 新增如下方法 <ul><li><code>removeAttribute</code></li></ul></li></ul><h3 id="🐞-bug-fixes" tabindex="-1"><a class="header-anchor" href="#🐞-bug-fixes"><span>🐞 Bug Fixes</span></a></h3>',5),d={href:"https://github.com/hylexus/jt-framework/issues/66",target:"_blank",rel:"noopener noreferrer"},h=e("li",null,"配置项默认值调整:",-1),p=a(`<div class="language-yaml line-numbers-mode" data-ext="yml" data-title="yml"><pre class="language-yaml"><code><span class="token key atrule">jt808</span><span class="token punctuation">:</span>
  <span class="token key atrule">server</span><span class="token punctuation">:</span>
  <span class="token key atrule">idle-state-handler</span><span class="token punctuation">:</span>
    <span class="token comment"># 改动原因见 https://github.com/hylexus/jt-framework/issues/66</span>
    <span class="token key atrule">writer-idle-time</span><span class="token punctuation">:</span> 0s <span class="token comment"># 由 20m 改为 0s(disabled)</span>
    <span class="token key atrule">all-idle-time</span><span class="token punctuation">:</span> 0s <span class="token comment"># 由 20m 改为 0s(disabled)</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="_2-0-3-rc1" tabindex="-1"><a class="header-anchor" href="#_2-0-3-rc1"><span>2.0.3-rc1</span></a></h2><h3 id="⭐-new-features-1" tabindex="-1"><a class="header-anchor" href="#⭐-new-features-1"><span>⭐ New Features</span></a></h3><ul><li><code>Jt808Session</code> 新增如下方法: <ul><li><code>setAttribute</code></li><li><code>getAttribute</code></li><li><code>getRequiredAttribute</code></li><li><code>removeAttribute</code></li></ul></li><li><code>Jt808ServerExchange</code> 新增如下方法 <ul><li><code>removeAttribute</code></li></ul></li></ul><h3 id="🐞-bug-fixes-1" tabindex="-1"><a class="header-anchor" href="#🐞-bug-fixes-1"><span>🐞 Bug Fixes</span></a></h3>`,5),g={href:"https://github.com/hylexus/jt-framework/issues/66",target:"_blank",rel:"noopener noreferrer"},b=e("li",null,"配置项默认值调整:",-1),m=a(`<div class="language-yaml line-numbers-mode" data-ext="yml" data-title="yml"><pre class="language-yaml"><code><span class="token key atrule">jt808</span><span class="token punctuation">:</span>
  <span class="token key atrule">server</span><span class="token punctuation">:</span>
    <span class="token key atrule">idle-state-handler</span><span class="token punctuation">:</span>
      <span class="token comment"># 改动原因见 https://github.com/hylexus/jt-framework/issues/66</span>
      <span class="token key atrule">writer-idle-time</span><span class="token punctuation">:</span> 0s <span class="token comment"># 由 20m 改为 0s(disabled)</span>
      <span class="token key atrule">all-idle-time</span><span class="token punctuation">:</span> 0s <span class="token comment"># 由 20m 改为 0s(disabled)</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="_2-0-2-release" tabindex="-1"><a class="header-anchor" href="#_2-0-2-release"><span>2.0.2-RELEASE</span></a></h2><h3 id="⭐-new-features-2" tabindex="-1"><a class="header-anchor" href="#⭐-new-features-2"><span>⭐ New Features</span></a></h3><ul><li>新增 <code>Jt808RequestLifecycleListener</code></li></ul><h3 id="📔-documentation" tabindex="-1"><a class="header-anchor" href="#📔-documentation"><span>📔 Documentation</span></a></h3><ul><li>新增 <strong>Jt808RequestLifecycleListener</strong> 文档</li><li>新增 <strong>辅助工具</strong> 文档</li></ul><h3 id="❤️-contributors" tabindex="-1"><a class="header-anchor" href="#❤️-contributors"><span>❤️ Contributors</span></a></h3>`,7),x={href:"https://github.com/hylexus",target:"_blank",rel:"noopener noreferrer"},f=a('<h2 id="_2-0-2-rc2" tabindex="-1"><a class="header-anchor" href="#_2-0-2-rc2"><span>2.0.2-rc2</span></a></h2><h3 id="⭐-new-features-3" tabindex="-1"><a class="header-anchor" href="#⭐-new-features-3"><span>⭐ New Features</span></a></h3><ul><li>新增 <code>Jt808MsgBuilder</code></li><li>新增 <code>ByteArrayFieldSerializer</code></li></ul><h3 id="🔨-dependency-upgrades" tabindex="-1"><a class="header-anchor" href="#🔨-dependency-upgrades"><span>🔨 Dependency Upgrades</span></a></h3><ul><li><code>Spring-Boot</code> 版本升级到 <strong>2.5.12</strong></li></ul><h3 id="❤️-contributors-1" tabindex="-1"><a class="header-anchor" href="#❤️-contributors-1"><span>❤️ Contributors</span></a></h3>',6),_={href:"https://github.com/hylexus",target:"_blank",rel:"noopener noreferrer"},k=e("h2",{id:"_2-0-2-rc1",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#_2-0-2-rc1"},[e("span",null,"2.0.2-rc1")])],-1),v=e("h3",{id:"🐞-bug-fixes-2",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#🐞-bug-fixes-2"},[e("span",null,"🐞 Bug Fixes")])],-1),y={href:"https://github.com/hylexus/jt-framework/issues/64",target:"_blank",rel:"noopener noreferrer"},w=a('<h3 id="⭐-new-features-4" tabindex="-1"><a class="header-anchor" href="#⭐-new-features-4"><span>⭐ New Features</span></a></h3><ul><li>去掉 <code>EventBus</code>，业务线程池使用自定义的 <code>EventExecutorGroup</code></li><li>废弃 <code>jt808.msg-processor.thread-pool.xxx</code> 配置项，使用 <code>jt808.msg-processor.executor-group.xxx</code> 代替</li></ul><h3 id="🔨-dependency-upgrades-1" tabindex="-1"><a class="header-anchor" href="#🔨-dependency-upgrades-1"><span>🔨 Dependency Upgrades</span></a></h3><ul><li><code>Gradle</code> 版本升级到 <strong>6.8.1</strong></li><li><code>Spring-Boot</code> 版本升级到 <strong>2.5.7</strong></li><li><code>Netty</code> 版本升级到 <strong>4.1.75.Final</strong></li></ul><h3 id="❤️-contributors-2" tabindex="-1"><a class="header-anchor" href="#❤️-contributors-2"><span>❤️ Contributors</span></a></h3>',5),E={href:"https://github.com/hylexus",target:"_blank",rel:"noopener noreferrer"},A=e("h2",{id:"_2-0-1-release",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#_2-0-1-release"},[e("span",null,"2.0.1-RELEASE")])],-1),F=e("h3",{id:"🐞-bug-fixes-3",tabindex:"-1"},[e("a",{class:"header-anchor",href:"#🐞-bug-fixes-3"},[e("span",null,"🐞 Bug Fixes")])],-1),S={href:"https://github.com/hylexus/jt-framework/issues/63",target:"_blank",rel:"noopener noreferrer"},N=a('<h2 id="_2-0-0-release" tabindex="-1"><a class="header-anchor" href="#_2-0-0-release"><span>2.0.0-RELEASE</span></a></h2><h3 id="⭐-new-features-5" tabindex="-1"><a class="header-anchor" href="#⭐-new-features-5"><span>⭐ New Features</span></a></h3><ul><li>注解驱动开发(支持<code>SpEL</code>)</li><li>支持消息分包</li><li>支持多版本</li></ul><h3 id="📔-documentation-1" tabindex="-1"><a class="header-anchor" href="#📔-documentation-1"><span>📔 Documentation</span></a></h3><p>新增 <strong>2.x</strong> 文档。</p><h3 id="❤️-contributors-3" tabindex="-1"><a class="header-anchor" href="#❤️-contributors-3"><span>❤️ Contributors</span></a></h3>',6),R={href:"https://github.com/dfEric",target:"_blank",rel:"noopener noreferrer"},B={href:"https://github.com/hylexus",target:"_blank",rel:"noopener noreferrer"};function L(j,J){const t=r("ExternalLinkIcon");return i(),o("div",null,[u,e("ul",null,[e("li",null,[e("a",d,[n("#66"),s(t)])]),h]),p,e("ul",null,[e("li",null,[e("a",g,[n("https://github.com/hylexus/jt-framework/issues/66"),s(t)])]),b]),m,e("ul",null,[e("li",null,[e("a",x,[n("@hylexus"),s(t)])])]),f,e("ul",null,[e("li",null,[e("a",_,[n("@hylexus"),s(t)])])]),k,v,e("ul",null,[e("li",null,[e("a",y,[n("https://github.com/hylexus/jt-framework/issues/64"),s(t)])])]),w,e("ul",null,[e("li",null,[e("a",E,[n("@hylexus"),s(t)])])]),A,F,e("ul",null,[e("li",null,[e("a",S,[n("https://github.com/hylexus/jt-framework/issues/63"),s(t)])])]),N,e("ul",null,[e("li",null,[e("a",R,[n("@dfEric"),s(t)])]),e("li",null,[e("a",B,[n("@hylexus"),s(t)])])])])}const q=l(c,[["render",L],["__file","2.0.x.html.vue"]]),T=JSON.parse('{"path":"/v2/release-notes/2.0.x.html","title":"2.0.x","lang":"zh-CN","frontmatter":{"icon":"branch","description":"2.0.x 2.0.3-RELEASE ⭐ New Features Jt808Session 新增如下方法: setAttribute getAttribute getRequiredAttribute removeAttribute Jt808ServerExchange 新增如下方法 removeAttribute 🐞 Bug Fixes #6...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v2/release-notes/2.0.x.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"2.0.x"}],["meta",{"property":"og:description","content":"2.0.x 2.0.3-RELEASE ⭐ New Features Jt808Session 新增如下方法: setAttribute getAttribute getRequiredAttribute removeAttribute Jt808ServerExchange 新增如下方法 removeAttribute 🐞 Bug Fixes #6..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2023-05-12T13:44:14.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2023-05-12T13:44:14.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"2.0.x\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2023-05-12T13:44:14.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"2.0.3-RELEASE","slug":"_2-0-3-release","link":"#_2-0-3-release","children":[{"level":3,"title":"⭐ New Features","slug":"⭐-new-features","link":"#⭐-new-features","children":[]},{"level":3,"title":"🐞 Bug Fixes","slug":"🐞-bug-fixes","link":"#🐞-bug-fixes","children":[]}]},{"level":2,"title":"2.0.3-rc1","slug":"_2-0-3-rc1","link":"#_2-0-3-rc1","children":[{"level":3,"title":"⭐ New Features","slug":"⭐-new-features-1","link":"#⭐-new-features-1","children":[]},{"level":3,"title":"🐞 Bug Fixes","slug":"🐞-bug-fixes-1","link":"#🐞-bug-fixes-1","children":[]}]},{"level":2,"title":"2.0.2-RELEASE","slug":"_2-0-2-release","link":"#_2-0-2-release","children":[{"level":3,"title":"⭐ New Features","slug":"⭐-new-features-2","link":"#⭐-new-features-2","children":[]},{"level":3,"title":"📔 Documentation","slug":"📔-documentation","link":"#📔-documentation","children":[]},{"level":3,"title":"❤️ Contributors","slug":"❤️-contributors","link":"#❤️-contributors","children":[]}]},{"level":2,"title":"2.0.2-rc2","slug":"_2-0-2-rc2","link":"#_2-0-2-rc2","children":[{"level":3,"title":"⭐ New Features","slug":"⭐-new-features-3","link":"#⭐-new-features-3","children":[]},{"level":3,"title":"🔨 Dependency Upgrades","slug":"🔨-dependency-upgrades","link":"#🔨-dependency-upgrades","children":[]},{"level":3,"title":"❤️ Contributors","slug":"❤️-contributors-1","link":"#❤️-contributors-1","children":[]}]},{"level":2,"title":"2.0.2-rc1","slug":"_2-0-2-rc1","link":"#_2-0-2-rc1","children":[{"level":3,"title":"🐞 Bug Fixes","slug":"🐞-bug-fixes-2","link":"#🐞-bug-fixes-2","children":[]},{"level":3,"title":"⭐ New Features","slug":"⭐-new-features-4","link":"#⭐-new-features-4","children":[]},{"level":3,"title":"🔨 Dependency Upgrades","slug":"🔨-dependency-upgrades-1","link":"#🔨-dependency-upgrades-1","children":[]},{"level":3,"title":"❤️ Contributors","slug":"❤️-contributors-2","link":"#❤️-contributors-2","children":[]}]},{"level":2,"title":"2.0.1-RELEASE","slug":"_2-0-1-release","link":"#_2-0-1-release","children":[{"level":3,"title":"🐞 Bug Fixes","slug":"🐞-bug-fixes-3","link":"#🐞-bug-fixes-3","children":[]}]},{"level":2,"title":"2.0.0-RELEASE","slug":"_2-0-0-release","link":"#_2-0-0-release","children":[{"level":3,"title":"⭐ New Features","slug":"⭐-new-features-5","link":"#⭐-new-features-5","children":[]},{"level":3,"title":"📔 Documentation","slug":"📔-documentation-1","link":"#📔-documentation-1","children":[]},{"level":3,"title":"❤️ Contributors","slug":"❤️-contributors-3","link":"#❤️-contributors-3","children":[]}]}],"git":{"createdTime":1683899054000,"updatedTime":1683899054000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":1.01,"words":304},"filePathRelative":"v2/release-notes/2.0.x.md","localizedDate":"2023年5月12日","autoDesc":true,"excerpt":"\\n<h2>2.0.3-RELEASE</h2>\\n<h3>⭐ New Features</h3>\\n<ul>\\n<li><code>Jt808Session</code> 新增如下方法:\\n<ul>\\n<li><code>setAttribute</code></li>\\n<li><code>getAttribute</code></li>\\n<li><code>getRequiredAttribute</code></li>\\n<li><code>removeAttribute</code></li>\\n</ul>\\n</li>\\n<li><code>Jt808ServerExchange</code> 新增如下方法\\n<ul>\\n<li><code>removeAttribute</code></li>\\n</ul>\\n</li>\\n</ul>"}');export{q as comp,T as data};