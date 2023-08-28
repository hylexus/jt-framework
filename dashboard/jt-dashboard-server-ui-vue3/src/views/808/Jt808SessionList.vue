<template>
  <nav>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/' }">Dashboard</el-breadcrumb-item>
      <el-breadcrumb-item>808ServerInstance</el-breadcrumb-item>
      <el-breadcrumb-item>{{ this.query.instanceId }}11</el-breadcrumb-item>
    </el-breadcrumb>
  </nav>
  <el-card class="box-card terminal-list-box" style="border-radius: 10px">
    <div slot="header" class="clearfix">
      <el-form inline size="small" @submit.native.prevent>
        <el-form-item label="终端ID">
          <el-input
            v-model="query.terminalId"
            placeholder="terminalId"
            @keyup.enter.native="reloadData"
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
          <el-button type="primary" @click="reloadData" icon="el-icon-search">查询</el-button>
          <el-button @click="resetData" icon="el-icon-refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <el-table :data="table.data" border stripe style="width: 100%">
        <el-table-column type="index" width="50"> </el-table-column>
        <el-table-column prop="terminalId" label="终端ID" width="180"> </el-table-column>
        <el-table-column prop="version" label="协议版本" width="100" filter-placement="bottom-end">
          <template #default="scope">
            <el-tag :type="scope.row.version === '2019' ? '' : 'success'" disable-transitions
              >{{ scope.row.version }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="lastCommunicationTime" label="最近一次通信时间"> </el-table-column>
        <el-table-column prop="latestGeo" label="最近一次地理位置"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-link type="primary" @click="videoButtonClick(scope.row)">音视频</el-link>
            <!--              <router-link target="_blank" :to="{path:'/player-demo01',query:{terminalId: scope.row.terminalId}}">toxxx</router-link>-->
          </template>
        </el-table-column>
      </el-table>
      <div style="border: 1px solid #eee; border-top-width: 0; padding: 10px 0">
        <el-pagination
          :page-sizes="[5, 10, 20, 30, 50, 100, 200]"
          :current-page="query.page"
          :page-size="query.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="table.total"
          @pagination="table.data"
          @size-change="sizeChange"
          @current-change="currentChange"
        />
      </div>
    </div>
  </el-card>
</template>

<script>
import { requestTerminalList } from '@/api/jt808-api'
import * as CommonUtils from '@/utils/common-utils'

export default {
  name: 'Jt808TerminalList',
  data() {
    return {
      query: {
        instanceId: undefined,
        page: 1,
        pageSize: 5,
        terminalId: undefined,
        version: 'all'
      },
      table: {
        total: 0,
        data: []
      }
    }
  },
  created() {
    const id = this.$route.params && this.$route.params.instanceId
    const sim = this.$route.params && this.$route.params.sim
    if (!id) {
      this.$message.error('instanceId is empty')
    }
    this.query.instanceId = id
    this.query.terminalId = sim
  },
  mounted() {
    this.reloadData()
  },
  methods: {
    resetData() {
      this.query.version = 'all'
      this.query.terminalId = undefined
      this.reloadData()
    },
    reloadData() {
      requestTerminalList({ ...this.query })
        .then((resp) => {
          if (CommonUtils.isSuccess(resp)) {
            this.table.data = resp.data.records
            this.table.total = resp.data.total
          } else {
            console.log(resp)
            this.$message.error(resp.msg)
          }
        })
        .catch((e) => {
          this.$message.error(e)
          console.log(e)
        })
    },
    videoButtonClick(row) {
      // const targetUrl = this.$router.resolve({
      //   path: '/PlayerDemo01',
      //   query: {
      //     terminalId: row.terminalId
      //   }
      // })
      // window.open(targetUrl.href, '_blank')

      // this.$router.push({name: 'PlayerDemo01', query: {terminalId: row.terminalId}})
      this.$router.push({
        path: '/808-instance/' + this.query.instanceId + '/video-player/' + row.terminalId
        // query: {
        //   terminalId: row.terminalId,
        //   instanceId: row.instanceId
        // }
      })
    },
    currentChange(currentPage) {
      this.query.page = currentPage
      this.reloadData()
    },
    sizeChange(pageSize) {
      this.query.rows = pageSize
      this.reloadData()
    }
  }
}
</script>

<style lang="scss">
.terminal-list-box {
  .ep-card__header {
    padding-bottom: 0;
  }
}
</style>
