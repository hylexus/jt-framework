import{_ as e}from"./plugin-vue_export-helper-DlAUqK2U.js";import{o as s,c as n,a}from"./app-Djo5EUbb.js";const o={},t=a(`<h1 id="message-processor" tabindex="-1"><a class="header-anchor" href="#message-processor"><span>message-processor</span></a></h1><div class="hint-container info"><p class="hint-container-title">提示</p><p>该章节介绍的是 <code>jt808.msg-processor.executor-group.xxx</code> 消息处理线程池相关的配置。</p><p>参见 <code>io.netty.util.concurrent.DefaultEventExecutorGroup</code> 。</p></div><h2 id="配置项总览" tabindex="-1"><a class="header-anchor" href="#配置项总览"><span>配置项总览</span></a></h2><div class="language-yaml line-numbers-mode" data-ext="yml" data-title="yml"><pre class="language-yaml"><code><span class="token key atrule">jt808</span><span class="token punctuation">:</span>
  <span class="token key atrule">msg-processor</span><span class="token punctuation">:</span>
    <span class="token key atrule">executor-group</span><span class="token punctuation">:</span>
      <span class="token key atrule">pool-name</span><span class="token punctuation">:</span> 808<span class="token punctuation">-</span>msg<span class="token punctuation">-</span>processor
      <span class="token key atrule">thread-count</span><span class="token punctuation">:</span> <span class="token number">128</span>
      <span class="token key atrule">max-pending-tasks</span><span class="token punctuation">:</span> <span class="token number">128</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="msg-processor-thread-pool" tabindex="-1"><a class="header-anchor" href="#msg-processor-thread-pool"><span><s>msg-processor.thread-pool</s></span></a></h2><div class="hint-container caution"><p class="hint-container-title">提示</p><p><code>msg-processor.thread-pool.xxx</code> 系列配置在 <code>2.0.2</code> 中已经废弃(存在跨线程资源回收问题)。</p><p>使用 <code>jt808.msg-handler</code> 代替。</p></div><h2 id="msg-processor-executor-group" tabindex="-1"><a class="header-anchor" href="#msg-processor-executor-group"><span>msg-processor.executor-group</span></a></h2><h3 id="thread-count" tabindex="-1"><a class="header-anchor" href="#thread-count"><span>thread-count</span></a></h3><ul><li>类型：<code>int</code></li><li>默认值： <ul><li><strong>v2.2.1</strong> 之前: <code>Runtime.getRuntime().availableProcessors() * 2</code></li><li><strong>v2.2.1</strong> 之后: <strong>128</strong></li></ul></li></ul><h3 id="max-pending-tasks" tabindex="-1"><a class="header-anchor" href="#max-pending-tasks"><span>max-pending-tasks</span></a></h3><ul><li>类型：<code>int</code></li><li>默认值：<code>128</code></li></ul><h3 id="pool-name" tabindex="-1"><a class="header-anchor" href="#pool-name"><span>pool-name</span></a></h3><p>线程池的线程名前缀。</p><ul><li>类型：<code>String</code></li><li>默认值：<code>808-msg-processer</code></li><li>默认值： <ul><li><strong>v2.2.1</strong> 之前: <code>808-msg-processer</code></li><li><strong>v2.2.1</strong> 之后: <code>808-msg-processor</code></li></ul></li></ul>`,14),r=[t];function c(p,l){return s(),n("div",null,r)}const d=e(o,[["render",c],["__file","message-processor.html.vue"]]),m=JSON.parse('{"path":"/v2/jt-808/config/message-processor.html","title":"message-processor","lang":"zh-CN","frontmatter":{"icon":"process","description":"message-processor 提示 该章节介绍的是 jt808.msg-processor.executor-group.xxx 消息处理线程池相关的配置。 参见 io.netty.util.concurrent.DefaultEventExecutorGroup 。 配置项总览 提示 msg-processor.thread-pool.xxx ...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v2/jt-808/config/message-processor.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"message-processor"}],["meta",{"property":"og:description","content":"message-processor 提示 该章节介绍的是 jt808.msg-processor.executor-group.xxx 消息处理线程池相关的配置。 参见 io.netty.util.concurrent.DefaultEventExecutorGroup 。 配置项总览 提示 msg-processor.thread-pool.xxx ..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2024-11-29T15:02:48.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2024-11-29T15:02:48.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"message-processor\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2024-11-29T15:02:48.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"配置项总览","slug":"配置项总览","link":"#配置项总览","children":[]},{"level":2,"title":"msg-processor.thread-pool","slug":"msg-processor-thread-pool","link":"#msg-processor-thread-pool","children":[]},{"level":2,"title":"msg-processor.executor-group","slug":"msg-processor-executor-group","link":"#msg-processor-executor-group","children":[{"level":3,"title":"thread-count","slug":"thread-count","link":"#thread-count","children":[]},{"level":3,"title":"max-pending-tasks","slug":"max-pending-tasks","link":"#max-pending-tasks","children":[]},{"level":3,"title":"pool-name","slug":"pool-name","link":"#pool-name","children":[]}]}],"git":{"createdTime":1683899054000,"updatedTime":1732892568000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":2}]},"readingTime":{"minutes":0.53,"words":158},"filePathRelative":"v2/jt-808/config/message-processor.md","localizedDate":"2023年5月12日","autoDesc":true,"excerpt":"\\n<div class=\\"hint-container info\\">\\n<p class=\\"hint-container-title\\">提示</p>\\n<p>该章节介绍的是 <code>jt808.msg-processor.executor-group.xxx</code> 消息处理线程池相关的配置。</p>\\n<p>参见 <code>io.netty.util.concurrent.DefaultEventExecutorGroup</code> 。</p>\\n</div>\\n<h2>配置项总览</h2>\\n<div class=\\"language-yaml\\" data-ext=\\"yml\\" data-title=\\"yml\\"><pre class=\\"language-yaml\\"><code><span class=\\"token key atrule\\">jt808</span><span class=\\"token punctuation\\">:</span>\\n  <span class=\\"token key atrule\\">msg-processor</span><span class=\\"token punctuation\\">:</span>\\n    <span class=\\"token key atrule\\">executor-group</span><span class=\\"token punctuation\\">:</span>\\n      <span class=\\"token key atrule\\">pool-name</span><span class=\\"token punctuation\\">:</span> 808<span class=\\"token punctuation\\">-</span>msg<span class=\\"token punctuation\\">-</span>processor\\n      <span class=\\"token key atrule\\">thread-count</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">128</span>\\n      <span class=\\"token key atrule\\">max-pending-tasks</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">128</span>\\n</code></pre></div>"}');export{d as comp,m as data};