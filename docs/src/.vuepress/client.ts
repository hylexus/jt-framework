import { defineClientConfig } from 'vuepress/client'
import RepoGitee from './components/RepoGitee'

export default defineClientConfig({
    enhance({ app }) {
        app.component('RepoGitee', RepoGitee)
    },
})
