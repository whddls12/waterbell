<template>
  <div class="report-list">
    <div class="report-list-header">
      <h1>신고 접수</h1>
      <router-link to="/road/report/create">글쓰기</router-link>
    </div>
  </div>
  <hr />
  <RoadReportList />
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import RoadReportList from '../components/report/roadReportList.vue'

export default defineComponent({
  name: 'RoadReport',
  components: {
    RoadReportList
  }
})
</script>
<style scoped></style>
