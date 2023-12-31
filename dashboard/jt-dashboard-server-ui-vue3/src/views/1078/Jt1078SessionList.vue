<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { requestTerminalList, unSubscribeSessions, unSubscribe } from '@/api/jt1078-api'
import * as CommonUtils from '@/utils/common-utils'
import { useRoute } from 'vue-router'
import { dayjs, ElMessage } from 'element-plus'
import { channelConfig } from '@/assets/json/jt1078-config'
import { Search, Refresh, ArrowRight } from '@element-plus/icons-vue'

const route = useRoute()
const query = reactive({
  instanceId: '',
  page: 1,
  rows: 0,
  sim: '',
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
const closeSubscribe = async (row: any) => {
  const res: any = await unSubscribe({
    instanceId: route.params.instanceId,
    subscriberId: row.id
  })
  if (res.code === 0) {
    row.closed = true
    ElMessage.success(res.msg)
  }
}
const closeSessions = async (row: any) => {
  const res: any = await unSubscribeSessions({
    instanceId: route.params.instanceId,
    sessionId: row.id
  })
  if (res.code === 0) {
    row.closed = true
    ElMessage.success(res.msg)
  }
}

const tableRowClassName = ({ row }: { row: any }) => {
  if (row?.createdAt < dayjs().subtract(30, 'm').format('YYYY-MM-DD HH:mm:ss')) {
    return 'warning-row'
  }
  return ''
}
</script>
<template>
  <nav p4>
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item to="/">Dashboard</el-breadcrumb-item>
      <el-breadcrumb-item>1078SessionList</el-breadcrumb-item>
      <el-breadcrumb-item>{{ query.instanceId }}11</el-breadcrumb-item>
    </el-breadcrumb>
  </nav>
  <el-card class="box-card terminal-list-box" style="border-radius: 10px">
    <template #header>
      <el-form inline size="small" @submit.prevent>
        <el-form-item label="终端ID / SIM">
          <el-input v-model="query.sim" placeholder="终端ID / SIM" @keyup.enter="reloadData"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="reloadData" :icon="Search">查询</el-button>
          <el-button @click="resetData" :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </template>
    <el-table :data="table.data" border stripe w-full>
      <el-table-column type="index" width="50"></el-table-column>
      <el-table-column type="expand">
        <template #default="{ row }">
          <el-table :data="row.subscribers" border :row-class-name="tableRowClassName">
            <el-table-column label="订阅时间" prop="createdAt" />
            <el-table-column label="终端ID / SIM" prop="sim" />
            <el-table-column label="Desc" prop="desc" />
            <el-table-column
              label="Metadata"
              :show-overflow-tooltip="{
                effect: 'light',
                popperClass: 'tip-content'
              }"
            >
              <template #default="{ row }">
                {{ JSON.stringify(row.metadata) }}
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
              <template #default="{ row }">
                <el-link type="primary" @click="closeSubscribe(row)" :disabled="row.closed">{{
                  row.closed ? '已关闭' : '关闭订阅'
                }}</el-link>
              </template>
            </el-table-column>
          </el-table>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="SubscriberId" width="180"></el-table-column>
      <el-table-column prop="lastCommunicateTime" label="最近一次通信时间"></el-table-column>
      <el-table-column prop="channel" label="通道">
        <template #default="{ row }">
          {{ channelConfig.find((e) => e.channel === row.channel)?.location }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="{ row }">
          <el-link type="primary" @click="closeSessions(row)" :disabled="row.closed">{{
            row.closed ? '已关闭' : '关闭通道'
          }}</el-link>
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
.tip-content {
  width: 500px;
}
.ep-table .warning-row {
  --ep-table-tr-bg-color: var(--ep-color-warning-light-9);
}
</style>
