<template lang="">
  <!-- <div class="dash-box"> -->
  <div class="bigbox">
    <!-- <div> -->
    <roadDashWarnMsgVue />
    <div id="columnflex">
      <div id="rowflex">
        <div id="columnflex">
          <roadDashCctvVue class="left" id="dashcomponent" />
        </div>
        <div id="columnflex">
          <roadDashMapVue class="right" id="dashcomponent" />
          <roadDashRainAmountVue class="right" id="dashcomponent" />
        </div>
      </div>

      <div id="rowflex">
        <roadDashReportVue class="left" id="dashcomponent" />
        <roadDashWaterLevelVue class="right" id="dashcomponent" />
      </div>
    </div>
  </div>
</template>
<script>
// import Map from '@/underroad/components/dashboard/roadDashNearUnderroad.vue'

import { defineComponent } from 'vue'

import roadDashMapVue from '../components/dashboard/roadDashNearUnderroad.vue'
import roadDashReportVue from '../components/dashboard/roadDashReportList.vue'
import roadDashWarnMsgVue from '../components/dashboard/roadDashWarningMessage.vue'
import roadDashWaterLevelVue from '../components/dashboard/roadDashWaterLevelHeight.vue'
import roadDashCctvVue from '../components/dashboard/roadDashRealTimeVideo.vue'
import roadDashRainAmountVue from '../components/dashboard/roadDashRainFallAmount.vue'

export default defineComponent({
  name: 'RoadDash',
  components: {
    roadDashCctvVue,
    roadDashMapVue,
    roadDashRainAmountVue,
    roadDashReportVue,
    roadDashWarnMsgVue,
    roadDashWaterLevelVue
  }
})
</script>
<style>
.bigbox {
  width: 100%;
  max-width: 1400px;
  margin: 0;
  height: 100%; /* set height to 100% */
  display: flex;
  justify-self: center;
  align-self: center;
  flex-grow: inherit;
  flex-direction: column;
  /* align-items: stretch; */
}
.container {
  display: flex;
  flex-direction: column;
  width: 480px;
  border-radius: 8px;
  padding: 10px;
  background-color: #f2f7ff;
}
#rowflex {
  display: flex;
  border-radius: 8px;
  flex-direction: row;
  justify-content: space-evenly;
  align-items: stretch; /* stretch child components to have equal heights */
}

#columnflex {
  display: flex;
  flex-direction: column;
  height: 100%; /* set height to 100% */
}

#dashcomponent {
  flex-grow: 1; /* allow components to grow equally */
  margin: 10px;
}
/* .container {
  display: flex;
  flex-direction: column;
  padding: 10px;
  background-color: #f5d682;
  border: 1px solid red;
}
.bigbox {
  width: 100%;
  max-width: 1400px;
  margin: 0;
}

#rowflex {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
  /* flex-wrap: wrap; */

#columnflex {
  display: flex;
  flex-direction: column;
}

#dashcomponent {
  margin: 10px;
}

.right {
  flex-grow: 3;
}

.left {
  flex-grow: 2;
}
</style>
