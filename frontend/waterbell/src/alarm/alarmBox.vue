<template lang="">
  <div class="table-box mt-4">
    <div class="title">알림함</div>
    <table class="table table-hover table-bordered table-bordered">
      <thead class="thead-dark">
        <tr>
          <th scope="col" class="text-center" style="width: 50px">번호</th>
          <th scope="col" class="text-center" style="width: 400px">내용</th>
          <th scope="col" class="text-center" style="width: 150px">알림종류</th>
          <th scope="col" class="text-center" style="width: 150px">발신자</th>
          <th scope="col" class="text-center" style="width: 150px">등록일시</th>
          <th scope="col" class="text-center" style="width: 100px">처리상태</th>
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
          <td>{{ currentIndex(index) }}</td>
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
    let currentIndex = (index: any) => {
      return (currentPage.value - 1) * 10 + index + 1
    }
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
      currentIndex,
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
<style scoped lang="css">
.router-box {
  display: flex;
  min-height: 500px;
  min-width: 600px;
  width: 100%;

  overflow: hidden;
}
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

.thead-dark th {
  background-color: #f2f7ff !important;
  color: #114cb1 !important;
}

.tr {
  cursor: pointer;
}

.table-box {
  display: flex;
  flex-direction: column;
  /* 너비를 100%로 설정하여 부모 요소의 전체 너비를 사용 */
  padding: 20px 0; /* 좌우에 20px의 패딩을 추가 */
  margin: 10px 10px; /* 상하 간격을 10px로 유지하고 좌우 마진을 자동으로 설정하여 가운데 정렬 */
  box-sizing: border-box; /* 패딩을 포함한 전체 너비를 100%로 유지*/
}

/* .table {
  width: 100%;
} */
/* 테이블 셀 내용 가운데 정렬 */
.table th,
.table td {
  text-align: center;
  vertical-align: middle;
}

table th:first-child,
table td:first-child {
  border-left: 0;
}
table th:last-child,
table td:last-child {
  border-right: 0;
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

/* 페이지네이션 컨테이너를 아래쪽으로 배치 */
.pagination-container {
  text-align: center;
}

/* 페이지네이션 버튼들을 세로로 배치 */
.pagination {
  display: block;
  margin: 10px auto;
  width: 200px; /* Adjust the width as needed */
}

/* 페이지네이션 버튼 스타일 */
.pagination span {
  margin: 8px;
  cursor: pointer;
}

/* Active page style */
.pagination .active {
  text-decoration: underline;
}
</style>
