import{_ as t}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as s,o,c,b as n,d as e,e as l,w as i,a as p}from"./app-Djo5EUbb.js";const d={},r=n("h1",{id:"异常处理",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#异常处理"},[n("span",null,"异常处理")])],-1),u=n("p",null,[e("直接实现 "),n("code",null,"MsgHandler"),e(" 等接口并 "),n("code",null,"手动注册"),e(" 的组件的异常处理可以在实现类中自己处理。")],-1),m=n("p",null,[e("基于注解的 "),n("code",null,"MsgHandler"),e(" 的异常处理可以直接由注解实现。")],-1),v=p(`<h2 id="jt808requestmsghandleradvice" tabindex="-1"><a class="header-anchor" href="#jt808requestmsghandleradvice"><span>@Jt808RequestMsgHandlerAdvice</span></a></h2><p>类比于 <code>Spring</code> 的 <code>@ControllerAdvice</code> 注解。</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token annotation punctuation">@Slf4j</span>
<span class="token annotation punctuation">@Jt808RequestMsgHandlerAdvice</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">BuiltinDefaultExceptionHandler</span> <span class="token punctuation">{</span>
    <span class="token comment">// ...</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="jt808exceptionhandler" tabindex="-1"><a class="header-anchor" href="#jt808exceptionhandler"><span>@Jt808ExceptionHandler</span></a></h2><p>类比于 <code>Spring</code> 的 <code>@ExceptionHandler</code> 注解。</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token annotation punctuation">@Slf4j</span>
<span class="token annotation punctuation">@Jt808RequestMsgHandlerAdvice</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">BuiltinDefaultExceptionHandler</span> <span class="token punctuation">{</span>

    <span class="token annotation punctuation">@Jt808ExceptionHandler</span><span class="token punctuation">(</span><span class="token punctuation">{</span><span class="token class-name">Throwable</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">}</span><span class="token punctuation">)</span>
    <span class="token keyword">public</span> <span class="token keyword">void</span> <span class="token function">processThrowable</span><span class="token punctuation">(</span><span class="token class-name">Throwable</span> throwable<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        log<span class="token punctuation">.</span><span class="token function">info</span><span class="token punctuation">(</span><span class="token string">&quot;BuiltinDefaultExceptionHandler :&quot;</span><span class="token punctuation">,</span> throwable<span class="token punctuation">)</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><div class="hint-container tip"><p class="hint-container-title">传送门</p><p>内置的异常处理器位于 <code>BuiltinDefaultExceptionHandler</code> 中。</p></div>`,7);function h(g,k){const a=s("RouteLink");return o(),c("div",null,[r,u,m,n("p",null,[e("具体配置请移步 "),l(a,{to:"/v1/jt-808/config/#exception-handler-scan"},{default:i(()=>[e("配置文档")]),_:1})]),v])}const f=t(d,[["render",h],["__file","exception-handler.html.vue"]]),j=JSON.parse('{"path":"/v1/jt-808/guide/annotation-based-dev/exception-handler.html","title":"异常处理","lang":"zh-CN","frontmatter":{"description":"异常处理 直接实现 MsgHandler 等接口并 手动注册 的组件的异常处理可以在实现类中自己处理。 基于注解的 MsgHandler 的异常处理可以直接由注解实现。 具体配置请移步 @Jt808RequestMsgHandlerAdvice 类比于 Spring 的 @ControllerAdvice 注解。 @Jt808ExceptionHand...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v1/jt-808/guide/annotation-based-dev/exception-handler.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"异常处理"}],["meta",{"property":"og:description","content":"异常处理 直接实现 MsgHandler 等接口并 手动注册 的组件的异常处理可以在实现类中自己处理。 基于注解的 MsgHandler 的异常处理可以直接由注解实现。 具体配置请移步 @Jt808RequestMsgHandlerAdvice 类比于 Spring 的 @ControllerAdvice 注解。 @Jt808ExceptionHand..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2022-12-23T16:14:45.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2022-12-23T16:14:45.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"异常处理\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2022-12-23T16:14:45.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"@Jt808RequestMsgHandlerAdvice","slug":"jt808requestmsghandleradvice","link":"#jt808requestmsghandleradvice","children":[]},{"level":2,"title":"@Jt808ExceptionHandler","slug":"jt808exceptionhandler","link":"#jt808exceptionhandler","children":[]}],"git":{"createdTime":1671812085000,"updatedTime":1671812085000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":0.43,"words":129},"filePathRelative":"v1/jt-808/guide/annotation-based-dev/exception-handler.md","localizedDate":"2022年12月24日","autoDesc":true,"excerpt":"\\n<p>直接实现 <code>MsgHandler</code> 等接口并 <code>手动注册</code> 的组件的异常处理可以在实现类中自己处理。</p>\\n<p>基于注解的 <code>MsgHandler</code> 的异常处理可以直接由注解实现。</p>\\n<p>具体配置请移步 <a href=\\"/jt-framework/v1/jt-808/config/#exception-handler-scan\\" target=\\"_blank\\">配置文档</a></p>\\n<h2>@Jt808RequestMsgHandlerAdvice</h2>\\n<p>类比于 <code>Spring</code> 的 <code>@ControllerAdvice</code> 注解。</p>"}');export{f as comp,j as data};