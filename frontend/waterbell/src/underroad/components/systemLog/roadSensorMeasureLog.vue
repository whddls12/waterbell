<template lang="">
  <div class="container mt-4">
    <div><h5>센서 측정 로그</h5></div>
    <div>
      <div>
        <label>Start Date:</label>
        <span class="VueDatePicker">
          <VueDatePicker
            v-model="startDate"
            placeholder="Select date"
          ></VueDatePicker>
        </span>
      </div>
      <div>
        <label>End Date:</label>
        <span class="VueDatePicker">
          <VueDatePicker
            v-model="endDate"
            placeholder="Select date"
          ></VueDatePicker>
        </span>
      </div>
      <label for="category-select">Category</label>
      <div id="category-select">
        <input type="radio" id="height" value="HEIGHT" v-model="category" />
        <label for="height">수위</label>
        <input
          type="radio"
          id="temperature"
          value="TEMPERATURE"
          v-model="category"
        />
        <label for="temperature">온도</label>
        <input type="radio" id="humidity" value="HUMIDITY" v-model="category" />
        <label for="humidity">습도</label>
        <input type="radio" id="dust" value="DUST" v-model="category" />
        <label for="dust">미세먼지</label>
      </div>
    </div>
    <table class="table table-hover table-bordered table-bordered">
      <thead class="thead-dark">
        <tr>
          <th scope="col" class="text-center" style="width: 50px">번호</th>
          <th scope="col" class="text-center" style="width: 400px">시간</th>
          <th scope="col" class="text-center" style="width: 150px">시설이름</th>
          <th scope="col" class="text-center" style="width: 150px">구분</th>
          <th scope="col" class="text-center" style="width: 150px">센서값</th>
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
          <td>{{ log.sensorTime }}</td>
          <td>{{ log.facilityName }}</td>
          <td>{{ log.category }}</td>
          <td>{{ log.value }}</td>
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
        <!-- Page number links -->
        <span
          class="page-item"
          v-for="page in pageCount"
          :key="page"
          :class="{ active: page - 1 === currentPage }"
        >
          <a class="page-link" href="#" @click.prevent="goToPage(page - 1)">{{
            page
          }}</a>
        </span>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, computed, ref, watch } from 'vue'
import http from '@/types/http'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { mapGetters } from 'vuex'
import VueDatePicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'

export default defineComponent({
  name: 'roadDashReportVue',
  computed: {
    ...mapGetters('auth', [
      'loginUser',
      'isLogin',
      'role',
      'accessToken',
      'refreshToken'
    ])
  },
  components: { VueDatePicker },
  created() {
    console.log('로그인한 사용자:', this.loginUser)
    console.log('로그인 상태:', this.isLogin)
    console.log('역할:', this.role)
    console.log('접근 토큰:', this.accessToken)
    console.log('갱신 토큰:', this.refreshToken)
  },

  setup() {
    const startDate = ref(new Date()) // 오늘 날짜를 초기값으로 설정
    const endDate = ref(new Date()) // 오늘 날짜를 초기값으로 설정
    const ITEMS_PER_PAGE = 10
    const currentPage = ref(0)
    const category = ref('')
    const facilityId = 10

    const paginatedData = computed(() => {
      const start = currentPage.value * ITEMS_PER_PAGE
      const end = start + ITEMS_PER_PAGE
      return logList.value.slice(start, end)
    })

    const pageCount = computed(() => {
      return Math.ceil(logList.value.length / ITEMS_PER_PAGE)
    })

    const nextPage = () => {
      if (currentPage.value < pageCount.value - 1) {
        currentPage.value++
      }
    }

    const prevPage = () => {
      if (currentPage.value > 0) {
        currentPage.value--
      }
    }

    const goToPage = (page: number) => {
      currentPage.value = page
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

    const store = useStore()

    // getters에서 nowUnderroad 가져오기
    // const nowUnderroad = computed(() => store.getters.nowUnderroad).value

    // const facility_id = computed(() => store.getters['auth/facilityId']).value

    let logList = ref<
      {
        id: number
        sensorTime: string
        facilityName: string
        category: string
        value: number
      }[]
    >([])

    const setList = () => {
      //   const facilityId = computed(() => store.getters['auth/facilityId']).value
      http
        .get(`/system/facilities/${facilityId}/sensors/HEIGHT/logs`, {})
        .then((res) => {
          logList.value = res.data
        })
        .catch((error) => {
          console.log(error)
        })
      console.log(setList)
    }
    const movePage = (alarm_id: any) => {
      console.log('Clicked on alarm_id:', alarm_id)
    }

    watch(category, (newCategory: any) => {
      //   const facilityId = computed(() => store.getters['auth/facilityId']).value
      if (newCategory) {
        http
          .get(
            `/system/facilities/${facilityId}/sensors/${newCategory}/logs`,
            {}
          )
          .then((res) => {
            logList.value = res.data
          })
          .catch((error) => {
            console.log(error)
          })
      }
    })

    watch([startDate, endDate], ([newStartDate, newEndDate]) => {
      let startDate = toLocalDateTime(newStartDate)
      let endDate = toLocalDateTime(newEndDate)
      console.log('Start Date:', startDate)
      console.log('End Date:', endDate)
    })

    onMounted(() => {
      setList()
    })
    return {
      logList: paginatedData,
      currentPage,
      movePage,
      nextPage,
      prevPage,
      goToPage,
      pageCount,
      category,
      startDate,
      endDate
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

.VueDatePicker {
  height: 50px;
  width: 50px;
}
</style>
