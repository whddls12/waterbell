<template lang="">
  <div class="container mt-4">
    <div><h1>알림함</h1></div>
    <table class="table table-hover table-bordered table-bordered">
      <thead class="thead-dark">
        <tr>
          <th scope="col" class="text-center" style="width: 50px">번호</th>
          <th scope="col" class="text-center" style="width: 400px">내용</th>
          <th scope="col" class="text-center" style="width: 150px">알림종류</th>
          <th scope="col" class="text-center" style="width: 150px">발신자</th>
          <th scope="col" class="text-center" style="width: 150px">등록일시</th>
          <th scope="col" class="text-center" style="width: 150px">처리상태</th>
        </tr>
      </thead>
      <tbody v-if="AlarmList && AlarmList.length">
        <tr
          v-for="(alarm, index) in AlarmList"
          :key="alarm.id"
          class="tr"
          @click="movePage(alarm.id)"
          align="center"
        >
          <td>{{ index + 1 }}</td>
          <td>{{ alarm.content }}</td>
          <td>{{ alarm.alarmType }}</td>
          <td>{{ alarm.sender }}</td>
          <td>{{ alarm.regDate }}</td>
          <td>{{ alarm.status }}</td>
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
import { useRouter } from 'vue-router'
import store from '@/store/index'
import axios from '@/types/apiClient'

export default defineComponent({
  name: 'roadDashReportVue',

  setup() {
    const currentPage = ref(1)
    const apiClient = axios.apiClient(store)
    const role = computed(() => store.getters['auth/role']).value
    const router = useRouter()
    let AlarmList = ref<
      {
        id: string
        content: string
        alarmType: string
        sender: string
        regDate: string
        status: string
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

    const setList = async () => {
      try {
        const res = await apiClient.get(`/alarm/${role}/${currentPage.value}`)
        AlarmList.value = res.data.list
        pageNavigation.value = res.data.pageNavigation
        console.log(pageNavigation.value)
      } catch (error) {
        console.error('Error fetching height data:', error)
      }
    }

    const range = (start: number, end: number) => {
      return [...Array(end - start + 1).keys()].map((val) => val + start)
    }

    const goToPage = (page: number) => {
      currentPage.value = page
      try {
        apiClient.get(`/alarm/${role}/${currentPage.value}`).then((res) => {
          AlarmList.value = res.data.list
          pageNavigation.value = res.data.pageNavigation
        })
      } catch (error) {
        console.error('Error fetching height data:', error)
      }
    }

    const movePage = (alarm_id: any) => {
      console.log('Clicked on alarm_id:', alarm_id)
      router.push(`/alarm/detail/${alarm_id}`)
    }

    onMounted(() => {
      setList()
    })
    return {
      AlarmList,
      currentPage,
      movePage,
      range,
      goToPage,
      pageNavigation
    }
  }
})
</script>
<style lang="css">
.thead-dark th {
  background-color: #343a40 !important;
  color: white !important;
}

.tr {
  cursor: pointer;
}

.container {
  width: 90%;
  margin: 0 auto;
}

/* 테이블 셀 내용 가운데 정렬 */
.table th,
.table td {
  text-align: center;
}

/* 테이블 헤더 글자 크기 및 굵게 */
.table th {
  font-size: 14px;
  font-weight: bold;
}

/* 테이블 내용 글자 크기 */
.table td {
  font-size: 14px;
}

/* 테이블 행 높이 조정 */
.table tbody tr {
  height: 50px;
}

/* "등록된 신고접수가 없습니다." 메시지 스타일 */
.no-data-message {
  font-size: 16px;
  font-weight: bold;
  color: #999;
  height: 100px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.table-bordered th,
.table-bordered td {
  border: 1px solid #dee2e6; /* 원하는 색상과 크기로 조정 가능 */
}
</style>
