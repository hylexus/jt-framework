import{_ as e}from"./plugin-vue_export-helper-DlAUqK2U.js";import{o as a,c as n,a as s}from"./app-D_dj-vKV.js";const t={},o=s(`<h1 id="message-handler" tabindex="-1"><a class="header-anchor" href="#message-handler"><span>message-handler</span></a></h1><div class="hint-container info"><p class="hint-container-title">提示</p><p>该章节介绍的是 <code>jt808.msg-handler.xxx</code> 消息处理线程池相关的配置。</p><p>参见 <code>java.util.concurrent.ThreadPoolExecutor</code> 。</p></div><h2 id="配置项总览" tabindex="-1"><a class="header-anchor" href="#配置项总览"><span>配置项总览</span></a></h2><div class="language-yaml line-numbers-mode" data-ext="yml" data-title="yml"><pre class="language-yaml"><code><span class="token key atrule">jt808</span><span class="token punctuation">:</span>
  <span class="token key atrule">msg-handler</span><span class="token punctuation">:</span>
    <span class="token key atrule">enabled</span><span class="token punctuation">:</span> <span class="token boolean important">true</span>
    <span class="token key atrule">core-pool-size</span><span class="token punctuation">:</span> <span class="token number">128</span>
    <span class="token key atrule">max-pool-size</span><span class="token punctuation">:</span> <span class="token number">256</span>
    <span class="token key atrule">keep-alive</span><span class="token punctuation">:</span> 1m
    <span class="token key atrule">max-pending-tasks</span><span class="token punctuation">:</span> <span class="token number">256</span>
    <span class="token key atrule">daemon</span><span class="token punctuation">:</span> <span class="token boolean important">true</span>
    <span class="token key atrule">thread-name-prefix</span><span class="token punctuation">:</span> 808<span class="token punctuation">-</span>handler
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="core-pool-size" tabindex="-1"><a class="header-anchor" href="#core-pool-size"><span>core-pool-size</span></a></h2><p>参考 <code>java.util.concurrent.ThreadPoolExecutor.corePoolSize</code>。</p><ul><li>类型：<code>int</code></li><li>默认值：128</li></ul><h2 id="max-pool-size" tabindex="-1"><a class="header-anchor" href="#max-pool-size"><span>max-pool-size</span></a></h2><p>参考 <code>java.util.concurrent.ThreadPoolExecutor.maximumPoolSize</code>。</p><ul><li>类型：<code>int</code></li><li>默认值：256</li></ul><h2 id="keep-alive" tabindex="-1"><a class="header-anchor" href="#keep-alive"><span>keep-alive</span></a></h2><p>参考 <code>java.util.concurrent.ThreadPoolExecutor.keepAliveTime</code>。</p><ul><li>类型：<code>java.time.Duration</code></li><li>默认值：<code>1m</code></li></ul><h2 id="max-pending-tasks" tabindex="-1"><a class="header-anchor" href="#max-pending-tasks"><span>max-pending-tasks</span></a></h2><p>参考<code>java.util.concurrent.ThreadPoolExecutor.workQueue</code>。</p><ul><li>类型：<code>int</code></li><li>默认值：<code>256</code></li></ul><h2 id="daemon" tabindex="-1"><a class="header-anchor" href="#daemon"><span>daemon</span></a></h2><ul><li>类型：<code>boolean</code></li><li>默认值：<code>true</code></li></ul><h2 id="pool-name" tabindex="-1"><a class="header-anchor" href="#pool-name"><span>pool-name</span></a></h2><p>线程池的线程名前缀。</p><ul><li>类型：<code>String</code></li><li>默认值：<code>808-handler</code></li></ul>`,21),l=[o];function c(p,i){return a(),n("div",null,l)}const u=e(t,[["render",c],["__file","message-handler.html.vue"]]),m=JSON.parse('{"path":"/v2/jt-808/config/message-handler.html","title":"message-handler","lang":"zh-CN","frontmatter":{"icon":"process","description":"message-handler 提示 该章节介绍的是 jt808.msg-handler.xxx 消息处理线程池相关的配置。 参见 java.util.concurrent.ThreadPoolExecutor 。 配置项总览 core-pool-size 参考 java.util.concurrent.ThreadPoolExecutor.coreP...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v2/jt-808/config/message-handler.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"message-handler"}],["meta",{"property":"og:description","content":"message-handler 提示 该章节介绍的是 jt808.msg-handler.xxx 消息处理线程池相关的配置。 参见 java.util.concurrent.ThreadPoolExecutor 。 配置项总览 core-pool-size 参考 java.util.concurrent.ThreadPoolExecutor.coreP..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2024-11-29T15:02:48.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2024-11-29T15:02:48.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"message-handler\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2024-11-29T15:02:48.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"配置项总览","slug":"配置项总览","link":"#配置项总览","children":[]},{"level":2,"title":"core-pool-size","slug":"core-pool-size","link":"#core-pool-size","children":[]},{"level":2,"title":"max-pool-size","slug":"max-pool-size","link":"#max-pool-size","children":[]},{"level":2,"title":"keep-alive","slug":"keep-alive","link":"#keep-alive","children":[]},{"level":2,"title":"max-pending-tasks","slug":"max-pending-tasks","link":"#max-pending-tasks","children":[]},{"level":2,"title":"daemon","slug":"daemon","link":"#daemon","children":[]},{"level":2,"title":"pool-name","slug":"pool-name","link":"#pool-name","children":[]}],"git":{"createdTime":1732892568000,"updatedTime":1732892568000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":0.47,"words":142},"filePathRelative":"v2/jt-808/config/message-handler.md","localizedDate":"2024年11月29日","autoDesc":true,"excerpt":"\\n<div class=\\"hint-container info\\">\\n<p class=\\"hint-container-title\\">提示</p>\\n<p>该章节介绍的是 <code>jt808.msg-handler.xxx</code> 消息处理线程池相关的配置。</p>\\n<p>参见 <code>java.util.concurrent.ThreadPoolExecutor</code> 。</p>\\n</div>\\n<h2>配置项总览</h2>\\n<div class=\\"language-yaml\\" data-ext=\\"yml\\" data-title=\\"yml\\"><pre class=\\"language-yaml\\"><code><span class=\\"token key atrule\\">jt808</span><span class=\\"token punctuation\\">:</span>\\n  <span class=\\"token key atrule\\">msg-handler</span><span class=\\"token punctuation\\">:</span>\\n    <span class=\\"token key atrule\\">enabled</span><span class=\\"token punctuation\\">:</span> <span class=\\"token boolean important\\">true</span>\\n    <span class=\\"token key atrule\\">core-pool-size</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">128</span>\\n    <span class=\\"token key atrule\\">max-pool-size</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">256</span>\\n    <span class=\\"token key atrule\\">keep-alive</span><span class=\\"token punctuation\\">:</span> 1m\\n    <span class=\\"token key atrule\\">max-pending-tasks</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">256</span>\\n    <span class=\\"token key atrule\\">daemon</span><span class=\\"token punctuation\\">:</span> <span class=\\"token boolean important\\">true</span>\\n    <span class=\\"token key atrule\\">thread-name-prefix</span><span class=\\"token punctuation\\">:</span> 808<span class=\\"token punctuation\\">-</span>handler\\n</code></pre></div>"}');export{u as comp,m as data};
