<template>
  <div class="player-root">
    <el-card class="__card">
      <template #header>
        <div class="clearfix">
          <!--        <span>{{ config.title }}</span>-->
          <span>通道{{ config.channel }}</span>
          <el-divider direction="vertical"></el-divider>
          <span>{{ config.location }}</span>
          <el-divider direction="vertical"></el-divider>
          <span>{{ streamDesc(config.type) }}</span>
          <el-button
            :icon="buttonCss.reset"
            style="float: right; margin: 0 5px"
            circle
            @click="reset"
          ></el-button>
          <el-button style="float: right" circle @click="play" :icon="buttonCss.play"></el-button>

          <span style="margin: 0 10px; float: left"><span :class="buttonCss.status"></span></span>
        </div>
      </template>
      <!-- https://github.com/xqq/mpegts.js/issues/37 -->
      <video
        :ref="config.refId"
        style="width: 100%; height: 100%"
        width="100%"
        height="100%"
        controls
        autoplay
      ></video>
      {{ errorTips }}
    </el-card>
  </div>
</template>

<script>
import mpegts from 'mpegts.js'
import { requestPlayerUrl } from '@/api/dashboard'
import * as CommonUtils from '@/utils/common-utils'
import { Refresh, VideoPause, VideoPlay } from '@element-plus/icons-vue'

const STATUS_OFFLINE = Symbol('Offline')
const STATUS_CONNECTING = Symbol('Connecting')
const STATUS_RUNNING = Symbol('Running')
const STATUS_PAUSE = Symbol('Pause')

export default {
  name: 'FlvPlayer',
  props: {
    config: {
      title: undefined,
      sim: undefined,
      channel: undefined,
      dataType: 0,
      jt808ServerInstanceId: undefined
    }
  },
  data() {
    return {
      player: undefined,
      remoteUrl: undefined,
      status: STATUS_OFFLINE,
      errorTips: ''
    }
  },
  computed: {
    buttonCss() {
      switch (this.status) {
        case STATUS_OFFLINE: {
          return {
            play: VideoPlay,
            status: 'status-cycle-offline',
            reset: Refresh
          }
        }
        case STATUS_CONNECTING: {
          return {
            play: VideoPause,
            status: 'status-cycle-tidying',
            reset: Refresh
          }
        }
        case STATUS_RUNNING: {
          return {
            play: VideoPause,
            status: 'status-cycle-online',
            reset: Refresh
          }
        }
        case STATUS_PAUSE: {
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
    }
  },
  methods: {
    streamDesc(type) {
      if (type === 1) {
        return '音视频'
      }
      return '音频'
    },
    reset() {
      if (this.player !== undefined && this.player !== null) {
        this.player.pause()
        this.player.unload()
        this.player.detachMediaElement()
        this.player.destroy()
        this.player = undefined
      }

      this.status = STATUS_OFFLINE
    },
    async play() {
      if (this.status === STATUS_OFFLINE) {
        await this.initPlayer()
        this.status = STATUS_CONNECTING
        this.player
          .play()
          .then((r) => {
            // console.log(r) // ignore
            this.errorTips = r
          })
          .catch((e) => {
            // console.log(e) // ignore
            this.errorTips = e
          })
      } else if (this.status === STATUS_CONNECTING) {
        // do nothing
      } else if (this.status === STATUS_RUNNING) {
        this.player.pause()
        this.status = STATUS_PAUSE
      } else if (this.status === STATUS_PAUSE) {
        this.player
          .play()
          .then((r) => {
            console.log(r) // ignore
            this.errorTips = r
          })
          .catch((e) => {
            console.log(e) // ignore
            this.errorTips = e
          })
        this.status = STATUS_RUNNING
      } else {
        this.errorTips = '未知状态: ' + this.status.toString()
      }
    },
    async initPlayer() {
      if (this.player !== undefined && this.player !== null) {
        return
      }

      await this.getPlayerUrl()

      this.player = mpegts.createPlayer(
        {
          isLive: true,
          type: 'flv',
          url: this.remoteUrl,
          enableWorker: true,
          enableStashBuffer: false,
          stashInitialSize: 128
        },
        {
          enableWorker: true
        }
      )
      console.log(this.$refs)
      const element = this.$refs[this.config.refId]
      this.player.attachMediaElement(element)
      this.player.load()

      this.player.on(mpegts.Events.LOADING_COMPLETE, () => {
        this.status = STATUS_OFFLINE
        this.errorTips = '视频播放结束'
      })

      this.player.on(mpegts.Events.METADATA_ARRIVED, () => {
        this.status = STATUS_RUNNING
      })

      // this.player.on(mpegts.Events.MEDIA_INFO, (res) => {
      //   console.log('==> MEDIA_INFO')
      //   console.log(res)
      // })

      this.player.on(mpegts.Events.ERROR, (errorType, errorDetail, errorInfo) => {
        console.log('播放器异常')
        console.log('errorType:', errorType)
        console.log('errorDetail:', errorDetail)
        console.log('errorInfo:', errorInfo)
        this.errorTips = errorInfo
      })
    },
    async getPlayerUrl() {
      return requestPlayerUrl({
        protocolType: 'WEBSOCKET',
        // protocolType: 'HTTP',
        streamType: 'MAIN_STREAM',
        sim: this.config.sim,
        channelNumber: this.config.channel,
        dataType: this.config.dataType,
        instanceId: this.config.jt808ServerInstanceId
      }).then((resp) => {
        if (CommonUtils.isSuccess(resp)) {
          this.remoteUrl = resp.data.address
        } else {
          this.errorTips = resp.msg
        }
      })
    }
  }
}
</script>

<style lang="scss">
.player-root {
  .__card {
    height: 100%;

    .ep-card__header {
      margin: 0 0 5px 0;
      padding: 5px;
    }

    .ep-card__body {
      width: 200px;
      height: 200px;
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
