<template>
  <div class="container mt-4">
    <table class="table table-hover table-bordered table-bordered">
      <thead class="thead-dark">
        <tr>
          <!-- <th scope="col" class="text-center" style="width: 50px">No.</th> -->
          <th scope="col" class="text-center" style="width: 400px">제목</th>
          <th scope="col" class="text-center" style="width: 150px">작성자</th>
          <th scope="col" class="text-center" style="width: 150px">처리상태</th>
          <th scope="col" class="text-center" style="width: 150px">작성일시</th>
          <th scope="col" class="text-center" style="width: 150px">조회수</th>
          <th scope="col" class="text-center" style="width: 150px">파일</th>
        </tr>
      </thead>
      <tbody v-if="reportList && reportList.length">
        <tr
          v-for="(report, index) in reportList"
          :key="index"
          class="tr"
          @click="movePage(report.id)"
          align="center"
        >
          <!-- <td>{{ index + 1 }}</td> -->
          <td>{{ report.title }}</td>
          <td>{{ report.name }}</td>
          <td>{{ report.status }}</td>
          <td>{{ report.createDate }}</td>
          <td>{{ report.viewCount }}</td>
          <td>{{ report.uploadedfiles }}</td>
        </tr>
      </tbody>
      <tbody v-else>
        <tr>
          <td colspan="6" class="text-center">등록된 신고접수가 없습니다.</td>
        </tr>
      </tbody>
    </table>
    <!-- 페이지네이션 컨트롤 -->
    <div class="pagination-container">
      <div class="pagination">
        <!-- 이전 페이지 그룹으로 이동 -->
        <span
          v-if="pageNavigation.startPage > 1"
          @click.prevent="goToPage(pageNavigation.pre)"
        >
          &laquo;
        </span>

        <!-- 현재 페이지 그룹의 페이지 숫자들 -->
        <span
          v-for="page in range(
            pageNavigation.startPage,
            pageNavigation.endPage
          )"
          :key="page"
          :class="{ active: page === pageNavigation.pgno }"
          @click.prevent="goToPage(page)"
        >
          {{ page }}
        </span>

        <!-- 다음 페이지 그룹으로 이동 -->
        <span
          v-if="pageNavigation.endPage < pageNavigation.totalPageCnt"
          @click.prevent="goToPage(pageNavigation.next)"
        >
          &raquo;
        </span>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, computed, ref } from 'vue'
import http from '@/types/http'
import store from '@/store/index'
import { useRouter } from 'vue-router'
import axios from '@/types/apiClient'

export default defineComponent({
  name: 'roadReportListVue',
  setup() {
    // getters에서 nowUnderroad 가져오기
    // const nowUnderroad = computed(() => store.getters.nowUnderroad).value

    const facility_id = computed(() => store.getters['auth/facilityId'])
    const currentPage = ref(1)

    const apiClient = axios.apiClient(store)

    let reportList = ref<
      {
        id: string
        title: string
        content: string
        name: string
        phone: string
        status: string
        createDate: string
        viewCount: string
        boardPassword: string
      }[]
    >([])
    let pageNavigation = ref<{
      pgno: number
      totalCnt: number
      totalPageCnt: number
      startRange: boolean
      endRange: boolean
      naviSize: number
      startPage: number
      endPage: number
      pre: number
      next: number
      start: number
    }>({
      pgno: 1,
      totalCnt: 0,
      totalPageCnt: 0,
      startRange: false,
      endRange: false,
      naviSize: 0,
      startPage: 1,
      endPage: 1,
      pre: 0,
      next: 0,
      start: 0
    })

    const setList = () => {
      http
        .get(
          `/reports/undergroudRoad/${facility_id.value}/${currentPage.value}`
        )
        .then((res) => {
          // 가져온 신고접수 리스트 데이터를 준비된 배열에 넣기.
          console.log(res.data)
          reportList.value = res.data.list
          pageNavigation.value = res.data.pageNavigation
        })

        .catch(() => {
          console.log('목록없음')
        })
    }

    const range = (start: number, end: number) => {
      return [...Array(end - start + 1).keys()].map((val) => val + start)
    }

    const goToPage = (page: number) => {
      currentPage.value = page
      try {
        apiClient
          .get(
            `/reports/undergroudRoad/${facility_id.value}/${currentPage.value}`
          )
          .then((res) => {
            reportList.value = res.data.list
            pageNavigation.value = res.data.pageNavigation
          })
      } catch (error) {
        console.error('Error fetching height data:', error)
      }
    }

    const router = useRouter()
    const movePage = (report_id: any) => {
      console.log('Clicked on report_id:', report_id)
      router.push({
        path: `/road/report/${report_id}/detail`
      })
    }
    onMounted(() => {
      setList()
    })
    return {
      reportList,
      currentPage,
      pageNavigation,
      movePage,
      range,
      goToPage,
      setList
    }
  }
})
</script>
<style></style>
