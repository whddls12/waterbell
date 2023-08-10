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
          <parkDustGraphVue class="right" id="dashcomponent" />
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
import parkDustGraphVue from '../components/dashboard/parkDustGraph.vue'

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
    parkDashWeatherVue,
    parkDustGraphVue
  }
})
</script>
<style lang=""></style>
