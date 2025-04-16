import{_ as e}from"./plugin-vue_export-helper-DlAUqK2U.js";import{o as t,c as p,b as n,a as s}from"./app-D_dj-vKV.js";const o={},c=s('<h1 id="响应消息处理器" tabindex="-1"><a class="header-anchor" href="#响应消息处理器"><span>响应消息处理器</span></a></h1><h2 id="说明" tabindex="-1"><a class="header-anchor" href="#说明"><span>说明</span></a></h2><div class="hint-container tip"><p class="hint-container-title">提示</p><p>对响应给客户端的消息类型没有强制指定固定类型，任何类型的消息处理器返回的结果都被包装成了 <code>Jt808HandlerResult</code>。</p><p>而 <code>Jt808HandlerResult</code> 是由 <code>Jt808HandlerResultHandler</code> 处理的。</p><p>所以，支持哪些类型的响应消息取决于 <code>Jt808HandlerResultHandler</code> 的配置。</p></div><p>内置了两个 <code>Jt808HandlerResultHandler</code>:</p>',4),l={class:"demo"},i=["src"],u=s(`<ul><li><code>Jt808ResponseHandlerResultHandler</code> 能处理 <code>Jt808Response</code> 类型的响应数据。</li><li><code>Jt808ResponseBodyHandlerResultHandler</code> 能将处理被 <code>@Jt808ResponseBody</code> 注解修饰的返回类型。</li></ul><div class="hint-container tip"><p class="hint-container-title">提示</p><p>本小节主要介绍 <code>Jt808ResponseHandlerResultHandler</code> 类型的 <strong>类级别</strong> 处理器，毕竟当注解不方便处理请求时，这种类型的 <strong>HandlerResultHandler</strong> 是内置的 *<br><em>HandlerResultHandler</em>* 中唯一的选择。</p></div><h2 id="jt808responsebody" tabindex="-1"><a class="header-anchor" href="#jt808responsebody"><span>@Jt808ResponseBody</span></a></h2><h3 id="说明-1" tabindex="-1"><a class="header-anchor" href="#说明-1"><span>说明</span></a></h3><p>这个注解也是借鉴(抄袭)<code>Spring</code> 的 <code>@ResponseBody</code> 注解，表示被标记的类是响应体。</p><h3 id="示例" tabindex="-1"><a class="header-anchor" href="#示例"><span>示例</span></a></h3><p>下面是被 <code>@Jt808ResponseBody</code> 标记的类，表示该类是给客户端回复数据的 <code>body()</code> 部分：</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token annotation punctuation">@Data</span>
<span class="token annotation punctuation">@Accessors</span><span class="token punctuation">(</span>chain <span class="token operator">=</span> <span class="token boolean">true</span><span class="token punctuation">)</span>
<span class="token annotation punctuation">@Jt808ResponseBody</span><span class="token punctuation">(</span>msgId <span class="token operator">=</span> <span class="token number">0x8100</span><span class="token punctuation">,</span> maxPackageSize <span class="token operator">=</span> <span class="token number">33</span><span class="token punctuation">)</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">TerminalRegisterReplyMsg</span> <span class="token punctuation">{</span>
    <span class="token comment">// 1. byte[0,2) WORD 对应的终端注册消息的流水号</span>
    <span class="token annotation punctuation">@ResponseField</span><span class="token punctuation">(</span>order <span class="token operator">=</span> <span class="token number">0</span><span class="token punctuation">,</span> dataType <span class="token operator">=</span> <span class="token class-name">MsgDataType</span><span class="token punctuation">.</span><span class="token constant">WORD</span><span class="token punctuation">)</span>
    <span class="token keyword">private</span> <span class="token keyword">int</span> flowId<span class="token punctuation">;</span>
    <span class="token comment">// 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端</span>
    <span class="token annotation punctuation">@ResponseField</span><span class="token punctuation">(</span>order <span class="token operator">=</span> <span class="token number">1</span><span class="token punctuation">,</span> dataType <span class="token operator">=</span> <span class="token class-name">MsgDataType</span><span class="token punctuation">.</span><span class="token constant">BYTE</span><span class="token punctuation">)</span>
    <span class="token keyword">private</span> <span class="token keyword">byte</span> result<span class="token punctuation">;</span>
    <span class="token comment">// 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)</span>
    <span class="token annotation punctuation">@ResponseField</span><span class="token punctuation">(</span>order <span class="token operator">=</span> <span class="token number">3</span><span class="token punctuation">,</span> dataType <span class="token operator">=</span> <span class="token class-name">MsgDataType</span><span class="token punctuation">.</span><span class="token constant">STRING</span><span class="token punctuation">,</span> conditionalOn <span class="token operator">=</span> <span class="token string">&quot;result == 0&quot;</span><span class="token punctuation">)</span>
    <span class="token keyword">private</span> <span class="token class-name">String</span> authCode<span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre><div class="highlight-lines"><br><br><div class="highlight-line"> </div><br><br><br><br><br><br><br><br><br><br><br></div><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><p>下面是回复客户端的部分伪代码：</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token annotation punctuation">@Component</span>
<span class="token annotation punctuation">@Jt808RequestHandler</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">CommonHandler</span> <span class="token punctuation">{</span>
    <span class="token annotation punctuation">@Jt808RequestHandlerMapping</span><span class="token punctuation">(</span>msgType <span class="token operator">=</span> <span class="token number">0x0100</span><span class="token punctuation">,</span> versions <span class="token operator">=</span> <span class="token constant">VERSION_2019</span><span class="token punctuation">)</span>
    <span class="token keyword">public</span> <span class="token class-name">TerminalRegisterReplyMsg</span> <span class="token function">processTerminalRegisterMsgV2019</span><span class="token punctuation">(</span><span class="token class-name">Jt808RequestEntity</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token class-name">TerminalRegisterMsgV2019</span><span class="token punctuation">&gt;</span></span> request<span class="token punctuation">)</span> <span class="token punctuation">{</span>

        log<span class="token punctuation">.</span><span class="token function">info</span><span class="token punctuation">(</span><span class="token string">&quot;V2019--TerminalRegister : {}&quot;</span><span class="token punctuation">,</span> request<span class="token punctuation">)</span><span class="token punctuation">;</span>
        <span class="token keyword">return</span> <span class="token keyword">new</span> <span class="token class-name">TerminalRegisterReplyMsg</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
                <span class="token punctuation">.</span><span class="token function">setFlowId</span><span class="token punctuation">(</span>request<span class="token punctuation">.</span><span class="token function">flowId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                <span class="token punctuation">.</span><span class="token function">setResult</span><span class="token punctuation">(</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">)</span> <span class="token number">0</span><span class="token punctuation">)</span>
                <span class="token punctuation">.</span><span class="token function">setAuthCode</span><span class="token punctuation">(</span><span class="token string">&quot;authCode2019-admin&quot;</span><span class="token punctuation">)</span>
                <span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

<span class="token punctuation">}</span>
</code></pre><div class="highlight-lines"><br><br><br><br><div class="highlight-line"> </div><br><br><br><br><br><br><br><br><br><br></div><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="jt808response" tabindex="-1"><a class="header-anchor" href="#jt808response"><span>Jt808Response</span></a></h2><h3 id="说明-2" tabindex="-1"><a class="header-anchor" href="#说明-2"><span>说明</span></a></h3><h3 id="示例-1" tabindex="-1"><a class="header-anchor" href="#示例-1"><span>示例</span></a></h3><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token annotation punctuation">@Slf4j</span>
<span class="token annotation punctuation">@Component</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">TerminalRegisterMsgHandlerV2013</span> <span class="token keyword">implements</span> <span class="token class-name">SimpleJt808RequestHandler</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token class-name">Jt808Response</span><span class="token punctuation">&gt;</span></span> <span class="token punctuation">{</span>

    <span class="token doc-comment comment">/**
     * 处理 [终端注册] 消息
     */</span>
    <span class="token annotation punctuation">@Override</span>
    <span class="token keyword">public</span> <span class="token class-name">Set</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token class-name">MsgType</span><span class="token punctuation">&gt;</span></span> <span class="token function">getSupportedMsgTypes</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">return</span> <span class="token class-name">Set</span><span class="token punctuation">.</span><span class="token function">of</span><span class="token punctuation">(</span><span class="token class-name">BuiltinJt808MsgType</span><span class="token punctuation">.</span><span class="token constant">CLIENT_REGISTER</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token doc-comment comment">/**
     * 处理 [V2013] 版的消息
     */</span>
    <span class="token annotation punctuation">@Override</span>
    <span class="token keyword">public</span> <span class="token class-name">Set</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token class-name">Jt808ProtocolVersion</span><span class="token punctuation">&gt;</span></span> <span class="token function">getSupportedVersions</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">return</span> <span class="token class-name">Jt808ProtocolVersion</span><span class="token punctuation">.</span><span class="token function">unmodifiableSetVersion2013</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

    <span class="token comment">// 7E0100002F013912344323007B000B0002696431323374797065313233343536373838373635343332314944313233343501B8CA4A2D3132333435332D7E</span>
    <span class="token annotation punctuation">@Override</span>
    <span class="token keyword">public</span> <span class="token class-name">Jt808Response</span> <span class="token function">handleMsg</span><span class="token punctuation">(</span><span class="token class-name">Jt808ServerExchange</span> exchange<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token comment">// ...忽略请求读取过程</span>

        <span class="token comment">// 手动创建一个新的Jt808Response返回</span>
        <span class="token keyword">return</span> <span class="token class-name">Jt808Response</span><span class="token punctuation">.</span><span class="token function">newBuilder</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
                <span class="token punctuation">.</span><span class="token function">msgId</span><span class="token punctuation">(</span><span class="token class-name">BuiltinJt808MsgType</span><span class="token punctuation">.</span><span class="token constant">CLIENT_REGISTER_REPLY</span><span class="token punctuation">)</span>
                <span class="token punctuation">.</span><span class="token function">terminalId</span><span class="token punctuation">(</span>exchange<span class="token punctuation">.</span><span class="token function">request</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">terminalId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                <span class="token punctuation">.</span><span class="token function">flowId</span><span class="token punctuation">(</span>exchange<span class="token punctuation">.</span><span class="token function">session</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">nextFlowId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                <span class="token punctuation">.</span><span class="token function">version</span><span class="token punctuation">(</span>exchange<span class="token punctuation">.</span><span class="token function">request</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">version</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                <span class="token punctuation">.</span><span class="token function">body</span><span class="token punctuation">(</span>writer <span class="token operator">-&gt;</span> writer
                        <span class="token comment">// 1. byte[0,2) WORD 对应的终端注册消息的流水号</span>
                        <span class="token punctuation">.</span><span class="token function">writeWord</span><span class="token punctuation">(</span>exchange<span class="token punctuation">.</span><span class="token function">request</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">.</span><span class="token function">flowId</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">)</span>
                        <span class="token comment">// 2. byte[2,3) BYTE 0:成功;1:车辆已被注册;2:数据库中无该车辆; 3:终端已被注册;4:数据库中无该终端</span>
                        <span class="token punctuation">.</span><span class="token function">writeByte</span><span class="token punctuation">(</span><span class="token number">0</span><span class="token punctuation">)</span>
                        <span class="token comment">// 3. byte[3,x) STRING 鉴权码(只有在成功后才有该字段)</span>
                        <span class="token punctuation">.</span><span class="token function">writeString</span><span class="token punctuation">(</span><span class="token string">&quot;AuthCode-123&quot;</span><span class="token punctuation">)</span>
                <span class="token punctuation">)</span>
                <span class="token punctuation">.</span><span class="token function">build</span><span class="token punctuation">(</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre><div class="highlight-lines"><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><div class="highlight-line"> </div><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br></div><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,14);function r(a,d){return t(),p("div",null,[c,n("p",l,[n("img",{src:a.$withBase("/img/v2/design/jt808-handler-result-handler.png")},null,8,i)]),u])}const v=e(o,[["render",r],["__file","response-msg-handler.html.vue"]]),b=JSON.parse('{"path":"/v2/jt-808/guide/basic/response-msg-handler.html","title":"响应消息处理器","lang":"zh-CN","frontmatter":{"icon":"return","description":"响应消息处理器 说明 提示 对响应给客户端的消息类型没有强制指定固定类型，任何类型的消息处理器返回的结果都被包装成了 Jt808HandlerResult。 而 Jt808HandlerResult 是由 Jt808HandlerResultHandler 处理的。 所以，支持哪些类型的响应消息取决于 Jt808HandlerResultHandler...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v2/jt-808/guide/basic/response-msg-handler.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"响应消息处理器"}],["meta",{"property":"og:description","content":"响应消息处理器 说明 提示 对响应给客户端的消息类型没有强制指定固定类型，任何类型的消息处理器返回的结果都被包装成了 Jt808HandlerResult。 而 Jt808HandlerResult 是由 Jt808HandlerResultHandler 处理的。 所以，支持哪些类型的响应消息取决于 Jt808HandlerResultHandler..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2023-05-02T14:49:50.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2023-05-02T14:49:50.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"响应消息处理器\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2023-05-02T14:49:50.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"说明","slug":"说明","link":"#说明","children":[]},{"level":2,"title":"@Jt808ResponseBody","slug":"jt808responsebody","link":"#jt808responsebody","children":[{"level":3,"title":"说明","slug":"说明-1","link":"#说明-1","children":[]},{"level":3,"title":"示例","slug":"示例","link":"#示例","children":[]}]},{"level":2,"title":"Jt808Response","slug":"jt808response","link":"#jt808response","children":[{"level":3,"title":"说明","slug":"说明-2","link":"#说明-2","children":[]},{"level":3,"title":"示例","slug":"示例-1","link":"#示例-1","children":[]}]}],"git":{"createdTime":1671812085000,"updatedTime":1683038990000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":2}]},"readingTime":{"minutes":1.43,"words":430},"filePathRelative":"v2/jt-808/guide/basic/response-msg-handler.md","localizedDate":"2022年12月24日","autoDesc":true,"excerpt":"\\n<h2>说明</h2>\\n<div class=\\"hint-container tip\\">\\n<p class=\\"hint-container-title\\">提示</p>\\n<p>对响应给客户端的消息类型没有强制指定固定类型，任何类型的消息处理器返回的结果都被包装成了 <code>Jt808HandlerResult</code>。</p>\\n<p>而 <code>Jt808HandlerResult</code> 是由 <code>Jt808HandlerResultHandler</code> 处理的。</p>\\n<p>所以，支持哪些类型的响应消息取决于 <code>Jt808HandlerResultHandler</code> 的配置。</p>\\n</div>"}');export{v as comp,b as data};
