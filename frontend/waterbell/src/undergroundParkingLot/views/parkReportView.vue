<template>
  <div class="report-list">
    <div class="report-list-header title">신고 접수</div>
  </div>

  <ParkReportList />
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import ParkReportList from '../components/report/parkReportList.vue'

export default defineComponent({
  name: 'parkReportView',
  components: {
    ParkReportList
  }
})
</script>
<style scoped>
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
  margin-bottom: 40px;
  margin-top: 40px;
}
</style>
