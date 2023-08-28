<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>1078服务实例: {{ config.instanceId }}</span>
    </div>
    <el-descriptions :column="2" border>
      <el-descriptions-item label="InstanceId">
        <router-link type="primary" :to="'/808-instance/' + config.instanceId + '/session-list/'">{{
          config.instanceId
        }}</router-link>
      </el-descriptions-item>
      <el-descriptions-item label="Name">{{ config.registration.name }}</el-descriptions-item>
      <el-descriptions-item label="BaseUrl">{{ config.registration.baseUrl }}</el-descriptions-item>
      <el-descriptions-item label="Source">{{ config.registration.source }}</el-descriptions-item>
      <el-descriptions-item label="Host">{{ config.registration.host }}</el-descriptions-item>
      <el-descriptions-item label="TcpPort">{{ config.registration.tcpPort }}</el-descriptions-item>
      <el-descriptions-item label="Metadata">{{
        config.registration.metadata
      }}</el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>
<script>
export default {
  name: 'Jt1078ServerInstanceBox',
  props: {
    config: {
      instanceId: undefined,
      host: undefined,
      httpPort: undefined,
      tcpPort: undefined,
      notes: ''
    }
  },
  data() {
    return {}
  }
}
</script>

<style></style>
