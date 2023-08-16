<template lang="">
  <div class="cctv">
    <div>
      <p class="cctvLabel">지하주차장 입구</p>
      <div class="cctvBox">
        <div>
          <img id="cctv1" />
        </div>
      </div>
    </div>
    <div>
      <p class="cctvLabel">지하주차장 내부</p>
      <div class="cctvBox">
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
  gap: 10px;
  /* width: 700px; */
  height: 320px;
  margin-bottom: 60px;
}

.cctvBox {
  width: 480px;
  height: 320px;
  background: url(<path-to-image>),
    lightgray -9.84px 0px / 120.982% 100% no-repeat;
  margin-left: 0;
  position: relative; /* 추가된 코드 */
  overflow: hidden;
}
.cctvBox > div {
  position: absolute; /* 내부 div의 절대 위치 설정 */
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
.cctvBox > div > img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 이미지 비율을 유지하면서 꽉 차게 표시 */
  position: absolute; /*이미지를 div의 상단과 좌측 모서리에 위치시키기 위한 코드 */
  top: 0;
  left: 0;
  z-index: 1; /* 텍스트를 이미지 위에 표시하기 위해 z-index 추가 */
}

.cctv > div {
  display: flex;
  flex-direction: column;
  position: relative;
}
.cctvLabel {
  /*position: absolute;
  /* top: 10px; 위치를 조정하여 텍스트 위치 변경 가능 */
  justify-self: center;
  align-self: center;

  z-index: 2; /* 텍스트를 이미지 위에 표시하기 위해 z-index 추가 */
}
</style>
