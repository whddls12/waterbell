<template lang="">
  <div>
    <RoadControlCctvCom />
    <RoadControlWarningLightCom />
    <RoadControlLedCom />
  </div>
</template>
<script>
<<<<<<< HEAD
import { defineComponent } from "vue";
<<<<<<< HEAD
import roadControlLedCom from "../components/control/roadLedBoardControl.vue";
import roadControlCctvCom from "../components/control/roadRealTimeVideo.vue";
import roadControlWarningLightCom from "../components/control/roadWarningLightControl.vue";
=======
import roadControlLedCom from "../components/control/roadLedBoardControl.vue"
import roadControlCctvCom from "../components/control/roadRealTimeVideo.vue"
import roadControlWarningLightCom from "../components/control/roadWarningLightControl.vue"
>>>>>>> 39cac92 (map_마커표시 완료)
=======
import { defineComponent } from 'vue'
import roadControlLedCom from '../components/control/roadLedBoardControl.vue'
import roadControlCctvCom from '../components/control/roadRealTimeVideo.vue'
import roadControlWarningLightCom from '../components/control/roadWarningLightControl.vue'
>>>>>>> f02261b (feat: map 지하차도 status별 마커 커스텀)
export default defineComponent({
  name: 'RoadControl',
  components: {
    roadControlCctvCom,
    roadControlLedCom,
<<<<<<< HEAD
    roadControlWarningLightCom,
  },
});
=======
    roadControlWarningLightCom
  }
})
>>>>>>> f02261b (feat: map 지하차도 status별 마커 커스텀)
</script>
<style lang=""></style>
