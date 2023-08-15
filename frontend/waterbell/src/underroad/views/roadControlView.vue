<template lang="">
  <div class="main-container">
    <div class="title">지하차도 CCTV</div>
    <div class="road-control-cctv-com">
      <RoadControlCctvCom />
    </div>
    <div class="control-container">
      <RoadControlWarningLightCom />
      <RoadControlLedCom />
    </div>
  </div>
</template>
<script>
import { defineComponent } from 'vue'
import RoadControlCctvCom from '../components/control/roadRealTimeVideo.vue'
import RoadControlLedCom from '../components/control/roadLedBoardControl.vue'
import RoadControlWarningLightCom from '../components/control/roadWarningLightControl.vue'

export default defineComponent({
  name: 'RoadControl',
  components: {
    RoadControlCctvCom,
    RoadControlLedCom,
    RoadControlWarningLightCom
  }
})
</script>
<style scoped>
.road-control-cctv-com {
  width: 770px;
  height: 400px;
  flex-shrink: 0;
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
