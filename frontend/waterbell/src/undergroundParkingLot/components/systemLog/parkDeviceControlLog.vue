<template lang="">
  <div class="container">
    <div class="title">기기 제어 로그</div>
    <div class="datepicker-row">
      <div>
        <label>시작일시</label>
        <span class="VueDatePicker">
          <VueDatePicker
            v-model="startDate"
            placeholder="Select date"
          ></VueDatePicker>
        </span>
      </div>
      <div>
        <label>종료일시</label>
        <span class="VueDatePicker">
          <VueDatePicker
            v-model="endDate"
            placeholder="Select date"
          ></VueDatePicker>
        </span>
      </div>
    </div>

    <table class="table table-hover table-bordered table-bordered">
      <thead class="thead-dark">
        <tr>
          <th scope="col" class="text-center" style="width: 50px">번호</th>
          <th scope="col" class="text-center" style="width: 400px">시간</th>
          <th scope="col" class="text-center" style="width: 150px">시설이름</th>
          <th scope="col" class="text-center" style="width: 150px">구분</th>
          <th scope="col" class="text-center" style="width: 150px">수위</th>
          <th scope="col" class="text-center" style="width: 150px">제어</th>
        </tr>
      </thead>
      <tbody v-if="logList && logList.length">
        <tr
          v-for="(log, index) in logList"
          :key="log.id"
          class="tr"
          @click="movePage(log.id)"
          align="center"
        >
          <td>{{ index + 1 }}</td>
          <td>{{ formattedSensorTime(log.time) }}</td>
          <td>{{ log.name }}</td>
          <td>{{ categoryLabel(log.category) }}</td>
          <td>{{ log.height }}cm</td>
          <td>{{ controlLabel(log.command) }}</td>
        </tr>
      </tbody>
      <tbody v-else>
        <tr>
          <td colspan="6" class="text-center">해당하는 데이터가 없습니다.</td>
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
import { defineComponent, onMounted, computed, ref, watch } from 'vue'
import store from '@/store/index'
import VueDatePicker from '@vuepic/vue-datepicker'
import axios from '@/types/apiClient'
import '@vuepic/vue-datepicker/dist/main.css'

export default defineComponent({
  name: 'roadDashReportVue',
  components: { VueDatePicker },
  setup() {
    const now = new Date()
    const startDate = ref(new Date(now.getFullYear(), now.getMonth(), 1))
    const endDate = ref(now)
    const apiClient = axios.apiClient(store)
    const category = ref('PLATE')
    const currentPage = ref(1)
    const facilityId = computed(() => store.getters['auth/facilityId']).value

    let logList = ref<
      {
        id: number
        time: string
        name: string
        category: string
        height: number
        command: string
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
      let formattedStartDate = toLocalDateTime(startDate.value)
      let formattedEndDate = toLocalDateTime(endDate.value)
      apiClient
        .get(
          `/system/manager/facilities/${facilityId}/control/logs/${currentPage.value}`,
          {
            params: {
              searchStartDate: formattedStartDate,
              searchEndDate: formattedEndDate
            }
          }
        )
        .then((res) => {
          logList.value = res.data.list
          pageNavigation.value = res.data.pageNavigation
        })
        .catch((error) => {
          console.log(error)
        })
    }

    const range = (start: number, end: number) => {
      return [...Array(end - start + 1).keys()].map((val) => val + start)
    }

    const movePage = (alarm_id: any) => {
      console.log('Clicked on alarm_id:', alarm_id)
    }

    const goToPage = (page: number) => {
      currentPage.value = page
      let formattedStartDate = toLocalDateTime(startDate.value)
      let formattedEndDate = toLocalDateTime(endDate.value)

      try {
        apiClient
          .get(
            `/system/manager/facilities/${facilityId}/control/logs/${currentPage.value}`,
            {
              params: {
                searchStartDate: formattedStartDate,
                searchEndDate: formattedEndDate
              }
            }
          )
          .then((res) => {
            logList.value = res.data.list
            pageNavigation.value = res.data.pageNavigation
          })
          .catch((error) => {
            console.log(error)
          })
      } catch (error) {
        console.error('Error fetching height data:', error)
      }
    }

    const fetchLogList = (start: any, end: any) => {
      let formattedStartDate = toLocalDateTime(start)
      let formattedEndDate = toLocalDateTime(end)
      currentPage.value = 1

      apiClient
        .get(
          `/system/manager/facilities/${facilityId}/control/logs/${currentPage.value}`,
          {
            params: {
              searchStartDate: formattedStartDate,
              searchEndDate: formattedEndDate
            }
          }
        )
        .then((res) => {
          logList.value = res.data.list
          pageNavigation.value = res.data.pageNavigation
        })
        .catch((error) => {
          console.log(error)
        })
    }

    watch([startDate, endDate], ([newStartDate, newEndDate]) => {
      fetchLogList(newStartDate, newEndDate)
    })

    const categoryLabel = (cat: string) => {
      switch (cat) {
        case 'PLATE':
          return '차수판'
        case 'BOARD':
          return '전광판'
        default:
          return ''
      }
    }

    const controlLabel = (cat: string) => {
      switch (cat) {
        case 'ON':
          return '동작'
        case 'OFF':
          return '해제'
        default:
          return ''
      }
    }

    function toLocalDateTime(date: any) {
      const year = date.getFullYear()
      const month = date.getMonth() + 1
      const day = date.getDate()
      const hours = date.getHours()
      const minutes = date.getMinutes()
      const seconds = date.getSeconds()

      return `${year}-${month.toString().padStart(2, '0')}-${day
        .toString()
        .padStart(2, '0')}T${hours.toString().padStart(2, '0')}:${minutes
        .toString()
        .padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
    }

    const formattedSensorTime = (dateTime: string) => {
      let date = new Date(dateTime)
      let year = date.getFullYear()
      let month = (1 + date.getMonth()).toString().padStart(2, '0')
      let day = date.getDate().toString().padStart(2, '0')
      let hours = date.getHours().toString().padStart(2, '0')
      let minutes = date.getMinutes().toString().padStart(2, '0')

      return `${year}년 ${month}월 ${day}일 ${hours}:${minutes}`
    }

    onMounted(() => {
      setList()
    })

    return {
      logList,
      currentPage,
      movePage,
      category,
      startDate,
      endDate,
      pageNavigation,
      range,
      goToPage,
      categoryLabel,
      formattedSensorTime,
      controlLabel
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

.datepicker-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 30px;
}

.datepicker-row > div {
  flex: 1; /* 각 요소가 같은 너비를 가지도록 합니다. */
}

.VueDatePicker {
  height: 50px;
  width: 100%; /* 또는 적당한 %값 */
}

.title {
  color: var(--typography-1, #1c2a53);
  text-align: center;
  /* 회원가입상자_제목 */
  font-family: Roboto;
  font-size: 30px;
  font-style: normal;
  font-weight: 500;
  line-height: 16px; /* 53.333% */
  letter-spacing: 3px;
  margin-bottom: 20px;
  margin-top: 20px;
}

#category-select {
  margin-bottom: 20px;
  margin-top: 20px;
}
</style>
