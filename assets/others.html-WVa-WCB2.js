import{_ as a}from"./plugin-vue_export-helper-DlAUqK2U.js";import{o as t,c as o,b as n,a as e}from"./app-Djo5EUbb.js";const l={},i=e(`<h1 id="others" tabindex="-1"><a class="header-anchor" href="#others"><span>others</span></a></h1><div class="hint-container info"><p class="hint-container-title">提示</p><p>该章节介绍的是除前面章节内容之外的其他杂项配置。</p></div><h2 id="配置项总览" tabindex="-1"><a class="header-anchor" href="#配置项总览"><span>配置项总览</span></a></h2><div class="language-yaml line-numbers-mode" data-ext="yml" data-title="yml"><pre class="language-yaml"><code><span class="token key atrule">jt808</span><span class="token punctuation">:</span>
  <span class="token key atrule">built-components</span><span class="token punctuation">:</span>
    <span class="token key atrule">request-handlers</span><span class="token punctuation">:</span>
      <span class="token key atrule">enabled</span><span class="token punctuation">:</span> <span class="token boolean important">true</span>
    <span class="token key atrule">component-statistics</span><span class="token punctuation">:</span>
      <span class="token key atrule">enabled</span><span class="token punctuation">:</span> <span class="token boolean important">true</span>
<span class="token key atrule">logging</span><span class="token punctuation">:</span>
  <span class="token key atrule">level</span><span class="token punctuation">:</span>
    <span class="token key atrule">root</span><span class="token punctuation">:</span> info
    <span class="token key atrule">io.github.hylexus</span><span class="token punctuation">:</span> info
    <span class="token key atrule">jt-808.request.decoder</span><span class="token punctuation">:</span> info
    <span class="token key atrule">jt-808.response.encoder</span><span class="token punctuation">:</span> info
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="built-components" tabindex="-1"><a class="header-anchor" href="#built-components"><span>built-components</span></a></h2><h3 id="component-statistics-enabled" tabindex="-1"><a class="header-anchor" href="#component-statistics-enabled"><span>component-statistics.enabled</span></a></h3><ul><li>类型：<code>boolean</code></li><li>默认值：<code>true</code></li></ul><p><code>jt808.built-components.component-statistics.enabled</code> 表示是否开启服务启动完成后显示组件统计信息。</p><p>这些统计信息可以显示已经注册的 <strong>消息处理器</strong> 和其他 <strong>可配置的</strong> 组件。类似于下图所示：</p>`,9),p={class:""},c=["src"],r=e(`<h3 id="request-handlers-enabled" tabindex="-1"><a class="header-anchor" href="#request-handlers-enabled"><span>request-handlers.enabled</span></a></h3><ul><li>类型：<code>boolean</code></li><li>默认值：<code>true</code></li></ul><p><code>jt808.built-components.request-handlers.enabled</code> 表示是否启用内置的一些消息处理器。</p><p>内置消息处理器都在 <code>io.github.hylexus.jt.jt808.support.dispatcher.handler.builtin</code> 包下。</p><div class="hint-container warning"><p class="hint-container-title">注意</p><p>内置的处理器仅仅是示例性的，不要直接使用。</p></div><h2 id="logging" tabindex="-1"><a class="header-anchor" href="#logging"><span>logging</span></a></h2><div class="language-yaml line-numbers-mode" data-ext="yml" data-title="yml"><pre class="language-yaml"><code><span class="token key atrule">logging</span><span class="token punctuation">:</span>
  <span class="token key atrule">level</span><span class="token punctuation">:</span>
    <span class="token key atrule">root</span><span class="token punctuation">:</span> info
    <span class="token key atrule">io.github.hylexus</span><span class="token punctuation">:</span> info
    <span class="token comment"># 对应 io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgDecoder 的日志</span>
    <span class="token key atrule">jt-808.request.decoder</span><span class="token punctuation">:</span> info
    <span class="token comment"># 对应 io.github.hylexus.jt.jt808.support.codec.impl.DefaultJt808MsgEncoder 的日志</span>
    <span class="token key atrule">jt-808.response.encoder</span><span class="token punctuation">:</span> info
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,7);function u(s,d){return t(),o("div",null,[i,n("p",p,[n("img",{src:s.$withBase("/img/print-component-statistics.png"),alt:"print-component-statistics"},null,8,c)]),r])}const k=a(l,[["render",u],["__file","others.html.vue"]]),b=JSON.parse('{"path":"/v2/jt-808/config/others.html","title":"others","lang":"zh-CN","frontmatter":{"icon":"others","description":"others 提示 该章节介绍的是除前面章节内容之外的其他杂项配置。 配置项总览 built-components component-statistics.enabled 类型：boolean 默认值：true jt808.built-components.component-statistics.enabled 表示是否开启服务启动完成后显示组件统...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v2/jt-808/config/others.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"others"}],["meta",{"property":"og:description","content":"others 提示 该章节介绍的是除前面章节内容之外的其他杂项配置。 配置项总览 built-components component-statistics.enabled 类型：boolean 默认值：true jt808.built-components.component-statistics.enabled 表示是否开启服务启动完成后显示组件统..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2023-05-12T13:44:14.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2023-05-12T13:44:14.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"others\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2023-05-12T13:44:14.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"配置项总览","slug":"配置项总览","link":"#配置项总览","children":[]},{"level":2,"title":"built-components","slug":"built-components","link":"#built-components","children":[{"level":3,"title":"component-statistics.enabled","slug":"component-statistics-enabled","link":"#component-statistics-enabled","children":[]},{"level":3,"title":"request-handlers.enabled","slug":"request-handlers-enabled","link":"#request-handlers-enabled","children":[]}]},{"level":2,"title":"logging","slug":"logging","link":"#logging","children":[]}],"git":{"createdTime":1683899054000,"updatedTime":1683899054000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":0.76,"words":228},"filePathRelative":"v2/jt-808/config/others.md","localizedDate":"2023年5月12日","autoDesc":true,"excerpt":"\\n<div class=\\"hint-container info\\">\\n<p class=\\"hint-container-title\\">提示</p>\\n<p>该章节介绍的是除前面章节内容之外的其他杂项配置。</p>\\n</div>\\n<h2>配置项总览</h2>\\n<div class=\\"language-yaml\\" data-ext=\\"yml\\" data-title=\\"yml\\"><pre class=\\"language-yaml\\"><code><span class=\\"token key atrule\\">jt808</span><span class=\\"token punctuation\\">:</span>\\n  <span class=\\"token key atrule\\">built-components</span><span class=\\"token punctuation\\">:</span>\\n    <span class=\\"token key atrule\\">request-handlers</span><span class=\\"token punctuation\\">:</span>\\n      <span class=\\"token key atrule\\">enabled</span><span class=\\"token punctuation\\">:</span> <span class=\\"token boolean important\\">true</span>\\n    <span class=\\"token key atrule\\">component-statistics</span><span class=\\"token punctuation\\">:</span>\\n      <span class=\\"token key atrule\\">enabled</span><span class=\\"token punctuation\\">:</span> <span class=\\"token boolean important\\">true</span>\\n<span class=\\"token key atrule\\">logging</span><span class=\\"token punctuation\\">:</span>\\n  <span class=\\"token key atrule\\">level</span><span class=\\"token punctuation\\">:</span>\\n    <span class=\\"token key atrule\\">root</span><span class=\\"token punctuation\\">:</span> info\\n    <span class=\\"token key atrule\\">io.github.hylexus</span><span class=\\"token punctuation\\">:</span> info\\n    <span class=\\"token key atrule\\">jt-808.request.decoder</span><span class=\\"token punctuation\\">:</span> info\\n    <span class=\\"token key atrule\\">jt-808.response.encoder</span><span class=\\"token punctuation\\">:</span> info\\n</code></pre></div>"}');export{k as comp,b as data};