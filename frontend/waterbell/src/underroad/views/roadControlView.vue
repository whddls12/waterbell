<template lang="">
  <div>
    <RoadControlCctvCom />
    <RoadControlWarningLightCom />
    <RoadControlLedCom />
  </div>
</template>
<script>
import { defineComponent } from "vue";
import roadControlLedCom from "../components/control/roadLedBoardControl.vue";
import roadControlCctvCom from "../components/control/roadRealTimeVideo.vue";
import roadControlWarningLightCom from "../components/control/roadWarningLightControl.vue";
export default defineComponent({
  name: "RoadControl",
  components: {
    roadControlCctvCom,
    roadControlLedCom,
    roadControlWarningLightCom,
  },
});
</script>
<style lang=""></style>
