<template lang="">
  <div class="container mt-4">
    <table class="table table-hover table-bordered table-bordered">
      <thead class="thead-dark">
        <tr>
          <th scope="col" class="text-center" style="width: 50px">No.</th>
          <th scope="col" class="text-center" style="width: 400px">제목</th>
          <th scope="col" class="text-center" style="width: 150px">작성자</th>
          <th scope="col" class="text-center" style="width: 150px">처리상태</th>
          <th scope="col" class="text-center" style="width: 150px">작성일시</th>
          <th scope="col" class="text-center" style="width: 150px">조회수</th>
        </tr>
      </thead>
      <tbody v-if="reportList && reportList.length">
        <tr
          v-for="(report, index) in reportList"
          :key="report.report_id"
          class="tr"
          @click="movePage(report.report_id)"
          align="center"
        >
          <td>{{ index + 1 }}</td>
          <td>{{ report.title }}</td>
          <td>{{ report.name }}</td>
          <td>{{ report.status }}</td>
          <td>{{ report.createDate }}</td>
          <td>{{ report.viewCount }}</td>
        </tr>
      </tbody>
      <tbody v-else>
        <tr>
          <td colspan="6" class="text-center">등록된 신고접수가 없습니다.</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, computed, ref } from 'vue'
import http from '@/types/http'
import store from '@/store/index'
import { useRouter } from 'vue-router'

export default defineComponent({
  name: 'roadReportListVue',
  setup() {
    // getters에서 nowUnderroad 가져오기
    const nowUnderroad = computed(() => store.getters['auth/facilityId']).value
    console.log(nowUnderroad)
    const facility_id = nowUnderroad

    let reportList = ref<
      {
        report_id: string
        title: string
        author: string
        status: string
        createDate: string
        viewCount: string
        // 얘 이름달라도 가져와서 그냥 써지네 왜지
      }[]
    >([])

    const setList = () => {
      http
        // .get(`http://localhost:8080/reports/undergroudRoad/${facility_id}/1`)
        .get(`http://localhost:8080/reports/undergroudRoad/1/1`)
        .then((res) => {
          // 가져온 신고접수 리스트 데이터를 준비된 배열에 넣기.
          console.log(res.data.list)
          reportList.value = res.data.list
        })
    }
    const router = useRouter()
    const movePage = (report_id: any) => {
      router.push(`reports/${report_id}/road`)
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
<style lang=""></style>
