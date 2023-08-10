<template>
  <div class="report-list">
    <div class="report-list-header">
      <h1>신고 접수</h1>
      <router-link to="/park/report/create">글쓰기</router-link>
    </div>
  </div>
  <hr />
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
<style></style>
