import{_ as s}from"./plugin-vue_export-helper-DlAUqK2U.js";import{r as o,o as i,c as l,b as n,d as a,e as t,a as r}from"./app-Djo5EUbb.js";const c={},p=n("h1",{id:"terminalvalidator",tabindex:"-1"},[n("a",{class:"header-anchor",href:"#terminalvalidator"},[n("span",null,"TerminalValidator")])],-1),u={class:"hint-container tip"},d=n("p",{class:"hint-container-title"},"鸣谢",-1),m={href:"https://github.com/anotherMe17",target:"_blank",rel:"noopener noreferrer"},k={href:"https://github.com/anotherMe17",target:"_blank",rel:"noopener noreferrer"},h={href:"https://github.com/hylexus/jt-framework/pull/19",target:"_blank",rel:"noopener noreferrer"},v=r(`<div class="language-java line-numbers-mode" data-ext="java" data-title="java"><pre class="language-java"><code><span class="token annotation punctuation">@Configuration</span>
<span class="token keyword">public</span> <span class="token keyword">class</span> <span class="token class-name">Jt808Configuration</span> <span class="token keyword">extends</span> <span class="token class-name">Jt808ServerConfigurationSupport</span> <span class="token punctuation">{</span>
    <span class="token comment">// [非必须配置] -- 可替换内置 TerminalValidator</span>
    <span class="token annotation punctuation">@Override</span>
    <span class="token keyword">public</span> <span class="token class-name">TerminalValidator</span> <span class="token function">terminalValidator</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
        <span class="token keyword">return</span> <span class="token keyword">new</span> <span class="token class-name">TerminalValidator</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
            <span class="token annotation punctuation">@Override</span>
            <span class="token keyword">public</span> <span class="token keyword">boolean</span> <span class="token function">validateTerminal</span><span class="token punctuation">(</span><span class="token class-name">RequestMsgMetadata</span> metadata<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token keyword">return</span> <span class="token boolean">true</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>

            <span class="token annotation punctuation">@Override</span>
            <span class="token keyword">public</span> <span class="token keyword">boolean</span> <span class="token function">needValidate</span><span class="token punctuation">(</span><span class="token class-name">RequestMsgMetadata</span> metadata<span class="token punctuation">,</span> <span class="token class-name">Integer</span> msgId<span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span class="token keyword">return</span> <span class="token boolean">true</span><span class="token punctuation">;</span>
            <span class="token punctuation">}</span>
        <span class="token punctuation">}</span><span class="token punctuation">;</span>
    <span class="token punctuation">}</span>
<span class="token punctuation">}</span>
</code></pre><div class="line-numbers" aria-hidden="true"><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div><div class="line-number"></div></div></div>`,1);function b(g,f){const e=o("ExternalLinkIcon");return i(),l("div",null,[p,n("div",u,[d,n("ul",null,[n("li",null,[a("该组件由 "),n("a",m,[a("anotherMe17"),t(e)]),a(" 贡献。")]),n("li",null,[a("非常感谢老哥 "),n("a",k,[a("anotherMe17"),t(e)]),a(" 的 "),n("a",h,[a("Pull request#19"),t(e)]),a(" 。")])])]),v])}const w=s(c,[["render",b],["__file","terminal-validator-config.html.vue"]]),x=JSON.parse('{"path":"/v1/jt-808/guide/customization/terminal-validator-config.html","title":"TerminalValidator","lang":"zh-CN","frontmatter":{"description":"TerminalValidator 鸣谢 该组件由 anotherMe17 贡献。 非常感谢老哥 anotherMe17 的 Pull request#19 。","head":[["meta",{"property":"og:url","content":"https://github.com/hylexus/jt-framework/jt-framework/v1/jt-808/guide/customization/terminal-validator-config.html"}],["meta",{"property":"og:site_name","content":"jt-framework"}],["meta",{"property":"og:title","content":"TerminalValidator"}],["meta",{"property":"og:description","content":"TerminalValidator 鸣谢 该组件由 anotherMe17 贡献。 非常感谢老哥 anotherMe17 的 Pull request#19 。"}],["meta",{"property":"og:type","content":"article"}],["meta",{"property":"og:locale","content":"zh-CN"}],["meta",{"property":"og:updated_time","content":"2022-12-23T16:14:45.000Z"}],["meta",{"property":"article:author","content":"hylexus"}],["meta",{"property":"article:modified_time","content":"2022-12-23T16:14:45.000Z"}],["script",{"type":"application/ld+json"},"{\\"@context\\":\\"https://schema.org\\",\\"@type\\":\\"Article\\",\\"headline\\":\\"TerminalValidator\\",\\"image\\":[\\"\\"],\\"dateModified\\":\\"2022-12-23T16:14:45.000Z\\",\\"author\\":[{\\"@type\\":\\"Person\\",\\"name\\":\\"hylexus\\",\\"url\\":\\"https://github.com/hylexus\\"}]}"]]},"headers":[],"git":{"createdTime":1671812085000,"updatedTime":1671812085000,"contributors":[{"name":"hylexus","email":"hylexus@163.com","commits":1}]},"readingTime":{"minutes":0.24,"words":73},"filePathRelative":"v1/jt-808/guide/customization/terminal-validator-config.md","localizedDate":"2022年12月24日","autoDesc":true,"excerpt":"\\n<div class=\\"hint-container tip\\">\\n<p class=\\"hint-container-title\\">鸣谢</p>\\n<ul>\\n<li>该组件由 <a href=\\"https://github.com/anotherMe17\\" target=\\"_blank\\" rel=\\"noopener noreferrer\\">anotherMe17</a> 贡献。</li>\\n<li>非常感谢老哥 <a href=\\"https://github.com/anotherMe17\\" target=\\"_blank\\" rel=\\"noopener noreferrer\\">anotherMe17</a> 的 <a href=\\"https://github.com/hylexus/jt-framework/pull/19\\" target=\\"_blank\\" rel=\\"noopener noreferrer\\">Pull request#19</a> 。</li>\\n</ul>\\n</div>"}');export{w as comp,x as data};