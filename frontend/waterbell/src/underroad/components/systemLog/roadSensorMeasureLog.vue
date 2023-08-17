<template lang="">
  <div class="table-box">
    <div class="title">센서 측정 로그</div>
    <h6>{{ LogFacilityName }}</h6>
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

    <div id="category-select">
      <div>
        <input type="radio" id="height" value="HEIGHT" v-model="category" />
        <label for="height">수위</label>
      </div>
      <div>
        <input type="radio" id="TEMP" value="TEMP" v-model="category" />
        <label for="TEMP">온도</label>
      </div>
      <div>
        <input type="radio" id="humidity" value="HUMID" v-model="category" />
        <label for="humidity">습도</label>
      </div>
      <div>
        <input type="radio" id="dust" value="DUST" v-model="category" />
        <label for="dust">미세먼지</label>
      </div>
    </div>
    <table class="table table-hover table-bordered table-bordered">
      <thead class="thead-dark">
        <tr>
          <!-- <th scope="col" class="text-center" style="width: 50px">번호</th> -->
          <th scope="col" class="text-center" style="width: 600px">시간</th>
          <!-- <th scope="col" class="text-center" style="width: 150px">시설이름</th> -->
          <th scope="col" class="text-center" style="width: 200px">구분</th>
          <th
            v-if="category === 'HEIGHT'"
            scope="col"
            class="text-center"
            style="width: 200px"
          >
            센서값(cm)
          </th>
          <th
            v-if="category === 'TEMP'"
            scope="col"
            class="text-center"
            style="width: 200px"
          >
            센서값(℃)
          </th>
          <th
            v-if="category === 'HUMID'"
            scope="col"
            class="text-center"
            style="width: 200px"
          >
            센서값(%)
          </th>
          <th
            v-if="category === 'DUST'"
            scope="col"
            class="text-center"
            style="width: 200px"
          >
            센서값(㎍/㎥)
          </th>
        </tr>
      </thead>
      <tbody v-if="logList && logList.length">
        <tr
          v-for="log in logList"
          :key="log.id"
          class="tr"
          @click="movePage(log.id)"
          align="center"
        >
          <!-- <td>{{ index + 1 }}</td> -->
          <td>{{ formattedSensorTime(log.sensorTime) }}</td>
          <!-- <td>{{ log.facilityName }}</td> -->
          <td v-html="categoryLabel(log.category)"></td>
          <td v-if="log.category === 'HEIGHT'">{{ log.value }} cm</td>
          <td v-if="log.category === 'TEMP'">{{ log.value }} ℃</td>
          <td v-if="log.category === 'HUMID'">{{ log.value }} %</td>
          <td v-if="log.category === 'DUST'">{{ log.value }} ㎍/㎥</td>
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

<script scoped lang="ts">
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
    const category = ref('HEIGHT')
    const currentPage = ref(1)
    const facilityId = computed(() => store.getters['auth/facilityId']).value

    let logList = ref<
      {
        id: number
        sensorTime: string
        facilityName: string
        category: string
        value: number
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
          `/system/manager/facilities/${facilityId}/sensors/${category.value}/logs/${currentPage.value}`,
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
            `/system/manager/facilities/${facilityId}/sensors/${category.value}/logs/${currentPage.value}`,
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

    watch(category, (newCategory: any) => {
      category.value = newCategory
      currentPage.value = 1
      let formattedStartDate = toLocalDateTime(startDate.value)
      let formattedEndDate = toLocalDateTime(endDate.value)
      apiClient
        .get(
          `/system/manager/facilities/${facilityId}/sensors/${category.value}/logs/${currentPage.value}`,
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
    })

    const fetchLogList = (start: any, end: any) => {
      let formattedStartDate = toLocalDateTime(start)
      let formattedEndDate = toLocalDateTime(end)
      currentPage.value = 1

      apiClient
        .get(
          `/system/manager/facilities/${facilityId}/sensors/${category.value}/logs/${currentPage.value}`,
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
        case 'HEIGHT':
          return '<span style="color: blue;">수위</span>'
        case 'TEMP':
          return '<span style="color: red;">온도</span>'
        case 'HUMID':
          return '<span style="color: green;">습도</span>'
        case 'DUST':
          return '<span style="color: orange;">미세먼지</span>'
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

    const LogFacilityName = computed(() => {
      return logList.value.length ? logList.value[0].facilityName : ''
    })

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
      LogFacilityName
    }
  }
})
</script>
<style scoped lang="css">
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
  width: 100%; /* 너비를 100%로 설정하여 부모 요소의 전체 너비를 사용 */
  padding: 20px 0; /* 좌우에 20px의 패딩을 추가 */
  margin: 10px auto; /* 상하 간격을 10px로 유지하고 좌우 마진을 자동으로 설정하여 가운데 정렬 */
  box-sizing: border-box; /* 패딩을 포함한 전체 너비를 100%로 유지*/
}

.table {
  width: 100%;
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

.datepicker-row {
  display: flex;
  justify-content: space-between;
  margin-top: 30px;
  margin-bottom: 50px;
}

.datepicker-row > div {
  flex: 1; /* 각 요소가 같은 너비를 가지도록 합니다. */
}

.VueDatePicker {
  height: 50px;
  width: 100%; /* 또는 적당한 %값 */
}

#category-select {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  margin-top: 20px;
  gap: 20px;
}

#category-select > div {
  display: flex;
  gap: 5px;
}
</style>
