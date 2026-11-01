<!--
Portions correspond to element-plus-vite-starter.
Comparison snapshot: https://github.com/element-plus/element-plus-vite-starter/blob/fa10f32788a09b2faab73d3f9709f33bc7cf4d3f/
Local modifications by hylexus; source attribution restored 2026-10-02.
Upstream package metadata declares MIT; complete original notice remains
unresolved. See licenses/element-plus-vite-starter-NOTICE.txt in the repository.
-->

<template>
  <el-config-provider namespace="ep">
    <BaseHeader />
    <div class="flex main-container">
      <!--      <BaseSide />-->
      <div w="full" py="4">
        <RouterView />
      </div>
    </div>
  </el-config-provider>
</template>

<style>
#app {
  text-align: center;
  color: var(--ep-text-color-primary);
}

.main-container {
  height: calc(100vh - var(--ep-menu-item-height) - 3px);
}
</style>
