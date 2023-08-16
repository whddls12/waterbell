<template>
  <div class="report-list">
    <div class="report-list-header">
      <h1>신고 접수</h1>
      <!-- <router-link to="/road/report/create">글쓰기</router-link> -->
    </div>
  </div>

  <RoadReportList />
</template>

<script lang="ts">
import RoadReportList from '../components/report/roadReportList.vue'

import { computed, defineComponent } from 'vue'
import router from '@/router/index'
import store from '@/store/index'

export default defineComponent({
  name: 'RoadReport',
  components: {
    RoadReportList
  }
})
</script>
<style scoped></style>
