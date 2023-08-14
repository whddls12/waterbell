<template>
  <div>
    <p>신고접수 리스트</p>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from 'vue'
import store from '@/store'
import http from '@/types/http'
import router from '@/router'

export default defineComponent({
  name: 'parkReportListVue',
  setup() {
    // 아파트 정보 가져오기?
    const facility_id = computed(() => store.getters['auth/facilityId'])
    // // 신고접수 리스트 정의
    // let reportList = ref<{}[]>([])

    // // 신고접수 리스트 가져오기
    // const setList = () => {}

    // // 신고접수 상세 조회
    // const movePage = (report_id: any) => {
    //   router.push(`report/detail/:report_id`)
    // }
    // onMounted(() => {
    //   setList()
    // })
    // return { reportList, movePage, setList }
  }
})
</script>
<style></style>
