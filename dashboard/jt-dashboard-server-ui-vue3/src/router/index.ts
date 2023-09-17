import { createRouter, createWebHashHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'

const router = createRouter({
  history: createWebHashHistory(import.meta.env.BASE_URL),
  routes: [
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
      path: '/1078-instance/:instanceId/session-list',
      name: 'Jt1078SessionList',
      component: () => import('@/views/1078/Jt1078SessionList.vue')
    },
    {
      path: '/808-instance/:instanceId/video-player/:sim',
      name: 'PlayerDemo01',
      component: () => import('@/views/1078/Jt1078SessionFlvPlayer.vue')
    },
    {
      path: '/1078-instance/:instanceId/video-player',
      name: 'PlayerDemo02',
      component: () => import('@/views/1078/Jt1078SessionFlvPlayer.vue')
    }
  ]
})

export default router
