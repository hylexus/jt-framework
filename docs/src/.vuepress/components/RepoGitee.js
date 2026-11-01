/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2021 - PRESENT by Mr.Hope<mister-hope@outlook.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/*
 * Corresponds to vuepress-theme-hope: https://github.com/vuepress-theme-hope/vuepress-theme-hope/blob/v2.0.0-rc.32/packages/theme/src/client/modules/navbar/components/RepoLink.ts
 * Changes by hylexus: Gitee destination and local component/style integration.
 * Attribution restored 2026-10-02; exact historical import version is unknown.
 */

import {defineComponent, h, resolveComponent} from "vue";
import {GiteeIcon} from "vuepress-shared/client";
import './styles/repo-link.css'
export default defineComponent({
    name: "RepoGitee",
    components: {GiteeIcon},
    setup() {
        return () => h("div", { class: "nav-item vp-repo" }, h("a", {
            class: "vp-repo-link",
            href: 'https://gitee.com/hylexus/jt-framework',
            target: "_blank",
            rel: "noopener noreferrer",
            "aria-label": 'Gitee',
        }, h(resolveComponent('GiteeIcon'), {
            style: {
                width: "1.25rem",
                height: "1.25rem",
                verticalAlign: "middle",
            },
        })))
    },
});
