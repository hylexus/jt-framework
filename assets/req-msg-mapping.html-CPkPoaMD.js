import{_ as o}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as c,o as i,c as l,b as a,d as t,e as n,w as s,a as d}from"./app-Djo5EUbb.js";const p={},r=d(`<h1 id="请求消息映射" tabindex="-1"><a class="header-anchor" href="#请求消息映射"><span>请求消息映射</span></a></h1><h2 id="jt808reqmsgbody" tabindex="-1"><a class="header-anchor" href="#jt808reqmsgbody"><span>@Jt808ReqMsgBody</span></a></h2><p>该注解只能标记于 <code>请求消息体实体类</code> 上。</p><div class="hint-container caution"><p class="hint-container-title">注意</p><p>目前为止，<code>请求消息体实体类</code> 必须实现标记接口 <code>RequestMsgBody</code> 。</p></div><p>类似于 <code>Hibernate</code> 和 <code>MyBatis</code> 中的 <code>@Table</code> 注解。</p><table><thead><tr><th>属性</th><th>解释</th><th>取值示例</th></tr></thead><tbody><tr><td><code>msgType</code></td><td>808报文类型，消息Id</td><td>0x0200</td></tr></tbody></table><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token annotation punctuation">@Data</span>
<span class="token annotation punctuation">@Accessors</span><span class="token punctuation">(</span>chain <span class="token operator">=</span> <span class="token boolean">true</span><span class="token punctuation">)</span>
<span class="token annotation punctuation">@Jt808ReqMsgBody</span><span class="token punctuation">(</span>msgType <span class="token operator">=</span> <span class="token number">0x0200</span><span class="token punctuation">)</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">LocationUploadMsgBody</span> <span class="token keyword">implements</span> <span class="token class-name">RequestMsgBody</span> <span class="token punctuation">{</span>
    <span class="token comment">// ...</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="basicfield" tabindex="-1"><a class="header-anchor" href="#basicfield"><span>@BasicField</span></a></h2><p>只能标记于字段上。类比于<code>Hibernate</code> 或 <code>MyBatis</code> 中的 <code>@Column</code>、<code>@Basic</code>。</p><table><thead><tr><th>属性</th><th>解释</th><th>取值示例</th></tr></thead><tbody><tr><td><code>startIndex</code></td><td>起始字节索引</td><td>0、4、12</td></tr><tr><td><code>startIndexMethod</code></td><td>作用和startIndex相同，当startIndex无法直接指定时可根据该属性定义的方法名返回字节数</td><td><code>&quot;getLength&quot;</code></td></tr><tr><td><code>dataType</code></td><td>数据类型</td><td>WORD、DWORD</td></tr><tr><td><code>length</code></td><td>长度，字节数</td><td>4、6</td></tr><tr><td><code>byteCountMethod</code></td><td>作用和length相同，当length无法直接指定时可根据该属性定义的方法名返回字节数</td><td><code>&quot;getLength&quot;</code></td></tr><tr><td><code>customerDataTypeConverterClass</code></td><td>自定义的类型转换器</td><td></td></tr></tbody></table><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token annotation punctuation">@BasicField</span><span class="token punctuation">(</span>startIndex <span class="token operator">=</span> <span class="token number">4</span><span class="token punctuation">,</span> dataType <span class="token operator">=</span> <span class="token constant">BYTES</span><span class="token punctuation">,</span> length <span class="token operator">=</span> <span class="token number">4</span><span class="token punctuation">)</span>
<span class="token keyword">private</span> <span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> statusBytes<span class="token punctuation">;</span>

<span class="token annotation punctuation">@BasicField</span><span class="token punctuation">(</span>startIndex <span class="token operator">=</span> <span class="token number">4</span><span class="token punctuation">,</span> dataType <span class="token operator">=</span> <span class="token constant">BYTES</span><span class="token punctuation">,</span> byteCountMethod <span class="token operator">=</span> &#39;getLength&#39;<span class="token punctuation">)</span>
<span class="token keyword">private</span> <span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span> statusBytes1<span class="token punctuation">;</span>

<span class="token keyword">public</span> <span class="token keyword">int</span> <span class="token function">getLength</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
    <span class="token keyword">return</span> <span class="token number">4</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="splittablefield" tabindex="-1"><a class="header-anchor" href="#splittablefield"><span>@SplittableField</span></a></h2><p>将被修饰的字段拆分之后赋值给另一个bean。</p><div class="hint-container caution"><p class="hint-container-title">注意</p><p>该注解目前仅仅适用于 <code>int</code> 、 <code>short</code> 、 <code>byte</code> 类型的字段。</p></div><table><thead><tr><th>属性</th><th>解释</th><th>取值示例</th></tr></thead><tbody><tr><td><code>splitPropertyValueIntoNestedBeanField</code></td><td>目标属性</td><td></td></tr></tbody></table>`,15),u=d('<h2 id="slicedfrom" tabindex="-1"><a class="header-anchor" href="#slicedfrom"><span>@SlicedFrom</span></a></h2><div class="hint-container caution"><p class="hint-container-title">注意</p><p>该注解目前仅仅适用于 <code>int</code> 、 <code>short</code> 、 <code>byte</code> 类型的字段。</p></div><table><thead><tr><th>属性</th><th>解释</th><th>取值示例</th></tr></thead><tbody><tr><td><code>sourceFieldName</code></td><td>源字段名</td><td></td></tr><tr><td><code>bitIndex</code></td><td>源字段中的第几个bit</td><td>0</td></tr><tr><td><code>startBitIndex</code></td><td>源字段中的起始bit索引</td><td>1</td></tr><tr><td><code>endBitIndex</code></td><td>源字段中的终止bit索引</td><td>2</td></tr></tbody></table>',3),h=d('<h2 id="extrafield-extramsgbody" tabindex="-1"><a class="header-anchor" href="#extrafield-extramsgbody"><span>@ExtraField&amp;@ExtraMsgBody</span></a></h2><ul><li>@ExtraField</li></ul><p>适用于类似位置附加消息的结构。</p><table><thead><tr><th>属性</th><th>解释</th><th>取值示例</th></tr></thead><tbody><tr><td><code>startIndex</code></td><td>起始字节索引</td><td>28</td></tr><tr><td><code>length</code></td><td>字节数</td><td>20</td></tr><tr><td><code>byteCountMethod</code></td><td>作用和length相同，当length无法直接指定时可根据该属性定义的方法名返回字节数</td><td>aFieldName</td></tr><tr><td><code>byteCountOfMsgId</code></td><td>消息ID占用几个字节</td><td>1</td></tr><tr><td><code>byteCountOfContentLength</code></td><td>表示消息长度的字段占用几个字节</td><td>1</td></tr></tbody></table><ul><li>@ExtraMsgBody</li></ul><p>用于嵌套的附加信息实体类。</p><table><thead><tr><th>属性</th><th>解释</th><th>取值示例</th></tr></thead><tbody><tr><td><code>byteCountOfMsgId</code></td><td>消息ID占用几个字节</td><td>1</td></tr><tr><td><code>byteCountOfContentLength</code></td><td>表示消息长度的字段占用几个字节</td><td>1</td></tr></tbody></table>',7);function m(b,y){const e=c("RouteLink");return i(),l("div",null,[r,a("p",null,[t("用法请"),n(e,{to:"/jt-808/guide/annotation-based-dev/location-msg-parse-demo.html#2.%E4%BD%BF%E7%94%A8@SlicedFrom%E8%A7%A3%E6%9E%90"},{default:s(()=>[t("参考示例")]),_:1}),t("。")]),u,a("p",null,[t("用法请"),n(e,{to:"/jt-808/guide/annotation-based-dev/location-msg-parse-demo.html#2.%E4%BD%BF%E7%94%A8@SlicedFrom%E8%A7%A3%E6%9E%90"},{default:s(()=>[t("参考示例")]),_:1}),t("。")]),h,a("p",null,[t("用法请"),n(e,{to:"/jt-808/guide/annotation-based-dev/location-msg-parse-demo.html#%E8%A7%A3%E6%9E%90%E4%BD%8D%E7%BD%AE%E9%99%84%E5%8A%A0%E9%A1%B9%E5%88%97%E8%A1%A8"},{default:s(()=>[t("参考示例")]),_:1}),t("。")])])}const v=o(p,[["render",m],["__file","req-msg-mapping.html.vue"]]),x=JSON.parse('{"path":"/v1/jt-808/guide/annotation-based-dev/req-msg-mapping.html","title":"请求消息映射","lang":"zh-CN","frontmatter":{"description":"请求消息映射 @Jt808ReqMsgBody 该注解只能标记于 请求消息体实体类 上。 注意 目前为止，请求消息体实体类 必须实现标记接口 RequestMsgBody 。 类似于 Hibernate 和 MyBatis 中的 @Table 注解。 @BasicField 只能标记于字段上。类比于Hibernate 或 MyBatis 中的 @Col...","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v1/jt-808/guide/annotation-based-dev/req-msg-mapping.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"请求消息映射"}],["meta",{"property":"og:description","content":"请求消息映射 @Jt808ReqMsgBody 该注解只能标记于 请求消息体实体类 上。 注意 目前为止，请求消息体实体类 必须实现标记接口 RequestMsgBody 。 类似于 Hibernate 和 MyBatis 中的 @Table 注解。 @BasicField 只能标记于字段上。类比于Hibernate 或 MyBatis 中的 @Col..."}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2022-12-23T16:14:45.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2022-12-23T16:14:45.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"请求消息映射\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2022-12-23T16:14:45.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"@Jt808ReqMsgBody","slug":"jt808reqmsgbody","link":"#jt808reqmsgbody","children":[]},{"level":2,"title":"@BasicField","slug":"basicfield","link":"#basicfield","children":[]},{"level":2,"title":"@SplittableField","slug":"splittablefield","link":"#splittablefield","children":[]},{"level":2,"title":"@SlicedFrom","slug":"slicedfrom","link":"#slicedfrom","children":[]},{"level":2,"title":"@ExtraField&@ExtraMsgBody","slug":"extrafield-extramsgbody","link":"#extrafield-extramsgbody","children":[]}],"git":{"createdTime":1671812085000,"updatedTime":1671812085000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":1.95,"words":584},"filePathRelative":"v1/jt-808/guide/annotation-based-dev/req-msg-mapping.md","localizedDate":"2022年12月24日","autoDesc":true,"excerpt":"\\n<h2>@Jt808ReqMsgBody</h2>\\n<p>该注解只能标记于 <code>请求消息体实体类</code> 上。</p>\\n<div class=\\"hint-container caution\\">\\n<p class=\\"hint-container-title\\">注意</p>\\n<p>目前为止，<code>请求消息体实体类</code> 必须实现标记接口 <code>RequestMsgBody</code> 。</p>\\n</div>\\n<p>类似于 <code>Hibernate</code> 和 <code>MyBatis</code> 中的 <code>@Table</code> 注解。</p>"}');export{v as comp,x as data};