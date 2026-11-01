<!--
Portions correspond to element-plus-vite-starter.
Comparison snapshot: https://github.com/element-plus/element-plus-vite-starter/blob/fa10f32788a09b2faab73d3f9709f33bc7cf4d3f/
Local modifications by hylexus; source attribution restored 2026-10-02.
Upstream package metadata declares MIT; complete original notice remains
unresolved. See licenses/element-plus-vite-starter-NOTICE.txt in the repository.
-->

<script lang="ts" setup>
import { toggleDark } from '@/composables'
</script>

<template>
  <el-menu class="el-menu-demo" mode="horizontal">
    <el-menu-item index="1">JT-Dashboard</el-menu-item>
    <el-menu-item h="full" @click="toggleDark()">
      <button
        class="border-none w-full bg-transparent cursor-pointer"
        style="height: var(--ep-menu-item-height)"
      >
        <i inline-flex i="dark:ep-moon ep-sunny" />
      </button>
    </el-menu-item>
  </el-menu>
</template>
