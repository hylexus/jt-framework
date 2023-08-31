<script setup lang="ts">
import { Config } from './props'

defineProps<{
  config: Config
}>()
</script>
<template>
  <el-card class="box-card">
    <template #header>
      <span>1078服务实例: {{ config.instanceId }}</span>
    </template>
    <el-descriptions :column="2" border>
      <el-descriptions-item label="InstanceId">
        <router-link type="primary" :to="'/808-instance/' + config.instanceId + '/session-list/'"
          >{{ config.instanceId }}
        </router-link>
      </el-descriptions-item>
      <el-descriptions-item label="Name">{{ config.registration?.name }}</el-descriptions-item>
      <el-descriptions-item label="BaseUrl">{{ config.registration?.baseUrl }}</el-descriptions-item>
      <el-descriptions-item label="Source">{{ config.registration?.source }}</el-descriptions-item>
      <el-descriptions-item label="Host">{{ config.registration?.host }}</el-descriptions-item>
      <el-descriptions-item label="TcpPort">{{ config.registration?.tcpPort }}</el-descriptions-item>
      <el-descriptions-item label="Metadata"
        >{{ config.registration?.metadata }}
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>
