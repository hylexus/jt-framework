import{_ as n}from"./plugin-vue_export-helper-DlAUqK2U.js";import{o as s,c as a,a as t}from"./app-Djo5EUbb.js";const e={},p=t(`<h1 id="协议扫盲" tabindex="-1"><a class="header-anchor" href="#协议扫盲"><span>协议扫盲</span></a></h1><h2 id="数据类型" tabindex="-1"><a class="header-anchor" href="#数据类型"><span>数据类型</span></a></h2><h3 id="_808协议数据类型" tabindex="-1"><a class="header-anchor" href="#_808协议数据类型"><span>808协议数据类型</span></a></h3><table><thead><tr><th>数据类型</th><th>描述及要求</th></tr></thead><tbody><tr><td>BYTE</td><td>无符号单字节整形（字节， 8 位）</td></tr><tr><td>WORD</td><td>无符号双字节整形（字， 16 位）</td></tr><tr><td>DWORD</td><td>无符号四字节整形（双字， 32 位）</td></tr><tr><td>BYTE[n]</td><td>n 字节</td></tr><tr><td>BCD[n]</td><td>8421 码， n 字节</td></tr><tr><td>STRING</td><td>GBK 编码，若无数据，置空</td></tr></tbody></table><h3 id="对应java数据类型" tabindex="-1"><a class="header-anchor" href="#对应java数据类型"><span>对应Java数据类型</span></a></h3><p>和文档中定义的数据类型都在枚举类 <code>io.github.hylexus.jt.data.MsgDataType</code> 中。</p><div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token annotation punctuation">@Getter</span>
<span class="token keyword">public</span> <span class="token keyword">enum</span> <span class="token class-name">MsgDataType</span> <span class="token punctuation">{</span>
    <span class="token function">BYTE</span><span class="token punctuation">(</span><span class="token number">1</span><span class="token punctuation">,</span> <span class="token string">&quot;无符号单字节整型(字节，8 位)&quot;</span><span class="token punctuation">,</span> <span class="token function">newHashSet</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token class-name">Byte</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token keyword">int</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token class-name">Integer</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token class-name">Short</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token keyword">short</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">,</span>
    <span class="token function">BYTES</span><span class="token punctuation">(</span><span class="token number">0</span><span class="token punctuation">,</span> <span class="token string">&quot;&quot;</span><span class="token punctuation">,</span> <span class="token function">newHashSet</span><span class="token punctuation">(</span><span class="token keyword">byte</span><span class="token punctuation">[</span><span class="token punctuation">]</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">,</span>
    <span class="token function">WORD</span><span class="token punctuation">(</span><span class="token number">2</span><span class="token punctuation">,</span> <span class="token string">&quot;无符号双字节整型(字，16 位)&quot;</span><span class="token punctuation">,</span> <span class="token function">newHashSet</span><span class="token punctuation">(</span><span class="token keyword">short</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token class-name">Short</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token keyword">int</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token class-name">Integer</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">,</span>
    <span class="token comment">// https://github.com/hylexus/jt-framework/issues/34</span>
    <span class="token function">DWORD</span><span class="token punctuation">(</span><span class="token number">4</span><span class="token punctuation">,</span> <span class="token string">&quot;无符号四字节整型(双字，32 位)&quot;</span><span class="token punctuation">,</span> <span class="token function">newHashSet</span><span class="token punctuation">(</span><span class="token keyword">long</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token class-name">Long</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token keyword">int</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">,</span> <span class="token class-name">Integer</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">,</span>
    <span class="token function">BCD</span><span class="token punctuation">(</span><span class="token number">0</span><span class="token punctuation">,</span> <span class="token string">&quot;8421 码，n 字节&quot;</span><span class="token punctuation">,</span> <span class="token function">newHashSet</span><span class="token punctuation">(</span><span class="token class-name">String</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">,</span>
    <span class="token function">STRING</span><span class="token punctuation">(</span><span class="token number">0</span><span class="token punctuation">,</span> <span class="token string">&quot;GBK 编码，若无数据，置空&quot;</span><span class="token punctuation">,</span> <span class="token function">newHashSet</span><span class="token punctuation">(</span><span class="token class-name">String</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">,</span>
    <span class="token function">UNKNOWN</span><span class="token punctuation">(</span><span class="token number">0</span><span class="token punctuation">,</span> <span class="token string">&quot;未知类型，用于占位符或默认值&quot;</span><span class="token punctuation">,</span> <span class="token function">newHashSet</span><span class="token punctuation">(</span><span class="token class-name">String</span><span class="token punctuation">.</span><span class="token keyword">class</span><span class="token punctuation">)</span><span class="token punctuation">)</span><span class="token punctuation">,</span>
    <span class="token punctuation">;</span>

    <span class="token doc-comment comment">/**
     * 字节数
     * 为零表示使用外部指定的长度
     *
     * <span class="token keyword">@see</span> <span class="token reference"><span class="token class-name">BasicField</span><span class="token punctuation">#</span><span class="token function">length</span><span class="token punctuation">(</span><span class="token punctuation">)</span></span>
     */</span>
    <span class="token keyword">private</span> <span class="token keyword">final</span> <span class="token keyword">int</span> byteCount<span class="token punctuation">;</span>

    <span class="token keyword">private</span> <span class="token keyword">final</span> <span class="token class-name">String</span> desc<span class="token punctuation">;</span>

    <span class="token keyword">private</span> <span class="token keyword">final</span> <span class="token class-name">Set</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token class-name">Class</span><span class="token punctuation">&lt;</span><span class="token operator">?</span><span class="token punctuation">&gt;</span><span class="token punctuation">&gt;</span></span> expectedTargetClassType<span class="token punctuation">;</span>

    <span class="token class-name">MsgDataType</span><span class="token punctuation">(</span><span class="token keyword">int</span> byteCount<span class="token punctuation">,</span> <span class="token class-name">String</span> desc<span class="token punctuation">,</span> <span class="token class-name">Set</span><span class="token generics"><span class="token punctuation">&lt;</span><span class="token class-name">Class</span><span class="token punctuation">&lt;</span><span class="token operator">?</span><span class="token punctuation">&gt;</span><span class="token punctuation">&gt;</span></span> expectedTargetClassType<span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">this</span><span class="token punctuation">.</span>byteCount <span class="token operator">=</span> byteCount<span class="token punctuation">;</span>
        <span class="token keyword">this</span><span class="token punctuation">.</span>desc <span class="token operator">=</span> desc<span class="token punctuation">;</span>
        <span class="token keyword">this</span><span class="token punctuation">.</span>expectedTargetClassType <span class="token operator">=</span> expectedTargetClassType<span class="token punctuation">;</span>
    <span class="token punctuation">}</span>

<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div><h2 id="消息结构" tabindex="-1"><a class="header-anchor" href="#消息结构"><span>消息结构</span></a></h2><table><thead><tr><th>标识位</th><th>消息头</th><th>消息体</th><th>校验码</th><th>标识位</th></tr></thead><tbody><tr><td>1byte(0x7e)</td><td>16byte</td><td></td><td>1byte</td><td>1byte(0x7e)</td></tr></tbody></table><h2 id="消息头" tabindex="-1"><a class="header-anchor" href="#消息头"><span>消息头</span></a></h2><p>整个报文中最复杂也就是消息头的处理了。</p><ul><li><code>分包消息</code> 的消息头长度为 <code>16字节</code></li><li><code>非分包消息</code> 的消息头长度为 <code>12字节</code></li></ul><p>消息头的结构如下所示：</p><div class="language-text line-numbers-mode" data-ext="text" data-title="text"><pre class="language-text"><code>消息ID(0-1)	消息体属性(2-3)	终端手机号(4-9)	消息流水号(10-11)	消息包封装项(12-15)

byte[0-1] 	消息ID word(16)
byte[2-3] 	消息体属性 word(16)
		bit[0-9]	消息体长度
		bit[10-12]	数据加密方式
						此三位都为 0，表示消息体不加密
						第 10 位为 1，表示消息体经过 RSA 算法加密
						其它保留
		bit[13]		分包
						1：消息体卫长消息，进行分包发送处理，具体分包信息由消息包封装项决定
						0：则消息头中无消息包封装项字段
		bit[14-15]	保留
byte[4-9] 	终端手机号或设备ID bcd[6]
		根据安装后终端自身的手机号转换
		手机号不足12 位，则在前面补 0
byte[10-11] 	消息流水号 word(16)
		按发送顺序从 0 开始循环累加
byte[12-15] 	消息包封装项
		byte[0-1]	消息包总数(word(16))
						该消息分包后得总包数
		byte[2-3]	包序号(word(16))
						从 1 开始
		如果消息体属性中相关标识位确定消息分包处理,则该项有内容
		否则无该项
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,14),o=[p];function c(l,i){return s(),a("div",null,o)}const r=n(e,[["render",c],["__file","protocol-introduction.html.vue"]]),k=JSON.parse('{"path":"/v1/jt-808/guide/basic/protocol-introduction.html","title":"协议扫盲","lang":"zh-CN","frontmatter":{"description":"协议扫盲 数据类型 808协议数据类型 对应Java数据类型 和文档中定义的数据类型都在枚举类 io.github.hylexus.jt.data.MsgDataType 中。 消息结构 消息头 整个报文中最复杂也就是消息头的处理了。 分包消息 的消息头长度为 16字节 非分包消息 的消息头长度为 12字节 消息头的结构如下所示：","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v1/jt-808/guide/basic/protocol-introduction.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"协议扫盲"}],["meta",{"property":"og:description","content":"协议扫盲 数据类型 808协议数据类型 对应Java数据类型 和文档中定义的数据类型都在枚举类 io.github.hylexus.jt.data.MsgDataType 中。 消息结构 消息头 整个报文中最复杂也就是消息头的处理了。 分包消息 的消息头长度为 16字节 非分包消息 的消息头长度为 12字节 消息头的结构如下所示："}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2022-12-23T16:14:45.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2022-12-23T16:14:45.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"协议扫盲\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2022-12-23T16:14:45.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[{"level":2,"title":"数据类型","slug":"数据类型","link":"#数据类型","children":[{"level":3,"title":"808协议数据类型","slug":"_808协议数据类型","link":"#_808协议数据类型","children":[]},{"level":3,"title":"对应Java数据类型","slug":"对应java数据类型","link":"#对应java数据类型","children":[]}]},{"level":2,"title":"消息结构","slug":"消息结构","link":"#消息结构","children":[]},{"level":2,"title":"消息头","slug":"消息头","link":"#消息头","children":[]}],"git":{"createdTime":1671812085000,"updatedTime":1671812085000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":2.17,"words":650},"filePathRelative":"v1/jt-808/guide/basic/protocol-introduction.md","localizedDate":"2022年12月24日","autoDesc":true,"excerpt":"\\n<h2>数据类型</h2>\\n<h3>808协议数据类型</h3>\\n<table>\\n<thead>\\n<tr>\\n<th>数据类型</th>\\n<th>描述及要求</th>\\n</tr>\\n</thead>\\n<tbody>\\n<tr>\\n<td>BYTE</td>\\n<td>无符号单字节整形（字节， 8 位）</td>\\n</tr>\\n<tr>\\n<td>WORD</td>\\n<td>无符号双字节整形（字， 16 位）</td>\\n</tr>\\n<tr>\\n<td>DWORD</td>\\n<td>无符号四字节整形（双字， 32 位）</td>\\n</tr>\\n<tr>\\n<td>BYTE[n]</td>\\n<td>n 字节</td>\\n</tr>\\n<tr>\\n<td>BCD[n]</td>\\n<td>8421 码， n 字节</td>\\n</tr>\\n<tr>\\n<td>STRING</td>\\n<td>GBK 编码，若无数据，置空</td>\\n</tr>\\n</tbody>\\n</table>"}');export{r as comp,k as data};