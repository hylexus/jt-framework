<script setup lang="ts">
import { computed, ref } from 'vue'
import mpegts from 'mpegts.js'
import { requestPlayerUrl } from '@/api/dashboard'
import * as CommonUtils from '@/utils/common-utils'
import { Refresh, VideoPause, VideoPlay } from '@element-plus/icons-vue'

enum STATUS {
  OFFLINE = 'Offline',
  CONNECTING = 'Connecting',
  RUNNING = 'Running',
  PAUSE = 'Pause'
}
interface Config {
  title?: string
  sim?: string
  channel?: string
  dataType: number
  jt808ServerInstanceId?: string
  location?: string
  type: number
}
const props = defineProps<{
  config: Config
}>()
const video = ref<HTMLMediaElement>()
const player = ref<mpegts.Player>()
const remoteUrl = ref('')
const status = ref(STATUS.OFFLINE)
const errorTips = ref('')
const buttonCss = computed(() => {
  switch (status.value) {
    case STATUS.OFFLINE: {
      return {
        play: VideoPlay,
        status: 'status-cycle-offline',
        reset: Refresh
      }
    }
    case STATUS.CONNECTING: {
      return {
        play: VideoPause,
        status: 'status-cycle-tidying',
        reset: Refresh
      }
    }
    case STATUS.RUNNING: {
      return {
        play: VideoPause,
        status: 'status-cycle-online',
        reset: Refresh
      }
    }
    case STATUS.PAUSE: {
      return {
        play: VideoPlay,
        status: 'status-cycle-offline',
        reset: Refresh
      }
    }
    default: {
      return {
        play: VideoPlay,
        status: 'status-cycle-offline',
        reset: Refresh
      }
    }
  }
})
const streamDesc = (type: number) => {
  if (type === 1) {
    return '音视频'
  }
  return '音频'
}
const reset = () => {
  if (player.value !== undefined) {
    player.value?.pause()
    player.value?.unload()
    player.value?.detachMediaElement()
    player.value?.destroy()
    player.value = undefined
  }

  status.value = STATUS.OFFLINE
}
const play = async () => {
  if (status.value === STATUS.OFFLINE) {
    await initPlayer()
    status.value = STATUS.CONNECTING
    player.value
      ?.play()
      ?.then((r: string | void) => {
        if (typeof r === 'string') {
          errorTips.value = r
        }
      })
      .catch((e: string) => {
        errorTips.value = e
      })
  } else if (status.value === STATUS.CONNECTING) {
    // do nothing
  } else if (status.value === STATUS.RUNNING) {
    player.value?.pause()
    status.value = STATUS.PAUSE
  } else if (status.value === STATUS.PAUSE) {
    player.value
      ?.play()
      ?.then((r: string | void) => {
        if (typeof r === 'string') {
          errorTips.value = r
        }
      })
      .catch((e: string) => {
        errorTips.value = e
      })
    status.value = STATUS.RUNNING
  } else {
    errorTips.value = '未知状态: ' + status.value
  }
}
const initPlayer = async () => {
  if (player.value !== undefined) {
    return
  }

  await getPlayerUrl()

  player.value = mpegts.createPlayer(
    {
      isLive: true,
      type: 'flv',
      url: remoteUrl.value
      // enableWorker: true,
      // enableStashBuffer: false,
      // stashInitialSize: 128
    },
    {
      enableWorker: true
    }
  )
  if (video.value !== undefined) {
    player.value.attachMediaElement(video.value)
  }

  player.value.load()

  player.value.on(mpegts.Events.LOADING_COMPLETE, () => {
    status.value = STATUS.OFFLINE
    errorTips.value = '视频播放结束'
  })

  player.value.on(mpegts.Events.METADATA_ARRIVED, () => {
    status.value = STATUS.RUNNING
  })

  // this.player.on(mpegts.Events.MEDIA_INFO, (res) => {
  //   console.log('==> MEDIA_INFO')
  //   console.log(res)
  // })

  player.value.on(mpegts.Events.ERROR, (errorType, errorDetail, errorInfo) => {
    console.log('播放器异常')
    console.log('errorType:', errorType)
    console.log('errorDetail:', errorDetail)
    console.log('errorInfo:', errorInfo)
    errorTips.value = errorInfo
  })
}
const getPlayerUrl = async () => {
  const { sim, channel, dataType, jt808ServerInstanceId } = props.config
  return requestPlayerUrl({
    protocolType: 'WEBSOCKET',
    // protocolType: 'HTTP',
    streamType: 'MAIN_STREAM',
    sim,
    channelNumber: channel,
    dataType,
    instanceId: jt808ServerInstanceId
  }).then((resp: any) => {
    if (CommonUtils.isSuccess(resp)) {
      remoteUrl.value = resp.data.address
    } else {
      errorTips.value = resp.msg
    }
  })
}
defineExpose({ play })
</script>
<template>
  <div class="player-root">
    <el-card class="__card">
      <template #header>
        <div class="player-header">
          <!--        <span>{{ config.title }}</span>-->
          <div>
            <span ml8 mr8><span :class="buttonCss.status"></span></span>
            <span>通道{{ config.channel }}</span>
            <el-divider direction="vertical"></el-divider>
            <span>{{ config.location }}</span>
            <el-divider direction="vertical"></el-divider>
            <span>{{ streamDesc(config.type) }}</span>
          </div>
          <div>
            <el-button :icon="buttonCss.reset" circle @click="reset"></el-button>
            <el-button circle @click="play" :icon="buttonCss.play"></el-button>
          </div>
        </div>
      </template>
      <video
        ref="video"
        w-full
        style="height: 100%"
        width="100%"
        height="100%"
        controls
        autoplay
      ></video>
      {{ errorTips }}
    </el-card>
  </div>
</template>
<style lang="scss">
.player-root {
  .player-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .__card {
    height: 100%;

    .ep-card__header {
      margin: 0 0 5px 0;
      padding: 5px;
    }

    .ep-card__body {
      padding: 5px;
    }
  }

  .status-cycle-online {
    width: 10px;
    height: 10px;
    display: inline-block;
    background-color: #05ec05;
    border-radius: 50%;
  }

  .status-cycle-offline {
    width: 10px;
    height: 10px;
    display: inline-block;
    background-color: #e00c3d;
    border-radius: 50%;
  }

  .status-cycle-tidying {
    width: 10px;
    height: 10px;
    display: inline-block;
    background-color: #e4f602;
    border-radius: 50%;
  }
}
</style>
