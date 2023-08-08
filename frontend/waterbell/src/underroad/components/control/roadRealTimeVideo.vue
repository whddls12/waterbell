<template lang="">
  <div class="cctv">
    <div class="entrance">지하차도 입구</div>
    <div class="internal">지하차도 내부</div>
  </div>
</template>
<script>
import { defineComponent, onMounted, onBeforeUnmount } from 'vue'
import webSocket1 from '@/types/webSocket_cam1'
import webSocket2 from '@/types/webSocket_cam2'
export default defineComponent({
  name: 'roadControlCctvVue',
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
})
</script>
<style scoped>
.cctv {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 700px;
  height: 341.163px;
}

.entrance {
  width: 300px;
  height: 280px;
  background: url(<path-to-image>),
    lightgray -9.84px 0px / 120.982% 100% no-repeat;
  margin-left: 50px;
}

.internal {
  width: 300px;
  height: 280px;
  background: url(<path-to-image>),
    lightgray -9.84px 0px / 120.982% 100% no-repeat;
  margin-left: 50px;
}
</style>
