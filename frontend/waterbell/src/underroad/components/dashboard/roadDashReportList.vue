<template lang="">
  <div class="container">
    <div><h5>최근 신고접수 내역</h5></div>
    <div>
      <thead>
        <tr>
          <th scope="col" class="text-center">제목</th>
          <th scope="col" class="text-center">처리상태</th>
          <th scope="col" class="text-center">등록일시</th>
        </tr>
      </thead>
      <tbody v-if="reportList.length != 0">
        <tr
          v-for="report in reportList"
          :key="report.report_id"
          class="tr"
          @click="movePage(report.board_id)"
          align="center"
        >
          <!-- <th scope="row">{{ no }}</th> -->
          <td>{{ report.title }}</td>
          <td>{{ report.status }}</td>
          <td>{{ report.createDate }}</td>
        </tr>
      </tbody>

      <tbody v-if="reportList.length == 0">
        <td colspan="4" class="text-center">등록된 신고접수가 없습니다.</td>
      </tbody>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, computed, ref } from 'vue'
import http from '@/types/http'
import store from '@/store/index'
import { useRouter } from 'vue-router'

export default defineComponent({
  name: 'roadDashReportVue',

  setup() {
    // const store = useStore()

    // getters에서 nowUnderroad 가져오기
    // const nowUnderroad = computed(() => store.getters.nowUnderroad).value

    const facility_id = computed(() => store.getters['auth/facilityId']).value

    let reportList = ref<
      {
        board_id: string
        title: string
        status: string
        create_date: string
      }[]
    >([])
    //  로 바꿔보기
    const setList = () => {
      http.get(`/reports/dash/${facility_id}`).then((res) => {
        //가져온 신고접수 리스트 데이터를 준비된 배열에 넣기.
        // console.log(res.data.list)
        reportList.value = res.data.list
      })
    }
    const router = useRouter()
    const movePage = (board_id: any) => {
      router.push(`/road/report/item?board_id=${board_id}`)
    }
    onMounted(() => {
      setList()
    })
    return {
      reportList,
      movePage,
      setList
    }
  }
})
</script>
<style lang="css"></style>
