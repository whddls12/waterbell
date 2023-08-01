<template lang="">
  <div>
    지하차도/ 대시보드 view
    <roadDashWarnMsgVue />
    <roadDashCctvVue />
    <roadDashRainAmountVue />
    <roadDashMapVue />
    <roadDashWaterLevelVue />
    <roadDashReportVue />
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
<style lang=""></style>
