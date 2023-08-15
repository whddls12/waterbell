<template lang="">
  <div class="main-container">
    <div class="title">지하주차장 CCTV</div>
    <div class="road-control-cctv-com">
      <ParkControlCctvCom />
    </div>
    <div class="controll-container">
      <ParkControlWarningLightCom />
      <ParkControlWallCom />
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import ParkControlCctvCom from '../components/control/parkRealTimeVideo.vue'
import ParkControlWallCom from '../components/control/parkWaterPlateControl.vue'
import ParkControlWarningLightCom from '../components/control/parkSirenControl.vue'

export default defineComponent({
  name: 'ParkControl',
  components: {
    ParkControlCctvCom,
    ParkControlWallCom,
    ParkControlWarningLightCom
  }
})
</script>
<style scoped>
.road-control-cctv-com {
  /* width: 770px; */
  /* height: 320px; */
  /* flex-shrink: 0; */
  fill: var(--unnamed, #f2f7ff);
  filter: drop-shadow(0px 4px 30px rgba(0, 0, 0, 0.17));
}

.main-container {
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 40px;
  width: 1000px;
}

.control-container {
  width: 1000px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 100px;
  align-self: stretch;
  margin-bottom: 50px;
}

.title {
  color: var(--typography-1, #1c2a53);
  text-align: center;
  font-family: score;
  /* 회원가입상자_제목 */
  font-size: 30px;
  font-style: normal;
  font-weight: 500;
  line-height: 16px; /* 53.333% */
  letter-spacing: 3px;
  margin-bottom: 60px;
  margin-top: 40px;
}
</style>
