<template lang="">
  <div class="container">
    <div>cctv</div>

    <div>
      <div><img id="cctv1" /></div>
    </div>
    <div>
      <div><img id="cctv2" /></div>
    </div>
  </div>
</template>
<script>
<<<<<<< HEAD
import { defineComponent, onMounted, onBeforeUnmount } from 'vue'
import webSocket1 from '@/types/webSocket_cam1'
import webSocket2 from '@/types/webSocket_cam2'
export default defineComponent({
  name: 'roadDashCctvVue',
  setup() {
    onMounted(() => {
      webSocket1.connectWebSocket()
      webSocket2.connectWebSocket()
    })
    onBeforeUnmount(() => {
      webSocket1.closeWebSocket()
      webSocket2.closeWebSocket()
    })
  }
=======
import { defineComponent } from 'vue'
// import webSocket1 from '@/types/webSocket_cam1'
// import webSocket2 from '@/types/webSocket_cam2'
export default defineComponent({
  name: 'roadDashCctvVue'
  // setup() {
  //   onMounted(() => {
  //     webSocket1.connectWebSocket()
  //     webSocket2.connectWebSocket()
  //   })
  //   beforeUnmount(() => {
  //     webSocket1.closeWebSocket()
  //     webSocket2.closeWebSocket()
  //   })
  // }
>>>>>>> 2283e66c8a57924054a6e520a5c438f017cf126e
})
</script>
<style lang="css">
#dash-cctv {
  height: 100%; /* set height to 100% */
  display: flex;
  flex-direction: column;
}
</style>
