<template>
  <div class="container">
    <div class="dash-box">
      <div class="dash-box-title">
        <h3>CCTV</h3>
      </div>
      <div class="dash-box-content">
        <div>
          <img id="cctv1" />
        </div>
        <div>
          <img id="cctv2" />
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { defineComponent, onMounted, onBeforeUnmount } from 'vue'
import webSocket1 from '@/types/webSocket_cam1'
<<<<<<< HEAD
// import webSocket2 from '@/types/webSocket_cam2'
=======
import webSocket2 from '@/types/webSocket_cam2'
>>>>>>> b986d59e90d30b970c460ab629643b986ef49e74
export default defineComponent({
  name: 'roadDashCctvVue',
  setup() {
    onMounted(() => {
      webSocket1.connectWebSocket()
<<<<<<< HEAD
      // webSocket2.connectWebSocket()
    })
    onBeforeUnmount(() => {
      webSocket1.closeWebSocket()
      // webSocket2.closeWebSocket()
=======
      webSocket2.connectWebSocket()
    })
    onBeforeUnmount(() => {
      webSocket1.closeWebSocket()
      webSocket2.closeWebSocket()
>>>>>>> b986d59e90d30b970c460ab629643b986ef49e74
    })
  }
})
</script>
<style lang="css">
#dash-cctv {
  height: 100%; /* set height to 100% */
  display: flex;
  flex-direction: column;
}
</style>
