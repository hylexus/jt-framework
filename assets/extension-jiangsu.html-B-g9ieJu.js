import{_ as n}from"./plugin-vue_export-helper-DlAUqK2U.js";import{o as s,c as a,a as e}from"./app-Djo5EUbb.js";const t={},o=e(`<h1 id="苏标扩展-v2-1-4" tabindex="-1"><a class="header-anchor" href="#苏标扩展-v2-1-4"><span>苏标扩展(v2.1.4)</span></a></h1><div class="language-yaml line-numbers-mode" data-ext="yml" data-title="yml"><pre class="language-yaml"><code><span class="token key atrule">jt808</span><span class="token punctuation">:</span>
  <span class="token comment">### ...(省略其他配置)</span>
  <span class="token comment">## 苏标附件服务器</span>
  <span class="token key atrule">attachment-server</span><span class="token punctuation">:</span>
    <span class="token comment">## 是否启用(默认: false)</span>
    <span class="token key atrule">enabled</span><span class="token punctuation">:</span> <span class="token boolean important">true</span>
    <span class="token comment"># 附件服务器端口(TCP)</span>
    <span class="token key atrule">port</span><span class="token punctuation">:</span> <span class="token number">6809</span>
    <span class="token comment"># this.bossGroup=new NioEventLoopGroup(bossThreadCount);</span>
    <span class="token key atrule">boss-thread-count</span><span class="token punctuation">:</span> <span class="token number">2</span>
    <span class="token comment"># this.workerGroup=new NioEventLoopGroup(workThreadCount);</span>
    <span class="token key atrule">worker-thread-count</span><span class="token punctuation">:</span> <span class="token number">4</span>
    <span class="token comment">## 0x30316364附件上传报文的最大长度(66560 = 1024 * 65)</span>
    <span class="token key atrule">max-frame-length</span><span class="token punctuation">:</span> <span class="token number">66560</span>
    <span class="token comment"># 处理附件相关指令(0x1210,0x1211,0x1212,0x6364)的线程池线配置</span>
    <span class="token key atrule">msg-processor</span><span class="token punctuation">:</span>
      <span class="token comment">## 业务线程池配置</span>
      <span class="token key atrule">executor-group</span><span class="token punctuation">:</span>
        <span class="token key atrule">max-pending-tasks</span><span class="token punctuation">:</span> <span class="token number">64</span>
        <span class="token key atrule">pool-name</span><span class="token punctuation">:</span> 808<span class="token punctuation">-</span>attachment<span class="token punctuation">-</span>processor
        <span class="token key atrule">thread-count</span><span class="token punctuation">:</span> <span class="token number">16</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,2),p=[o];function c(l,r){return s(),a("div",null,p)}const m=n(t,[["render",c],["__file","extension-jiangsu.html.vue"]]),k=JSON.parse('{"path":"/v2/jt-808/config/extension-jiangsu.html","title":"苏标扩展(v2.1.4)","lang":"zh-CN","frontmatter":{"icon":"proposal","description":"苏标扩展(v2.1.4)","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v2/jt-808/config/extension-jiangsu.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"苏标扩展(v2.1.4)"}],["meta",{"property":"og:description","content":"苏标扩展(v2.1.4)"}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2024-03-26T14:59:57.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2024-03-26T14:59:57.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"苏标扩展(v2.1.4)\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2024-03-26T14:59:57.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[],"git":{"createdTime":1711465197000,"updatedTime":1711465197000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":0.4,"words":121},"filePathRelative":"v2/jt-808/config/extension-jiangsu.md","localizedDate":"2024年3月26日","autoDesc":true,"excerpt":"\\n<div class=\\"language-yaml\\" data-ext=\\"yml\\" data-title=\\"yml\\"><pre class=\\"language-yaml\\"><code><span class=\\"token key atrule\\">jt808</span><span class=\\"token punctuation\\">:</span>\\n  <span class=\\"token comment\\">### ...(省略其他配置)</span>\\n  <span class=\\"token comment\\">## 苏标附件服务器</span>\\n  <span class=\\"token key atrule\\">attachment-server</span><span class=\\"token punctuation\\">:</span>\\n    <span class=\\"token comment\\">## 是否启用(默认: false)</span>\\n    <span class=\\"token key atrule\\">enabled</span><span class=\\"token punctuation\\">:</span> <span class=\\"token boolean important\\">true</span>\\n    <span class=\\"token comment\\"># 附件服务器端口(TCP)</span>\\n    <span class=\\"token key atrule\\">port</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">6809</span>\\n    <span class=\\"token comment\\"># this.bossGroup=new NioEventLoopGroup(bossThreadCount);</span>\\n    <span class=\\"token key atrule\\">boss-thread-count</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">2</span>\\n    <span class=\\"token comment\\"># this.workerGroup=new NioEventLoopGroup(workThreadCount);</span>\\n    <span class=\\"token key atrule\\">worker-thread-count</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">4</span>\\n    <span class=\\"token comment\\">## 0x30316364附件上传报文的最大长度(66560 = 1024 * 65)</span>\\n    <span class=\\"token key atrule\\">max-frame-length</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">66560</span>\\n    <span class=\\"token comment\\"># 处理附件相关指令(0x1210,0x1211,0x1212,0x6364)的线程池线配置</span>\\n    <span class=\\"token key atrule\\">msg-processor</span><span class=\\"token punctuation\\">:</span>\\n      <span class=\\"token comment\\">## 业务线程池配置</span>\\n      <span class=\\"token key atrule\\">executor-group</span><span class=\\"token punctuation\\">:</span>\\n        <span class=\\"token key atrule\\">max-pending-tasks</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">64</span>\\n        <span class=\\"token key atrule\\">pool-name</span><span class=\\"token punctuation\\">:</span> 808<span class=\\"token punctuation\\">-</span>attachment<span class=\\"token punctuation\\">-</span>processor\\n        <span class=\\"token key atrule\\">thread-count</span><span class=\\"token punctuation\\">:</span> <span class=\\"token number\\">16</span>\\n</code></pre></div>"}');export{m as comp,k as data};