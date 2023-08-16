<template>
  <div class="table-box">
    <button v-if="role == 'APART_MEMBER'" id="create" @click="createReport">
      글작성
    </button>
    <table class="table table-hover table-bordered table-bordered">
      <thead class="thead-dark">
        <tr>
          <!-- <th scope="col" class="text-center" style="width: 50px">No.</th> -->
          <th scope="col" class="text-center" style="width: 400px">제목</th>
          <th scope="col" class="text-center" style="width: 150px">작성자</th>
          <th scope="col" class="text-center" style="width: 150px">처리상태</th>
          <th scope="col" class="text-center" style="width: 150px">작성일시</th>
          <th scope="col" class="text-center" style="width: 150px">조회수</th>
          <!-- <th scope="col" class="text-center" style="width: 150px">파일</th> -->
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
          <td>{{ report.apartMember.name }}</td>
          <td>{{ statusEngToKr(report.status) }}</td>
          <td>{{ formattedTime(report.createDate) }}</td>
          <td>{{ report.viewCount }}</td>
          <!-- <td>{{ report.uploadedfiles }}</td> -->
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
import { defineComponent, ref, computed, onMounted } from 'vue'
import store from '@/store/index'
import { useRouter } from 'vue-router'
import axios from '@/types/apiClient'

export default defineComponent({
  name: 'parkReportListVue',
  setup() {
    const role = computed(() => store.getters['auth/role']).value

    // 아파트 id 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value

    const apiClient = axios.apiClient(store)

    const currentPage = ref(1)
    // // 신고접수 리스트 정의
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

    // // 신고접수 리스트 가져오기
    const setList = () => {
      apiClient
        .get(`/reports/apartBoard/apart/${facility_id}/${currentPage.value}`)
        .then((res) => {
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
          .get(`/reports/apartBoard/apart/${facility_id}/${currentPage.value}`)
          .then((res) => {
            reportList.value = res.data.list
            pageNavigation.value = res.data.pageNavigation
          })
      } catch (error) {
        console.error('Error fetching height data:', error)
      }
    }

    const router = useRouter()
    // 신고접수 상세 조회로 이동
    const movePage = (board_id: any) => {
      router.push(`/park/report/${board_id}/detail`)
    }

    function statusEngToKr(status: string) {
      if (status === 'BEFORE') {
        return '처리전'
      } else if (status === 'PROCESSING') {
        return '처리중'
      } else if (status === 'COMPLETE') {
        return '처리완료'
      }
    }

    function createReport() {
      // 권한이 있는 경우 글쓰기 페이지로 이동
      if (role == 'APART_MANAGER' || role == 'APART_MEMBER') {
        router.push(`/park/report/create`)
      } else {
        alert('글작성 권한이 없습니다.')
      }
    }

    // 작성일 포맷팅
    const formattedTime = (dateTime: string) => {
      let date = new Date(dateTime)
      let year = date.getFullYear()
      let month = (1 + date.getMonth()).toString().padStart(2, '0')
      let day = date.getDate().toString().padStart(2, '0')
      let hours = date.getHours().toString().padStart(2, '0')
      let minutes = date.getMinutes().toString().padStart(2, '0')
      let seconds = date.getSeconds().toString().padStart(2, '0')

      return `${year}년 ${month}월 ${day}일 ${hours}:${minutes}:${seconds}`
    }

    onMounted(() => {
      setList()
    })
    return {
      role,
      reportList,
      pageNavigation,
      currentPage,
      movePage,
      range,
      goToPage,
      setList,
      statusEngToKr,
      formattedTime,
      createReport
    }
  }
})
</script>
<style scoped lang="css">
.table-box {
  display: flex;
  flex-direction: column;
  width: 100%; /* 너비를 100%로 설정하여 부모 요소의 전체 너비를 사용 */
  padding: 20px; /* 좌우에 20px의 패딩을 추가 */
  margin: 0px auto; /* 상하 간격을 10px로 유지하고 좌우 마진을 자동으로 설정하여 가운데 정렬 */
  box-sizing: border-box; /* 패딩을 포함한 전체 너비를 100%로 유지*/
}

form {
  display: flex;
  flex-direction: row;
  gap: 10px;
  margin: 10px;
}

.searchBox {
  align-self: flex-end;
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
  width: 100%; /* 너비를 100%로 설정하여 부모 요소의 전체 너비를 사용 */
  padding: 20px; /* 좌우에 20px의 패딩을 추가 */
  margin: 10px auto; /* 상하 간격을 10px로 유지하고 좌우 마진을 자동으로 설정하여 가운데 정렬 */
  box-sizing: border-box; /* 패딩을 포함한 전체 너비를 100%로 유지*/
}

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

#create {
  gap: 10px;
  margin: 10px;
  align-self: flex-end;
  color: white;
  border-radius: 10px;
  background: var(--1, #10316b);
  display: flex;
  width: 100px;
  padding: 5px 10px;
  justify-content: center;
  align-items: center;
  gap: 8px;
}
</style>
