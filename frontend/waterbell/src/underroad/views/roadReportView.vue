<template>
  <div class="report-list">
    <div class="report-list-header">
      <h1>신고 접수</h1>
      <!-- <router-link to="/road/report/create">글쓰기</router-link> -->
      <button @click="createReport">글쓰기</button>
    </div>
  </div>
  <hr />
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
  },
  setup() {
    const role = computed(() => store.getters['auth/role']).value

    function createReport() {
      // 권한이 있는 경우 글쓰기 페이지로 이동
      if (role) {
        router.push({ path: `/road/report/create` })
      } else {
        // 비회원은 휴대폰인증 페이지로 이동
        router.push({ path: `/road/report/verification` })
      }
    }

    return { createReport }
  }
})
</script>
<style scoped></style>
