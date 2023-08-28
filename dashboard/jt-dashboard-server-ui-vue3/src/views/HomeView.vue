<template>
  <nav>
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item>Dashboard</el-breadcrumb-item>
    </el-breadcrumb>
  </nav>
  <div>
    <h3>808 Server Instance</h3>
    <el-row :gutter="20">
      <el-col :span="12" v-for="(it, index) in serverMetadata.jt808ServerMetadata" :key="index">
        <jt808-server-instance-box :config="it"></jt808-server-instance-box>
      </el-col>
    </el-row>
    <h3>1078 Server Instance</h3>
    <el-row :gutter="20">
      <el-col :span="12" v-for="(it, index) in serverMetadata.jt1078ServerMetadata" :key="index">
        <jt1078-server-instance-box :config="it"></jt1078-server-instance-box>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { requestServerInstances } from '@/api/dashboard'
import { isSuccess } from '@/utils/common-utils'
import Jt808ServerInstanceBox from '@/components/Jt808ServerInstanceBox.vue'
import Jt1078ServerInstanceBox from '@/components/Jt1078ServerInstanceBox.vue'

export default {
  name: 'HomeView',
  components: {
    Jt808ServerInstanceBox,
    Jt1078ServerInstanceBox
  },
  data() {
    return {
      serverMetadata: {
        jt808ServerMetadata: [],
        jt1078ServerMetadata: []
      }
    }
  },
  mounted() {
    this.loadServerMetadata()
  },
  methods: {
    loadServerMetadata() {
      requestServerInstances({}).then((resp) => {
        if (isSuccess(resp)) {
          this.serverMetadata = { ...resp.data }
        } else {
          this.$message.error(resp.msg)
        }
      })
    }
  }
}
</script>
