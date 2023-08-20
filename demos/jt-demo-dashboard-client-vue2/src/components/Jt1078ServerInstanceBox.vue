<template>
  <div class="box">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>1078服务实例: {{ config.instanceId }}</span>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="InstanceId">{{ config.instanceId }}</el-descriptions-item>
        <el-descriptions-item label="Host">{{ config.host }}</el-descriptions-item>
        <el-descriptions-item label="HttpPort">{{ config.httpPort }}</el-descriptions-item>
        <el-descriptions-item label="TcpPort">{{ config.tcpPort }}</el-descriptions-item>
        <el-descriptions-item label="Notes">{{ config.notes }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
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
  },
}
</script>

<style>

</style>
