<template>
  <div class="container">
    <div class="dash-box">
      <div class="dash-box-title">
        <h3>CCTV</h3>
      </div>
      <div class="dash-box-content" id="cctv-box-content">
        <p class="text-start">지하차도 외부</p>
        <div class="cctvBox">
          <div>
            <img id="cctv1" />
          </div>
        </div>
        <p class="text-start">지하차도 외부</p>
        <div class="cctvBox">
          <div>
            <img id="cctv2" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
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
})
</script>
<style scoped lang="css">
#dash-cctv {
  /* set height to 100% */
  display: flex;
  flex-direction: column;
}

.cctvBox {
  width: 100%; /* 가로 폭을 부모 요소에 꽉 차게 설정 */
  position: relative; /* 내부 요소의 절대 위치를 위한 기준 설정 */
  padding-top: 75%; /* 4:3 비율 유지 */
  overflow: hidden; /* 내부 요소가 cctvBox를 벗어나는 것을 방지 */
}

.cctvBox > div {
  position: absolute; /* 내부 div의 절대 위치 설정 */
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.cctvBox > div > img {
  width: 100%; /* 이미지의 가로 폭을 부모 div에 꽉 차게 설정 */
  height: 100%; /* 이미지의 세로 높이를 부모 div에 꽉 차게 설정 */
  object-fit: cover; /* 이미지의 비율을 유지하면서 부모 div에 꽉 차게 표시 */
  border: 0;
}

#cctv-box-content {
  margin: 0px;
  padding: 0px;
}
</style>
