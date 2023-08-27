<template>
  <div>
    <el-row :gutter="10">
      <el-col :span="12">
        <el-card class="box-card my-box">
          <div slot="header" class="clearfix">
            <span>0x9101(音视频实时传输请求)</span>
          </div>
          <el-form size="mini" inline label-width="140px">
            <el-form-item label="SIM">
              <el-input type="text" v-model="terminalId"/>
            </el-form-item>
            <el-form-item label="1078服务IP">
              <el-input type="text" v-model="payload9101.jt1078ServerIp"/>
            </el-form-item>
            <el-form-item label="1078服务端口(TCP)">
              <el-input-number :controls="false" v-model="payload9101.jt1078ServerPortTcp"/>
            </el-form-item>
            <el-form-item label="1078服务端口(UDP)">
              <el-input-number :controls="false" v-model="payload9101.jt1078ServerPortUdp"/>
            </el-form-item>
            <el-form-item label="逻辑通道号">
              <el-select v-model="payload9101.channelNumber"
                         clearable
                         allow-create
                         filterable
                         placeholder="请选择">
                <el-option
                  v-for="item in channelConfig()"
                  :key="item.channel"
                  :label="item.title"
                  :value="item.channel">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="数据类型">
              <el-select v-model="payload9101.dataType"
                         clearable
                         allow-create
                         filterable
                         placeholder="请选择">
                <el-option
                  v-for="item in dataType()"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="码流类型">
              <el-radio-group v-model="videoStreamType" size="mini">
                <el-radio-button :label="0">主码流</el-radio-button>
                <el-radio-button :label="1">子码流</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="超时时间(秒)">
              <el-input-number :controls="false" v-model="payload9101.timeout"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" plain
                         :disabled="autoSend9101Command === true"
                         :loading="tmp.command9101Loading === true"
                         @click="doSendMsg9101">
                下发指令
              </el-button>
            </el-form-item>
            <div>
              {{ tmp.response9101 }}
            </div>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card my-box">
          <div slot="header" class="clearfix">
            <span>0x9102(音视频实时传输控制)</span>
          </div>
          <el-form size="mini" inline label-width="120px">
            <el-form-item label="SIM">
              <el-input v-model="terminalId"/>
            </el-form-item>
            <el-form-item label="逻辑通道号">
              <el-select v-model="payload9102.channelNumber"
                         clearable
                         allow-create
                         filterable
                         placeholder="请选择">
                <el-option
                  v-for="item in channelConfig()"
                  :key="item.channel"
                  :label="item.title"
                  :value="item.channel">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="控制指令">
              <el-select v-model="payload9102.command"
                         clearable
                         allow-create
                         filterable
                         placeholder="请选择">
                <el-option
                  v-for="item in commandType()"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="关闭音视频类型">
              <el-select v-model="payload9102.mediaTypeToClose"
                         clearable
                         allow-create
                         filterable
                         placeholder="请选择">
                <el-option
                  v-for="item in mediaTypeToClose()"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="切换码流类型">
              <el-radio-group v-model="videoStreamType" size="mini" border>
                <el-radio-button :label="0">主码流</el-radio-button>
                <el-radio-button :label="1">子码流</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="超时时间(秒)">
              <el-input-number :controls="false" v-model="payload9102.timeout"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" plain @click="doSendMsg9102" :loading="tmp.command9102Loading === true">下发指令</el-button>
            </el-form-item>
            <div>
              {{ tmp.response9102 }}
            </div>
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { channelConfig, commandType, dataType, mediaTypeToClose } from '@/assets/json/jt1078-config'
import { sendMsg9101, sendMsg9102 } from '@/api/jt808-api'

export default {
  name: 'CommandSender',
  props: {
    sim: {type: String, required: true},
  },
  watch: {
    sim: function (val, oldVal) {
      this.terminalId = val
    }
  },
  data() {
    return {
      terminalId: this.sim,
      player: undefined,
      videoStreamType: 0,
      autoSend9101Command: false,
      payload9101: {
        // sim: this.sim
        // streamType: this.videoStreamType
        jt1078ServerIp: '1.1.1.1',
        jt1078ServerPortTcp: 61078,
        timeout: 3,
        jt1078ServerPortUdp: 0,
        channelNumber: 2,
        dataType: 0,
      },
      payload9102: {
        // sim: this.sim
        // streamType: this.videoStreamType
        channelNumber: 2,
        command: 0,
        mediaTypeToClose: 0,
        timeout: 3
      },
      tmp: {
        response9101: undefined,
        response9102: undefined,
        command9101Loading: false,
        command9102Loading: false,
      },
    }
  },
  methods: {
    doSendMsg9101() {
      this.tmp.command9101Loading = true
      sendMsg9101({
        ...this.payload9101,
        sim: this.terminalId,
        streamType: this.videoStreamType,
      }).then(resp => {
        this.tmp.response9101 = resp.data
        this.tmp.command9101Loading = false
      }).catch(e => {
        this.tmp.command9101Loading = false
        console.log(e)
        this.tmp.response9101 = e
        this.$message.error(e)
      })
    },
    doSendMsg9102() {
      this.tmp.command9102Loading = true
      sendMsg9102({
        ...this.payload9102,
        sim: this.terminalId,
        streamType: this.videoStreamType,
      }).then(resp => {
        this.tmp.response9102 = resp.data
        this.tmp.command9102Loading = false
      }).catch(e => {
        this.tmp.command9102Loading = false
        console.log(e)
        this.tmp.response9102 = e
        this.$message.error(e)
      })
    },
    mediaTypeToClose() {
      return mediaTypeToClose
    },
    dataType() {
      return dataType
    },
    commandType() {
      return commandType
    },
    channelConfig() {
      return channelConfig
    },
  }
}
</script>
