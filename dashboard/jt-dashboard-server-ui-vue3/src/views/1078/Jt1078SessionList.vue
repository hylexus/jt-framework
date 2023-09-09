<script setup lang="ts">
import { onMounted, reactive } from 'vue'
import { requestTerminalList } from '@/api/jt1078-api'
import * as CommonUtils from '@/utils/common-utils'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { channelConfig } from '@/assets/json/jt1078-config'
const route = useRoute()
const router = useRouter()
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
  query.rows = pageSize
  reloadData()
}
const videoButtonClick = (row: any) => {
  // const targetUrl = this.$router.resolve({
  //   path: '/PlayerDemo01',
  //   query: {
  //     terminalId: row.terminalId
  //   }
  // })
  // window.open(targetUrl.href, '_blank')

  // this.$router.push({name: 'PlayerDemo01', query: {terminalId: row.terminalId}})
  router.push({
    path: '/808-instance/' + query.instanceId + '/video-player/' + row.terminalId
    // query: {
    //   terminalId: row.terminalId,
    //   instanceId: row.instanceId
    // }
  })
}
</script>
<template>
  <nav>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item to="/">Dashboard</el-breadcrumb-item>
      <el-breadcrumb-item>1078SessionList</el-breadcrumb-item>
      <el-breadcrumb-item>{{ query.instanceId }}11</el-breadcrumb-item>
    </el-breadcrumb>
  </nav>
  <el-card class="box-card terminal-list-box" style="border-radius: 10px">
    <template #header>
      <el-form inline size="small" @submit.prevent>
        <el-form-item label="sim">
          <el-input v-model="query.sim" placeholder="sim" @keyup.enter="reloadData"></el-input>
        </el-form-item>
        <!--        <el-form-item label="版本">-->
        <!--          <el-radio-group v-model="query.version" @change="reloadData">-->
        <!--            <el-radio-button label="all">All</el-radio-button>-->
        <!--            <el-radio-button label="2019">V2019</el-radio-button>-->
        <!--            <el-radio-button label="2013">V2013</el-radio-button>-->
        <!--          </el-radio-group>-->
        <!--        </el-form-item>-->
        <el-form-item>
          <el-button type="primary" @click="reloadData" icon="el-icon-search">查询</el-button>
          <el-button @click="resetData" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </template>
    <el-table :data="table.data" border stripe style="width: 100%">
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column type="expand">
        <template #default="props">
          <el-table :data="props.row.subscribers" border>
            <el-table-column prop="channel" label="通道">
              <template #default="scope">
                {{ channelConfig.find((e) => e.channel === scope.row.channel)?.location }}
              </template>
            </el-table-column>
            <el-table-column label="订阅时间" prop="createdAt" />
            <el-table-column label="sim" prop="sim" />
            <el-table-column label="desc" prop="desc" />
          </el-table>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="id" width="180"> </el-table-column>
      <!--      <el-table-column prop="version" label="协议版本" width="100" filter-placement="bottom-end">-->
      <!--        <template #default="scope">-->
      <!--          <el-tag :type="scope.row.version === '2019' ? '' : 'success'" disable-transitions-->
      <!--            >{{ scope.row.version }}-->
      <!--          </el-tag>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
      <el-table-column prop="lastCommunicateTime" label="最近一次通信时间"> </el-table-column>
      <!--      <el-table-column prop="latestGeo" label="最近一次地理位置"> </el-table-column>-->
      <!--      <el-table-column fixed="right" label="操作" width="100">-->
      <!--        <template #default="scope">-->
      <!--          <el-link type="primary" @click="videoButtonClick(scope.row)">音视频</el-link>-->
      <!--          &lt;!&ndash;              <router-link target="_blank" :to="{path:'/player-demo01',query:{terminalId: scope.row.terminalId}}">toxxx</router-link>&ndash;&gt;-->
      <!--        </template>-->
      <!--      </el-table-column>-->
    </el-table>
    <el-pagination
      class="m-t-2"
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
