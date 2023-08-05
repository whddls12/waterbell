<template lang="">
  <div class="container mt-4">
    <div><h5>최근 신고접수 내역</h5></div>
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
import { defineComponent, onMounted, computed, ref } from 'vue'
import http from '@/types/http'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { mapGetters } from 'vuex'

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
  created() {
    console.log('로그인한 사용자:', this.loginUser)
    console.log('로그인 상태:', this.isLogin)
    console.log('역할:', this.role)
    console.log('접근 토큰:', this.accessToken)
    console.log('갱신 토큰:', this.refreshToken)
  },

  setup() {
    const ITEMS_PER_PAGE = 10
    const currentPage = ref(0)

    const paginatedData = computed(() => {
      const start = currentPage.value * ITEMS_PER_PAGE
      const end = start + ITEMS_PER_PAGE
      return AlarmList.value.slice(start, end)
    })

    const pageCount = computed(() => {
      return Math.ceil(AlarmList.value.length / ITEMS_PER_PAGE)
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
    const store = useStore()

    // getters에서 nowUnderroad 가져오기
    const nowUnderroad = computed(() => store.getters.nowUnderroad).value

    const facility_id = nowUnderroad.id

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

    const setList = () => {
      const role = computed(() => store.getters['auth/role']).value
      const token = computed(() => store.getters['auth/accessToken']).value
      console.log(token)
      http
        .get(`/alarm/${role}`, {
          headers: {
            Authorization: `${token}`
          }
        })
        .then((res) => {
          AlarmList.value = res.data
        })
      console.log('ㅇㅇ')
      console.log(AlarmList)
    }
    const router = useRouter()
    const movePage = (alarm_id: any) => {
      console.log('Clicked on alarm_id:', alarm_id)
      router.push(`/alarm/detail/${alarm_id}`)
    }

    onMounted(() => {
      setList()
    })
    return {
      AlarmList: paginatedData,
      currentPage,
      movePage,
      nextPage,
      prevPage,
      goToPage,
      pageCount
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
</style>
