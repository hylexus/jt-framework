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
