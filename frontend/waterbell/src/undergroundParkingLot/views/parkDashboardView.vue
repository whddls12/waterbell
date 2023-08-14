<template>
  <div class="bigbox">
    <!-- <div> -->
    <parkDashWarningMessageVue />
    <div id="columnflex">
      <div id="rowflex">
        <div id="columnflex">
          <parkDashCctvVue class="left" id="dashcomponent" />
        </div>
        <div id="columnflex">
          <parkDashWeatherVue class="right" id="dashcomponent" />
          <!-- <parkDashTempAndHumidVue class="right" id="dashcomponent" /> -->
          <parkDashDustVue class="right" id="dashcomponent" />
          <parkDashRainFallAmountVue class="right" id="dashcomponent" />
        </div>
      </div>

      <div id="rowflex">
        <parkDashReportListVue class="left" id="dashcomponent" />
        <parkDashWaterLevelHeightVue class="right" id="dashcomponent" />
      </div>
    </div>
  </div>
</template>
<script>
import { defineComponent } from 'vue'
import parkDashDustVue from '../components/dashboard/parkDashDust.vue'
import parkDashRainFallAmountVue from '../components/dashboard/parkDashRainFallAmount.vue'
import parkDashCctvVue from '../components/dashboard/parkDashRealTimeVideo.vue'
import parkDashReportListVue from '../components/dashboard/parkDashReportList.vue'
// import parkDashTempAndHumidVue from '../components/dashboard/parkDashTempAndHumid.vue'
import parkDashWarningMessageVue from '../components/dashboard/parkDashWarningMessage.vue'
import parkDashWaterLevelHeightVue from '../components/dashboard/parkDashWaterLevelHeight.vue'
import parkDashWeatherVue from '../components/dashboard/parkDashWeather.vue'

export default defineComponent({
  name: 'ParkDash',
  components: {
    parkDashDustVue,
    parkDashRainFallAmountVue,
    parkDashCctvVue,
    parkDashReportListVue,
    // parkDashTempAndHumidVue,
    parkDashWarningMessageVue,
    parkDashWaterLevelHeightVue,
    parkDashWeatherVue
  }
})
</script>
<style lang="css" scoped>
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
