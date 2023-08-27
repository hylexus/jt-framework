import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '@/views/HomeView.vue'

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/808-instance/:instanceId/session-list',
        name: 'Jt808SessionList',
        component: () => import('@/views/808/Jt808SessionList.vue')
    },
    {
        path: '/808-instance/:instanceId/video-player/:sim',
        name: 'PlayerDemo01',
        component: () => import('@/views/1078/Jt1078SessionFlvPlayer.vue')
    },
]

const router = new VueRouter({
    mode: 'hash',
    base: process.env.BASE_URL,
    routes
})

export default router
