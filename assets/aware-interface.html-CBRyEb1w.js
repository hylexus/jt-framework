import{_ as r}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as c,o as i,c as d,b as a,d as e,e as t,w as o,a as n}from"./app-D_dj-vKV.js";const l={},p=n('<h1 id="aware接口" tabindex="-1"><a class="header-anchor" href="#aware接口"><span>Aware接口</span></a></h1><p>首先声明这种 <code>Aware</code> 接口是从 <code>Spring</code> 中借鉴(抄袭)的 😂😂😂😂😂😂。</p><p>所以你要是知道 <code>Spring</code> 中的诸如 <code>ApplicationContextAware</code>、<code>EnvironmentAware</code> 等就几乎没必要看本小节了。</p><h2 id="为什么会提供" tabindex="-1"><a class="header-anchor" href="#为什么会提供"><span>为什么会提供？</span></a></h2><p>在用注解解析映射请求报文到实体类的时候，字段长度等的计算可能会依赖于消息头中的一些属性。</p><p>所以在基于注解的实体类映射时，你可以给实体类实现这些接口以自动注入一些其他信息。</p>',6),u={class:"hint-container caution"},m=a("p",{class:"hint-container-title"},"注意",-1),h=a("code",null,"请求体消息实体类",-1),v=n(`<h2 id="requestmsgheaderaware" tabindex="-1"><a class="header-anchor" href="#requestmsgheaderaware"><span>RequestMsgHeaderAware</span></a></h2><p>每次消息处理时为实体类注入 <code>RequestMsgHeader</code> 实例。</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token keyword">public</span> <span class="token keyword">interface</span> <span class="token class-name">RequestMsgHeaderAware</span> <span class="token punctuation">{</span>

    <span class="token keyword">void</span> <span class="token function">setRequestMsgHeader</span><span class="token punctuation">(</span><span class="token class-name">RequestMsgHeader</span> header<span class="token punctuation">)</span><span class="token punctuation">;</span>

<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="requestmsgmetadataaware" tabindex="-1"><a class="header-anchor" href="#requestmsgmetadataaware"><span>RequestMsgMetadataAware</span></a></h2><p>每次消息处理时为实体类注入 <code>RequestMsgMetadata</code> 实例。</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token keyword">public</span> <span class="token keyword">interface</span> <span class="token class-name">RequestMsgMetadataAware</span> <span class="token punctuation">{</span>

    <span class="token keyword">void</span> <span class="token function">setRequestMsgMetadata</span><span class="token punctuation">(</span><span class="token class-name">RequestMsgMetadata</span> metadata<span class="token punctuation">)</span><span class="token punctuation">;</span>

<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="bytesencoderaware" tabindex="-1"><a class="header-anchor" href="#bytesencoderaware"><span>BytesEncoderAware</span></a></h2>`,7),g={class:"hint-container tip"},w=a("p",{class:"hint-container-title"},"提示",-1),k=a("code",null,"BytesEncoder",-1),y=n(`<ul><li><code>MsgHandler</code> 中可以实现该接口以注入 <code>BytesEncoder</code> 给处理器实例。</li><li>当然如果是自定义的 <code>MsgHandler</code>（接口实现类），完全可以使用 <code>Spring</code> 的依赖注入的方式来使用 <code>BytesEncoder</code> 实例。</li><li>内置的 <code>MsgHandler</code> 都实现了该接口。</li></ul><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token keyword">public</span> <span class="token keyword">interface</span> <span class="token class-name">BytesEncoderAware</span> <span class="token punctuation">{</span>

    <span class="token keyword">void</span> <span class="token function">setBytesEncoder</span><span class="token punctuation">(</span><span class="token class-name">BytesEncoder</span> bytesEncoder<span class="token punctuation">)</span><span class="token punctuation">;</span>

<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,2);function b(_,f){const s=c("RouteLink");return i(),d("div",null,[p,a("div",u,[m,a("p",null,[e("目前为止，这些Aware接口只能在 "),t(s,{to:"/v1/jt-808/guide/more/src/v1/jt-808/guide/annotation-based-dev/req-msg-mapping.html"},{default:o(()=>[e("基于注解处理请求消息映射")]),_:1}),e(" 时用于 "),h,e(" 中。")])]),v,a("div",g,[w,a("p",null,[e("有关 "),k,e(" 的内容，"),t(s,{to:"/v1/jt-808/guide/more/src/v1/jt-808/guide/customization/escape-config.html"},{default:o(()=>[e("请移步此处")]),_:1}),e("。")])]),y])}const j=r(l,[["render",b],["__file","aware-interface.html.vue"]]),M=JSON.parse('{"path":"/v1/jt-808/guide/more/aware-interface.html","title":"Aware接口","lang":"zh-CN","frontmatter":{"description":"Aware接口 首先声明这种 Aware 接口是从 Spring 中借鉴(抄袭)的 😂😂😂😂😂😂。 所以你要是知道 Spring 中的诸如 ApplicationContextAware、EnvironmentAware 等就几乎没必要看本小节了。 为什么会提供？ 在用注解解析映射请求报文到实体类的时候，字段长度等的计算可能会依赖于消息头中...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v1/jt-808/guide/more/aware-interface.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"Aware接口"}],["meta",{"property":"og:description","content":"Aware接口 首先声明这种 Aware 接口是从 Spring 中借鉴(抄袭)的 😂😂😂😂😂😂。 所以你要是知道 Spring 中的诸如 ApplicationContextAware、EnvironmentAware 等就几乎没必要看本小节了。 为什么会提供？ 在用注解解析映射请求报文到实体类的时候，字段长度等的计算可能会依赖于消息头中..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2022-12-23T16:14:45.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2022-12-23T16:14:45.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"Aware接口\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2022-12-23T16:14:45.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"为什么会提供？","slug":"为什么会提供","link":"#为什么会提供","children":[]},{"level":2,"title":"RequestMsgHeaderAware","slug":"requestmsgheaderaware","link":"#requestmsgheaderaware","children":[]},{"level":2,"title":"RequestMsgMetadataAware","slug":"requestmsgmetadataaware","link":"#requestmsgmetadataaware","children":[]},{"level":2,"title":"BytesEncoderAware","slug":"bytesencoderaware","link":"#bytesencoderaware","children":[]}],"git":{"createdTime":1671812085000,"updatedTime":1671812085000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":1.09,"words":326},"filePathRelative":"v1/jt-808/guide/more/aware-interface.md","localizedDate":"2022年12月24日","autoDesc":true,"excerpt":"\\n<p>首先声明这种 <code>Aware</code> 接口是从 <code>Spring</code> 中借鉴(抄袭)的 😂😂😂😂😂😂。</p>\\n<p>所以你要是知道 <code>Spring</code> 中的诸如 <code>ApplicationContextAware</code>、<code>EnvironmentAware</code> 等就几乎没必要看本小节了。</p>\\n<h2>为什么会提供？</h2>\\n<p>在用注解解析映射请求报文到实体类的时候，字段长度等的计算可能会依赖于消息头中的一些属性。</p>\\n<p>所以在基于注解的实体类映射时，你可以给实体类实现这些接口以自动注入一些其他信息。</p>"}');export{j as comp,M as data};
