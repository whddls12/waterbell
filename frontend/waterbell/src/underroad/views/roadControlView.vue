<template lang="">
  <div class="main-container">
    <div class="header">지하차도 CCTV</div>
    <div class="road-control-cctv-com">
      <RoadControlCctvCom />
    </div>
    <div class="controll-container">
      <RoadControlLedCom />
      <RoadControlWarningLightCom />
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

.controll-container {
  width: 1000px;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 100px;
  align-self: stretch;
}

.header {
  display: flex;
  width: 314px;
  height: 64.877px;
  flex-direction: column;
  justify-content: center;
  flex-shrink: 0;
  color: #000;
  text-align: center;
  font-family: Roboto;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 32px; /* 133.333% */
}
</style>
