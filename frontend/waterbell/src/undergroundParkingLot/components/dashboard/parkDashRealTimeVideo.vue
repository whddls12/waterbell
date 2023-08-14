<template>
  <div class="container">
    <div class="dash-box">
      <div class="dash-box-title">
        <i class="fas fa-video"></i>
        <h3>CCTV</h3>
      </div>
      <div class="dash-box-content"></div>
    </div>
  </div>
</template>
<script>
import { defineComponent, onMounted, onBeforeUnmount } from 'vue'
import webSocket1 from '@/types/webSocket_cam1'
import webSocket2 from '@/types/webSocket_cam2'
export default defineComponent({
  name: 'parkDashCctvVue',
  setup() {
    onMounted(() => {
      // webSocket1.connectWebSocket()
      // webSocket2.connectWebSocket()
    })
    onBeforeUnmount(() => {
      // webSocket1.closeWebSocket()
      // webSocket2.closeWebSocket()
    })
  }
})
</script>
<style lang=""></style>
