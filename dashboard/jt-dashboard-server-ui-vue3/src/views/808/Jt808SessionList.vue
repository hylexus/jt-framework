<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { requestTerminalList } from '@/api/jt808-api'
import * as CommonUtils from '@/utils/common-utils'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowRight, Refresh, Search } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const query = reactive({
  instanceId: '',
  page: 1,
  rows: 0,
  pageSize: 5,
  terminalId: '',
  version: 'all'
})
const table = reactive({
  total: 0,
  data: []
})
onMounted(() => {
  reloadData()
})
const id = route.params?.instanceId
const sim = route.params?.sim
if (!id) {
  ElMessage.error('instanceId is empty')
}
if (typeof id === 'string') {
  query.instanceId = id
}
if (typeof sim === 'string') {
  query.terminalId = sim
}
const resetData = () => {
  query.version = 'all'
  query.terminalId = ''
  reloadData()
}
const reloadData = async () => {
  try {
    const resp: any = await requestTerminalList({ ...query })
    if (CommonUtils.isSuccess(resp)) {
      table.data = resp.data.records
      table.total = resp.data.total
    } else {
      console.log(resp)
      ElMessage.error(resp.msg)
    }
  } catch (e: any) {
    ElMessage.error(e)
  }
}
const currentChange = (currentPage: number) => {
  query.page = currentPage
  reloadData()
}
const sizeChange = (pageSize: number) => {
  query.pageSize = pageSize
  reloadData()
}
</script>
<template>
  <nav p4>
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item to="/">Dashboard</el-breadcrumb-item>
      <el-breadcrumb-item>808ServerInstance</el-breadcrumb-item>
      <el-breadcrumb-item>{{ query.instanceId }}11</el-breadcrumb-item>
    </el-breadcrumb>
  </nav>
  <el-card class="box-card terminal-list-box" style="border-radius: 10px">
    <template #header>
      <el-form inline size="small" @submit.prevent>
        <el-form-item label="终端ID / SIM">
          <el-input
            v-model="query.terminalId"
            placeholder="terminalId"
            @keyup.enter="reloadData"
          ></el-input>
        </el-form-item>
        <el-form-item label="版本">
          <el-radio-group v-model="query.version" @change="reloadData">
            <el-radio-button label="all">All</el-radio-button>
            <el-radio-button label="2019">V2019</el-radio-button>
            <el-radio-button label="2013">V2013</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="reloadData" :icon="Search">查询</el-button>
          <el-button @click="resetData" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </template>
    <el-table :data="table.data" border stripe w-full>
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column prop="terminalId" label="终端ID / SIM" width="220"> </el-table-column>
      <el-table-column prop="version" label="协议版本" width="100" filter-placement="bottom-end">
        <template #default="scope">
          <el-tag :type="scope.row.version === '2019' ? '' : 'success'" disable-transitions
            >{{ scope.row.version }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="会话建立时间" width="200"> </el-table-column>
      <el-table-column prop="lastCommunicationTime" label="最近一次通信时间"> </el-table-column>
      <el-table-column fixed="right" label="操作" width="100">
        <template #default="{ row }">
          <router-link
            type="primary"
            :to="`/808-instance/${query.instanceId}/video-player/${row.terminalId}`"
            >音视频</router-link
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      mt8
      :page-sizes="[5, 10, 20, 30, 50, 100, 200]"
      :current-page="query.page"
      :page-size="query.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="table.total"
      @pagination="table.data"
      @size-change="sizeChange"
      @current-change="currentChange"
    />
  </el-card>
</template>

<style lang="scss">
.terminal-list-box {
  .ep-card__header {
    padding-bottom: 0;
  }
}
</style>
