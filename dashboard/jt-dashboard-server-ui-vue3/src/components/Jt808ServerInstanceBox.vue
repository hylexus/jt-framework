<script setup lang="ts">
import { Config } from '@/components/props'
import { ArrowRight } from '@element-plus/icons-vue'
defineProps<{
  config: Config
}>()
</script>
<template>
  <el-card class="box-card" :class="config.status.status">
    <template #header>
      <span
        >808服务实例:
        <router-link type="primary" :to="`/808-instance/${config.instanceId}/session-list/`">{{
          config.instanceId
        }}</router-link></span
      >
    </template>
    <el-descriptions :column="2" border>
      <el-descriptions-item label="InstanceId">
        <router-link type="primary" :to="`/808-instance/${config.instanceId}/session-list/`">{{
          config.instanceId
        }}</router-link>
      </el-descriptions-item>
      <el-descriptions-item label="Name">{{ config.registration?.name }}</el-descriptions-item>
      <el-descriptions-item label="BaseUrl">{{
        config.registration?.baseUrl
      }}</el-descriptions-item>
      <el-descriptions-item label="Source">{{ config.registration?.source }}</el-descriptions-item>
      <el-descriptions-item label="Metadata">{{
        config.registration?.metadata
      }}</el-descriptions-item>
      <el-descriptions-item label="Request" v-if="config.metrics?.requests">
        <el-statistic title="Total" :value="config.metrics.requests.total">
          <template #suffix>
            <el-popover placement="right" :width="400" trigger="click">
              <template #reference>
                <el-icon><ArrowRight /></el-icon>
              </template>
              <el-table
                :data="
                  Object.keys(config.metrics.requests.details).map(
                    (key) => config.metrics.requests.details[key]
                  )
                "
              >
                <el-table-column width="150" property="msgIdAsHexString" label="msgId" />
                <el-table-column width="100" property="count" label="count" />
                <el-table-column width="300" property="desc" label="desc" />
              </el-table> </el-popover
          ></template>
        </el-statistic>
      </el-descriptions-item>
      <el-descriptions-item label="Sessions" v-if="config.metrics?.sessions">
        <el-row>
          <el-col :span="24">
            <el-statistic title="sessions" :value="config.metrics.sessions.current">
              <template #prefix><span style="font-size: 12px">current</span></template>
              <template #suffix
                >/ {{ config.metrics.sessions.max }}
                <span style="font-size: 12px">max</span></template
              >
            </el-statistic>
          </el-col>
        </el-row>
      </el-descriptions-item>
      <el-descriptions-item label="CreateAt">{{ config.createdAt }}</el-descriptions-item>
      <el-descriptions-item label="UpdateAt">{{ config.status?.updatedAt }}</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>
