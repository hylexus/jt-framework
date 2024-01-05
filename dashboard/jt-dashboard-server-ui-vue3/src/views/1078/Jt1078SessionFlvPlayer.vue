<script setup lang="ts">
import { reactive, ref } from 'vue'
import FlvPlayer from '@/components/FlvPlayer.vue'
import Config from '@/assets/json/jt1078-config'
import * as CommonUtils from '@/utils/common-utils'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { ArrowRight } from '@element-plus/icons-vue'
import {AudioHints} from "@/components/props";

const route = useRoute()
const instanceId = ref(route.params.instanceId)
const terminalId = ref(route.params.sim)
let playerConfigs = reactive<any[]>([])
const players = ref<InstanceType<typeof FlvPlayer>[]>([])
const playAll = () => {
  players.value.forEach((player) => {
    player?.play()
  })
}

const ignoreAudioData = () => {
  playerConfigs.forEach((config) => {
    console.log(config)
    config.audioHints = AudioHints.SILENCE
    console.log(config)
  })
}
const initConfigs = () => {
  if (CommonUtils.isEmpty(terminalId.value)) {
    ElMessage.error('terminalId is null or empty')
    return
  }
  playerConfigs = Config.channelConfig.map((it) => ({
    ...it,
    sim: terminalId.value,
    refId: 'ref-' + it.channel,
    dataType: 0,
    jt808ServerInstanceId: instanceId.value,
    audioHints: AudioHints.ADPCM_IMA_MONO,
  }))
}
initConfigs()
</script>
<template>
  <nav p4>
    <el-breadcrumb :separator-icon="ArrowRight">
      <el-breadcrumb-item to="/">Dashboard</el-breadcrumb-item>
      <el-breadcrumb-item>808ServerInstance</el-breadcrumb-item>
      <el-breadcrumb-item :to="{ name: 'Jt808SessionList', params: { instanceId: instanceId } }">
        {{ instanceId }}</el-breadcrumb-item
      >
      <el-breadcrumb-item>VideoPlayer</el-breadcrumb-item>
      <el-breadcrumb-item>{{ terminalId }}</el-breadcrumb-item>
    </el-breadcrumb>
  </nav>
  <div mt mb>
    <el-button @click="playAll" size="small">播放全部</el-button>
    <el-button @click="ignoreAudioData" size="small">忽略音频数据</el-button>
  </div>
  <el-row :gutter="10" pr8 pl8>
    <el-col mt8 :span="6" v-for="(it, index) in playerConfigs" :key="index">
      <FlvPlayer :config="it" ref="players" />
    </el-col>
  </el-row>
</template>
