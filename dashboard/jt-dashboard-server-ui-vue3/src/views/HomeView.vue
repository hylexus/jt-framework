<script setup lang="ts">
import { requestServerInstances } from '@/api/dashboard'
import { isSuccess } from '@/utils/common-utils'
import Jt808ServerInstanceBox from '@/components/Jt808ServerInstanceBox.vue'
import Jt1078ServerInstanceBox from '@/components/Jt1078ServerInstanceBox.vue'
import { onMounted, onUnmounted, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowRight } from '@element-plus/icons-vue'

const interval = ref(10)
let timer: NodeJS.Timeout
let serverMetadata = reactive({
  jt808ServerMetadata: [],
  jt1078ServerMetadata: []
})
onMounted(() => {
  loadServerMetadata()
  timer = setInterval(() => {
    loadServerMetadata()
  }, interval.value * 1000)
})
onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
const loadServerMetadata = async () => {
  const resp: any = await requestServerInstances()
  if (isSuccess(resp)) {
    serverMetadata.jt808ServerMetadata = resp.data.jt808ServerMetadata || []
    serverMetadata.jt1078ServerMetadata = resp.data.jt1078ServerMetadata || []
  } else {
    ElMessage.error(resp.msg)
  }
}
</script>
<template>
  <nav p4>
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item>Dashboard</el-breadcrumb-item>
    </el-breadcrumb>
  </nav>
  <div>
    <h3>808 Server Instance</h3>
    <el-row :gutter="20" justify="center">
      <el-col :span="8" p4 v-for="(it, index) in serverMetadata.jt808ServerMetadata" :key="index">
        <Jt808ServerInstanceBox :config="it" />
      </el-col>
    </el-row>
    <h3>1078 Server Instance</h3>
    <el-row :gutter="20" justify="center">
      <el-col :span="8" p4 v-for="(it, index) in serverMetadata.jt1078ServerMetadata" :key="index">
        <Jt1078ServerInstanceBox :config="it" />
      </el-col>
    </el-row>
  </div>
</template>
