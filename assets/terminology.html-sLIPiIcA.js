import{_ as n}from"./plugin-vue_export-helper-DlAUqK2U.js";import{o as s,c as a,a as e}from"./app-Djo5EUbb.js";const t={},c=e(`<h1 id="基本术语" tabindex="-1"><a class="header-anchor" href="#基本术语"><span>基本术语</span></a></h1><p>在 <code>2.x</code> 中，所有的核心 <code>API</code> 都重写了 (<code>fluent</code> 风格)。几个关键接口如下：</p><h2 id="jt808request" tabindex="-1"><a class="header-anchor" href="#jt808request"><span>Jt808Request</span></a></h2><p>客户端请求消息中的字节流最终会解析到 <code>Jt808Request</code> 里。<code>Jt808Request</code> 接口内容如下：</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token keyword">public</span> <span class="token keyword">interface</span> <span class="token class-name">Jt808Request</span> <span class="token punctuation">{</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@return</span> 消息ID
     */</span>
    <span class="token class-name">MsgType</span> <span class="token function">msgType</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@return</span> 请求头
     */</span>
    <span class="token class-name">Jt808RequestHeader</span> <span class="token function">header</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@return</span> 原始报文(转义之后)
     */</span>
    <span class="token class-name">ByteBuf</span> <span class="token function">rawByteBuf</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@return</span> 消息体(转义之后)
     */</span>
    <span class="token class-name">ByteBuf</span> <span class="token function">body</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@return</span> 校验码(原始报文)
     */</span>
    <span class="token keyword">byte</span> <span class="token function">originalCheckSum</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@return</span> 校验码(计算后)
     * <span class="token keyword">@see</span> <span class="token reference"><span class="token class-name">Jt808MsgBytesProcessor</span><span class="token punctuation">#</span><span class="token function">calculateCheckSum</span><span class="token punctuation">(</span><span class="token class-name">ByteBuf</span><span class="token punctuation">)</span></span>
     */</span>
    <span class="token keyword">byte</span> <span class="token function">calculatedCheckSum</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token comment">// ...</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="jt808response" tabindex="-1"><a class="header-anchor" href="#jt808response"><span>Jt808Response</span></a></h2><p>与 <code>Jt808Request</code> 相对应，处理完消息之后回复给客户端的数据对应着 <code>Jt808Response</code> 接口：</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token keyword">public</span> <span class="token keyword">interface</span> <span class="token class-name">Jt808Response</span> <span class="token keyword">extends</span> <span class="token class-name">Jt808ByteWriter</span> <span class="token punctuation">{</span>

    <span class="token keyword">int</span> <span class="token constant">DEFAULT_MAX_PACKAGE_SIZE</span> <span class="token operator">=</span> <span class="token number">1024</span><span class="token punctuation">;</span>

    <span class="token comment">// ...</span>

    <span class="token doc-comment comment">/**
     * byte[0,2) -- <span class="token punctuation">{</span><span class="token keyword">@link</span>  <span class="token reference"><span class="token class-name">MsgDataType</span><span class="token punctuation">#</span><span class="token field">WORD</span></span> WORD<span class="token punctuation">}</span> -- 消息ID
     */</span>
    <span class="token keyword">int</span> <span class="token function">msgType</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token class-name">Jt808ProtocolVersion</span> <span class="token function">version</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token punctuation">{</span><span class="token keyword">@link</span> <span class="token reference"><span class="token class-name">Jt808ProtocolVersion</span><span class="token punctuation">#</span><span class="token field">VERSION_2011</span></span> VERSION_2011<span class="token punctuation">}</span> -- byte[4,10) -- <span class="token punctuation">{</span><span class="token keyword">@link</span>  <span class="token reference"><span class="token class-name">MsgDataType</span><span class="token punctuation">#</span><span class="token field">BCD</span></span> BCD[6]<span class="token punctuation">}</span> -- 终端手机号
     * <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>
     * <span class="token punctuation">{</span><span class="token keyword">@link</span> <span class="token reference"><span class="token class-name">Jt808ProtocolVersion</span><span class="token punctuation">#</span><span class="token field">VERSION_2019</span></span> VERSION_2019<span class="token punctuation">}</span> -- byte[5,15) -- <span class="token punctuation">{</span><span class="token keyword">@link</span>  <span class="token reference"><span class="token class-name">MsgDataType</span><span class="token punctuation">#</span><span class="token field">BCD</span></span> BCD[10]<span class="token punctuation">}</span> -- 终端手机号
     */</span>
    <span class="token class-name">String</span> <span class="token function">terminalId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token punctuation">{</span><span class="token keyword">@link</span> <span class="token reference"><span class="token class-name">Jt808ProtocolVersion</span><span class="token punctuation">#</span><span class="token field">VERSION_2011</span></span> VERSION_2011<span class="token punctuation">}</span> -- byte[10,11) -- <span class="token punctuation">{</span><span class="token keyword">@link</span>  <span class="token reference"><span class="token class-name">MsgDataType</span><span class="token punctuation">#</span><span class="token field">WORD</span></span> WORD<span class="token punctuation">}</span> -- 流水号
     * <span class="token tag"><span class="token tag"><span class="token punctuation">&lt;</span>p</span><span class="token punctuation">&gt;</span></span>
     * <span class="token punctuation">{</span><span class="token keyword">@link</span> <span class="token reference"><span class="token class-name">Jt808ProtocolVersion</span><span class="token punctuation">#</span><span class="token field">VERSION_2019</span></span> VERSION_2019<span class="token punctuation">}</span> -- byte[15,16) -- <span class="token punctuation">{</span><span class="token keyword">@link</span>  <span class="token reference"><span class="token class-name">MsgDataType</span><span class="token punctuation">#</span><span class="token field">WORD</span></span> WORD<span class="token punctuation">}</span> -- 流水号
     */</span>
    <span class="token keyword">int</span> <span class="token function">flowId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token class-name">ByteBuf</span> <span class="token function">body</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token comment">// ...</span>
<span class="token punctuation">}</span>

</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="jt808session" tabindex="-1"><a class="header-anchor" href="#jt808session"><span>Jt808Session</span></a></h2><p>和客户端对应的连接都用一个叫做 <code>Jt808Session</code> 类来表示：</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token keyword">public</span> <span class="token keyword">interface</span> <span class="token class-name">Jt808Session</span> <span class="token keyword">extends</span> <span class="token class-name">Jt808FlowIdGenerator</span> <span class="token punctuation">{</span>

    <span class="token comment">// ...</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@param</span> <span class="token parameter">byteBuf</span> 待发送给客户端的数据
     */</span>
    <span class="token keyword">void</span> <span class="token function">sendMsgToClient</span><span class="token punctuation">(</span><span class="token class-name">ByteBuf</span> byteBuf<span class="token punctuation">)</span> <span class="token keyword">throws</span> <span class="token class-name">JtCommunicationException</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@return</span> 当前流水号，并自增
     * <span class="token keyword">@see</span> <span class="token reference"><span class="token punctuation">#</span><span class="token function">currentFlowId</span><span class="token punctuation">(</span><span class="token keyword">boolean</span><span class="token punctuation">)</span></span>
     */</span>
    <span class="token keyword">int</span> <span class="token function">currentFlowId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token keyword">default</span> <span class="token class-name">String</span> <span class="token function">sessionId</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">return</span> <span class="token function">id</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token class-name">Channel</span> <span class="token function">channel</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token class-name">Jt808Session</span> <span class="token function">channel</span><span class="token punctuation">(</span><span class="token class-name">Channel</span> channel<span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token class-name">String</span> <span class="token function">terminalId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@return</span> 当前终端的协议版本号
     */</span>
    <span class="token class-name">Jt808ProtocolVersion</span> <span class="token function">protocolVersion</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * <span class="token keyword">@return</span> 上次通信时间
     */</span>
    <span class="token keyword">long</span> <span class="token function">lastCommunicateTimestamp</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token class-name">Jt808Session</span> <span class="token function">lastCommunicateTimestamp</span><span class="token punctuation">(</span><span class="token keyword">long</span> lastCommunicateTimestamp<span class="token punctuation">)</span><span class="token punctuation">;</span>

<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="jt808serverexchange" tabindex="-1"><a class="header-anchor" href="#jt808serverexchange"><span>Jt808ServerExchange</span></a></h2><p>在实际处理消息的过程中，将 <code>Jt808Request</code>、<code>Jt808Response</code> 和 <code>Jt808Session</code> 都封装在了一个叫 <code>Jt808ServerExchange</code> 的对象里。</p><p>和 <code>Spring</code> 的 <code>WebFlux</code> 中的 <code>org.springframework.web.server.ServerWebExchange</code> 有类似的作用。</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token keyword">public</span> <span class="token keyword">interface</span> <span class="token class-name">Jt808ServerExchange</span> <span class="token punctuation">{</span>

    <span class="token class-name">Jt808Request</span> <span class="token function">request</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token class-name">Jt808Response</span> <span class="token function">response</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>

    <span class="token class-name">Jt808Session</span> <span class="token function">session</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,15),p=[c];function o(l,i){return s(),a("div",null,p)}const r=n(t,[["render",o],["__file","terminology.html.vue"]]),k=JSON.parse('{"path":"/v2/jt-808/guide/quick-start/terminology.html","title":"基本术语","lang":"zh-CN","frontmatter":{"icon":"proposal","description":"基本术语 在 2.x 中，所有的核心 API 都重写了 (fluent 风格)。几个关键接口如下： Jt808Request 客户端请求消息中的字节流最终会解析到 Jt808Request 里。Jt808Request 接口内容如下： Jt808Response 与 Jt808Request 相对应，处理完消息之后回复给客户端的数据对应着 Jt808R...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v2/jt-808/guide/quick-start/terminology.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"基本术语"}],["meta",{"property":"og:description","content":"基本术语 在 2.x 中，所有的核心 API 都重写了 (fluent 风格)。几个关键接口如下： Jt808Request 客户端请求消息中的字节流最终会解析到 Jt808Request 里。Jt808Request 接口内容如下： Jt808Response 与 Jt808Request 相对应，处理完消息之后回复给客户端的数据对应着 Jt808R..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2022-12-23T16:14:45.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2022-12-23T16:14:45.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"基本术语\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2022-12-23T16:14:45.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"Jt808Request","slug":"jt808request","link":"#jt808request","children":[]},{"level":2,"title":"Jt808Response","slug":"jt808response","link":"#jt808response","children":[]},{"level":2,"title":"Jt808Session","slug":"jt808session","link":"#jt808session","children":[]},{"level":2,"title":"Jt808ServerExchange","slug":"jt808serverexchange","link":"#jt808serverexchange","children":[]}],"git":{"createdTime":1671812085000,"updatedTime":1671812085000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":1.41,"words":424},"filePathRelative":"v2/jt-808/guide/quick-start/terminology.md","localizedDate":"2022年12月24日","autoDesc":true,"excerpt":"\\n<p>在 <code>2.x</code> 中，所有的核心 <code>API</code> 都重写了 (<code>fluent</code> 风格)。几个关键接口如下：</p>\\n<h2>Jt808Request</h2>\\n<p>客户端请求消息中的字节流最终会解析到 <code>Jt808Request</code> 里。<code>Jt808Request</code> 接口内容如下：</p>\\n<div class=\\"language-java\\" data-ext=\\"java\\" data-title=\\"java\\"><pre class=\\"language-java\\"><code><span class=\\"token keyword\\">public</span> <span class=\\"token keyword\\">interface</span> <span class=\\"token class-name\\">Jt808Request</span> <span class=\\"token punctuation\\">{</span>\\n\\n    <span class=\\"token doc-comment comment\\">/**\\n     * <span class=\\"token keyword\\">@return</span> 消息ID\\n     */</span>\\n    <span class=\\"token class-name\\">MsgType</span> <span class=\\"token function\\">msgType</span><span class=\\"token punctuation\\">(</span><span class=\\"token punctuation\\">)</span><span class=\\"token punctuation\\">;</span>\\n\\n    <span class=\\"token doc-comment comment\\">/**\\n     * <span class=\\"token keyword\\">@return</span> 请求头\\n     */</span>\\n    <span class=\\"token class-name\\">Jt808RequestHeader</span> <span class=\\"token function\\">header</span><span class=\\"token punctuation\\">(</span><span class=\\"token punctuation\\">)</span><span class=\\"token punctuation\\">;</span>\\n\\n    <span class=\\"token doc-comment comment\\">/**\\n     * <span class=\\"token keyword\\">@return</span> 原始报文(转义之后)\\n     */</span>\\n    <span class=\\"token class-name\\">ByteBuf</span> <span class=\\"token function\\">rawByteBuf</span><span class=\\"token punctuation\\">(</span><span class=\\"token punctuation\\">)</span><span class=\\"token punctuation\\">;</span>\\n\\n    <span class=\\"token doc-comment comment\\">/**\\n     * <span class=\\"token keyword\\">@return</span> 消息体(转义之后)\\n     */</span>\\n    <span class=\\"token class-name\\">ByteBuf</span> <span class=\\"token function\\">body</span><span class=\\"token punctuation\\">(</span><span class=\\"token punctuation\\">)</span><span class=\\"token punctuation\\">;</span>\\n\\n    <span class=\\"token doc-comment comment\\">/**\\n     * <span class=\\"token keyword\\">@return</span> 校验码(原始报文)\\n     */</span>\\n    <span class=\\"token keyword\\">byte</span> <span class=\\"token function\\">originalCheckSum</span><span class=\\"token punctuation\\">(</span><span class=\\"token punctuation\\">)</span><span class=\\"token punctuation\\">;</span>\\n\\n    <span class=\\"token doc-comment comment\\">/**\\n     * <span class=\\"token keyword\\">@return</span> 校验码(计算后)\\n     * <span class=\\"token keyword\\">@see</span> <span class=\\"token reference\\"><span class=\\"token class-name\\">Jt808MsgBytesProcessor</span><span class=\\"token punctuation\\">#</span><span class=\\"token function\\">calculateCheckSum</span><span class=\\"token punctuation\\">(</span><span class=\\"token class-name\\">ByteBuf</span><span class=\\"token punctuation\\">)</span></span>\\n     */</span>\\n    <span class=\\"token keyword\\">byte</span> <span class=\\"token function\\">calculatedCheckSum</span><span class=\\"token punctuation\\">(</span><span class=\\"token punctuation\\">)</span><span class=\\"token punctuation\\">;</span>\\n\\n    <span class=\\"token comment\\">// ...</span>\\n<span class=\\"token punctuation\\">}</span>\\n</code></pre></div>"}');export{r as comp,k as data};