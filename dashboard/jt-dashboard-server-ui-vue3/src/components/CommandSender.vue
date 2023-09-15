<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import Config from '@/assets/json/jt1078-config'
import { sendMsg9101, sendMsg9102 } from '@/api/jt808-api'
import { ElMessage } from 'element-plus'

const props = withDefaults(
  defineProps<{
    sim: string
  }>(),
  {
    sim: ''
  }
)

const terminalId = computed({
  get: () => props.sim,
  set: () => {}
})
const mediaTypeToClose = ref(Config.mediaTypeToClose)
const dataType = ref(Config.dataType)
const commandType = ref(Config.commandType)
const channelConfig = ref(Config.channelConfig)
const videoStreamType = ref(0)
const payload9101 = reactive({
  jt1078ServerIp: '1.1.1.1',
  jt1078ServerPortTcp: 61078,
  timeout: 3,
  jt1078ServerPortUdp: 0,
  channelNumber: 2,
  dataType: 0
})
const payload9102 = reactive({
  channelNumber: 2,
  command: 0,
  mediaTypeToClose: 0,
  timeout: 3
})
const tmp = reactive({
  response9101: null,
  response9102: null,
  command9101Loading: false,
  command9102Loading: false
})

const doSendMsg9101 = async () => {
  tmp.command9101Loading = true
  try {
    const resp = await sendMsg9101({
      ...payload9101,
      sim: terminalId.value,
      streamType: videoStreamType.value
    })
    tmp.response9101 = resp.data
    tmp.command9101Loading = false
  } catch (e: any) {
    tmp.command9101Loading = false
    tmp.response9101 = e
    ElMessage.error(e)
  }
}
const doSendMsg9102 = async () => {
  tmp.command9102Loading = true
  try {
    const resp = await sendMsg9102({
      ...payload9102,
      sim: terminalId.value,
      streamType: videoStreamType.value
    })
    tmp.response9102 = resp.data
    tmp.command9102Loading = false
  } catch (e: any) {
    tmp.command9102Loading = false
    tmp.response9102 = e
    ElMessage.error(e)
  }
}
</script>
<template>
  <el-row :gutter="10">
    <el-col :span="12">
      <el-card class="box-card">
        <template #header>
          <span>0x9101(音视频实时传输请求)</span>
        </template>
        <el-form size="small" inline label-width="140px">
          <el-form-item label="SIM">
            <el-input type="text" v-model="terminalId" />
          </el-form-item>
          <el-form-item label="1078服务IP">
            <el-input type="text" v-model="payload9101.jt1078ServerIp" />
          </el-form-item>
          <el-form-item label="1078服务端口(TCP)">
            <el-input-number :controls="false" v-model="payload9101.jt1078ServerPortTcp" />
          </el-form-item>
          <el-form-item label="1078服务端口(UDP)">
            <el-input-number :controls="false" v-model="payload9101.jt1078ServerPortUdp" />
          </el-form-item>
          <el-form-item label="逻辑通道号">
            <el-select
              v-model="payload9101.channelNumber"
              clearable
              allow-create
              filterable
              placeholder="请选择"
            >
              <el-option
                v-for="item in channelConfig"
                :key="item.channel"
                :label="item.title"
                :value="item.channel"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="数据类型">
            <el-select
              v-model="payload9101.dataType"
              clearable
              allow-create
              filterable
              placeholder="请选择"
            >
              <el-option
                v-for="item in dataType"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="码流类型">
            <el-radio-group v-model="videoStreamType" size="small">
              <el-radio-button :label="0">主码流</el-radio-button>
              <el-radio-button :label="1">子码流</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="超时时间(秒)">
            <el-input-number :controls="false" v-model="payload9101.timeout" />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              plain
              :loading="tmp.command9101Loading"
              @click="doSendMsg9101"
            >
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
      <el-card class="box-card">
        <template>
          <span>0x9102(音视频实时传输控制)</span>
        </template>
        <el-form size="small" inline label-width="120px">
          <el-form-item label="SIM">
            <el-input v-model="terminalId" />
          </el-form-item>
          <el-form-item label="逻辑通道号">
            <el-select
              v-model="payload9102.channelNumber"
              clearable
              allow-create
              filterable
              placeholder="请选择"
            >
              <el-option
                v-for="item in channelConfig"
                :key="item.channel"
                :label="item.title"
                :value="item.channel"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="控制指令">
            <el-select
              v-model="payload9102.command"
              clearable
              allow-create
              filterable
              placeholder="请选择"
            >
              <el-option
                v-for="item in commandType"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="关闭音视频类型">
            <el-select
              v-model="payload9102.mediaTypeToClose"
              clearable
              allow-create
              filterable
              placeholder="请选择"
            >
              <el-option
                v-for="item in mediaTypeToClose"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="切换码流类型">
            <el-radio-group v-model="videoStreamType" size="small" border>
              <el-radio-button :label="0">主码流</el-radio-button>
              <el-radio-button :label="1">子码流</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="超时时间(秒)">
            <el-input-number :controls="false" v-model="payload9102.timeout" />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              plain
              @click="doSendMsg9102"
              :loading="tmp.command9102Loading === true"
              >下发指令</el-button
            >
          </el-form-item>
          <div>
            {{ tmp.response9102 }}
          </div>
        </el-form>
      </el-card>
    </el-col>
  </el-row>
</template>
