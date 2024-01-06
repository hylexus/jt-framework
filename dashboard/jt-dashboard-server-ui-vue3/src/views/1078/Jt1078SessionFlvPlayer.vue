<script setup lang="ts">
import { computed, ref } from 'vue'
import FlvPlayer from '@/components/FlvPlayer.vue'
import Config from '@/assets/json/jt1078-config'
import * as CommonUtils from '@/utils/common-utils'
import { ElMessage } from 'element-plus'
import { useRoute } from 'vue-router'
import { ArrowRight, InfoFilled } from '@element-plus/icons-vue'
import { AudioHints, FlvPlayerStatus } from '@/components/props'

const route = useRoute()
const instanceId = ref(route.params.instanceId)
const terminalId = ref(route.params.sim)
let playerConfigs = ref<any[]>([])
const players = ref<InstanceType<typeof FlvPlayer>[]>([])
const playAll = () => {
  players.value.forEach((player) => {
    player?.play()
  })
}

const checkboxDisabled = computed(() => players.value.filter(it => it.status !== FlvPlayerStatus.OFFLINE).length > 0)

const onIgnoreAudioOptionChange = (checked: (string | number | boolean)) => {
  if (checked) {
    playerConfigs.value.forEach(config => config.audioHints = AudioHints.SILENCE)
  } else {
    playerConfigs.value.forEach(config => config.audioHints = AudioHints.AUTO)
  }
}

const initConfigs = () => {
  if (CommonUtils.isEmpty(terminalId.value)) {
    ElMessage.error('terminalId is null or empty')
    return
  }
  playerConfigs.value = Config.channelConfig.map((it) => ({
    ...it,
    sim: terminalId.value,
    refId: 'ref-' + it.channel,
    dataType: 0,
    jt808ServerInstanceId: instanceId.value,
    audioHints: AudioHints.SILENCE
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
        {{ instanceId }}
      </el-breadcrumb-item
      >
      <el-breadcrumb-item>VideoPlayer</el-breadcrumb-item>
      <el-breadcrumb-item>{{ terminalId }}</el-breadcrumb-item>
    </el-breadcrumb>
  </nav>
  <el-form mt mb inline>
    <el-form-item>
      <el-button @click="playAll" size="small">播放全部</el-button>
    </el-form-item>
    <el-form-item>
      <el-checkbox checked border @change="onIgnoreAudioOptionChange" :disabled="checkboxDisabled" size="small">
        忽略音频数据&nbsp;
        <el-tooltip
          class="box-item"
          effect="dark"
          content="忽略码流中的音频数据(服务端转码时)"
          placement="top"
        >
          <el-icon>
            <InfoFilled />
          </el-icon>
        </el-tooltip>
      </el-checkbox>
    </el-form-item>
  </el-form>
  <el-row :gutter="10" pr8 pl8>
    <el-col mt8 :span="6" v-for="(it, index) in playerConfigs" :key="index">
      <FlvPlayer :config="it" ref="players" />
    </el-col>
  </el-row>
</template>
