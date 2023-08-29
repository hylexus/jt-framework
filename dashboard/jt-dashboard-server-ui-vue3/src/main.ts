import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import '@/styles/index.scss'
import 'uno.css'
import 'element-plus/theme-chalk/src/message.scss'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.mount('#app')
