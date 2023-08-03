<template>
  <div>
    <h2>지하주차장 대시보드 페이지</h2>
    <parkDashDustVue />
    <parkDashRainFallAmountVue />
    <parkDashRealTimeVideoVue />
    <parkDashReportListVue />
    <parkDashTempAndHumidVue />
    <parkDashWarningMessageVue />
    <parkDashWaterLevelHeightVue />
  </div>
</template>
<script>
import { defineComponent } from 'vue'
import parkDashDustVue from '../components/dashboard/parkDashDust.vue'
import parkDashRainFallAmountVue from '../components/dashboard/parkDashRainFallAmount.vue'
import parkDashRealTimeVideoVue from '../components/dashboard/parkDashRealTimeVideo.vue'
import parkDashReportListVue from '../components/dashboard/parkDashReportList.vue'
import parkDashTempAndHumidVue from '../components/dashboard/parkDashTempAndHumid.vue'
import parkDashWarningMessageVue from '../components/dashboard/parkDashWarningMessage.vue'
import parkDashWaterLevelHeightVue from '../components/dashboard/parkDashWaterLevelHeight.vue'

export default defineComponent({
  name: 'ParkDash',
  components: {
    parkDashDustVue,
    parkDashRainFallAmountVue,
    parkDashRealTimeVideoVue,
    parkDashReportListVue,
    parkDashTempAndHumidVue,
    parkDashWarningMessageVue,
    parkDashWaterLevelHeightVue
  }
})
</script>
<style lang=""></style>
