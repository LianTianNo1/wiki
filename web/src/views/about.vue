<template>
  <div class="about">
    <a-layout-content
      :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <h1>这里是诺达希尔之叶，术士， 十区影之哀伤</h1>
      <a-image class="my-img" src="/WoWScrnShot_112319_233849.jpg" />
    </a-layout-content>
  </div>
</template>
<script lang="ts">
  import { defineComponent } from 'vue';

  export default defineComponent({
    name: 'about'
  });
</script>
<style>
  .my-img {
    width: 90%;
  }
</style>
