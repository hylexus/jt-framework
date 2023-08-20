<template>
  <div class="box">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>808服务实例: <router-link type="primary" :to="'/808-instance/'+config.instanceId+'/session-list/'">{{ config.instanceId }}</router-link></span>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="InstanceId">
          <router-link type="primary" :to="'/808-instance/'+config.instanceId+'/session-list/'">{{ config.instanceId }}</router-link>
        </el-descriptions-item>
        <el-descriptions-item label="Host">{{ config.host }}</el-descriptions-item>
        <el-descriptions-item label="Secure">{{ config.secure }}</el-descriptions-item>
        <el-descriptions-item label="HttpPort" :span="2">{{ config.httpPort }}</el-descriptions-item>
        <el-descriptions-item label="Notes">{{ config.notes }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>
<script>
export default {
  name: 'Jt808ServerInstanceBox',
  props: {
    config: {
      instanceId: undefined,
      host: undefined,
      httpPort: undefined,
      secure: false,
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
