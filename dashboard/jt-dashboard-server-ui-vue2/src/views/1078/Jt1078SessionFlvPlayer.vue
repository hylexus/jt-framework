<template>
  <div>
    <nav>
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item :to="{ path: '/' }">Dashboard</el-breadcrumb-item>
        <el-breadcrumb-item>808ServerInstance</el-breadcrumb-item>
        <el-breadcrumb-item :to="{ name: 'Jt808SessionList',params:{'instanceId':this.instanceId} }"> {{ this.instanceId }}</el-breadcrumb-item>
        <el-breadcrumb-item>VideoPlayer</el-breadcrumb-item>
        <el-breadcrumb-item>{{ this.terminalId }}</el-breadcrumb-item>
      </el-breadcrumb>
    </nav>
    <div>
      <el-button @click="playAll" size="mini">播放全部</el-button>
    </div>
    <el-row :gutter="10">
      <el-col :span="6" v-for="(it,index) in playerConfigs" :key="index">
        <flv-player :config="it" :ref="it.refId"></flv-player>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import FlvPlayer from '@/components/FlvPlayer.vue'
import { channelConfig } from '@/assets/json/jt1078-config'
import commonUtils from '@/utils/common-utils'

export default {
  name: 'HomeView',
  components: {FlvPlayer},
  data() {
    return {
      instanceId: undefined,
      terminalId: undefined,
      playerConfigs: [],
    }
  },
  created() {
    this.initConfigs()
  },
  methods: {
    playAll() {
      this.playerConfigs.forEach(it => {
        this.$refs[it.refId][0].play()
      })
    },
    initConfigs() {
      this.terminalId = this.$route.params.sim
      this.instanceId = this.$route.params.instanceId
      if (commonUtils.isEmpty(this.terminalId)) {
        this.$message.error('terminalId is null or empty')
        return
      }
      this.playerConfigs = channelConfig.map(it => {
        return {
          ...it,
          sim: this.terminalId,
          refId: 'ref-' + it.channel,
          dataType: 0,
          jt808ServerInstanceId: this.instanceId
        }
      })
    },
  }
}
</script>
