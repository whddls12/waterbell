<template lang="">
  <div class="container">
    <div class="title">입주민 관리</div>
    <div class="searchBox">
      <div>
        <label>호수</label>
        <input />
      </div>
      <div>
        <label>이름</label>
        <input />
      </div>
    </div>
    <table class="table table-hover table-bordered table-bordered">
      <thead class="thead-dark">
        <tr>
          <th scope="col" class="text-center" style="width: 50px">번호</th>
          <th scope="col" class="text-center" style="width: 400px">호수</th>
          <th scope="col" class="text-center" style="width: 150px">이름</th>
          <th scope="col" class="text-center" style="width: 150px">전화번호</th>
          <th scope="col" class="text-center" style="width: 150px">가입일자</th>
          <th scope="col" class="text-center" style="width: 150px">관리</th>
        </tr>
      </thead>
      <tbody v-if="memberList && memberList.length">
        <tr
          v-for="(member, index) in memberList"
          :key="member.id"
          class="tr"
          align="center"
        >
          <td>{{ index + 1 }}</td>
          <td>{{ member.addressNumber }}호</td>
          <td>{{ member.name }}</td>
          <td>{{ member.phone }}</td>
          <td>{{ setCreatedAt(member.createdAt) }}</td>
          <td>
            <button id="deleteBtn" @click="deleteMember(member.id)">
              삭제
            </button>
          </td>
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
      </div>
    </div>
    <!-- 다음 페이지 그룹으로 이동 -->
    <span
      v-if="pageNavigation.endPage < pageNavigation.totalPageCnt"
      @click.prevent="goToPage(pageNavigation.next)"
    >
      &raquo;
    </span>
    <!-- </div>
    </div> -->
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, computed, ref, watch } from 'vue'
import store from '@/store/index'
import axios from '@/types/apiClient'
import '@vuepic/vue-datepicker/dist/main.css'

export default defineComponent({
  name: 'manageMember',

  setup() {
    const apiClient = axios.apiClient(store)

    const currentPage = ref(1)
    const facilityId = computed(() => store.getters['auth/facilityId']).value
    let serachName = ref('') //검색이름
    let searchAddress = ref('') //검색호수
    let apartCode = ref('')

    let memberList = ref<
      {
        id: number
        loginId: string
        createdAt: string
        state: string
        role: number
        expiredAt: string | null
        phone: string
        platformType: string | null
        name: string
        addressedNumber: string
        system: boolean
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

    const setApartCode = async () => {
      // console.log('setApartCode실행')
      // 변경: async로 선언
      try {
        const res = await apiClient.get(`/facilities/${facilityId}/apart`)
        apartCode.value = res.data.apart.apartCode // 변경: apartCode 값을 여기서 설정
        // console.log(apartCode.value)
      } catch (error) {
        console.error('Error fetching apartCode:', error)
      }
    }

    const setList = () => {
      apiClient
        .post(
          `/management/apartManager/memberList`,

          {
            apartCode: apartCode.value,
            page: 1
          }
        )
        .then((res) => {
          console.log(res.data)
          memberList.value = res.data.list
          pageNavigation.value = res.data.pageNavigation
          console.log('memberList 잘 들어감? ')
          console.log(memberList.value)
        })
        .catch((error) => {
          console.log(error)
        })
    }

    const setCreatedAt = (dateString) => {
      const date = new Date(dateString)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0') // +1 이유: getMonth()는 0-11의 값을 반환하기 때문입니다.
      const day = String(date.getDate()).padStart(2, '0')

      return `${year}-${month}-${day}`
    }
    const deleteMember = (id) => {
      if (confirm('입주민 목록에서 삭제하시겠습니까?')) {
        apiClient.get(`/member/disable/apart_member/${id}`).then((res) => {
          console.log(Request)
          if (res.data.notification == '관리인 해제 완료') {
            console.log(res.data)
            alert('입주민 삭제가 완료되었습니다.')
          }
        })
      }
    }
    const range = (start: number, end: number) => {
      return [...Array(end - start + 1).keys()].map((val) => val + start)
    }

    const movePage = (alarm_id: any) => {
      console.log('Clicked on alarm_id:', alarm_id)
    }

    const goToPage = (page: number) => {
      currentPage.value = page

      try {
        apiClient
          .get(`/management/apartManager/memberList`, {
            apartCode: apartCode.value,
            page: currentPage.value
          })
          .then((res) => {
            memberList.value = res.data.list
            pageNavigation.value = res.data.pageNavigation
          })
          .catch((error) => {
            console.log(error)
          })
      } catch (error) {
        console.error('Error fetching height data:', error)
      }
    }

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

    onMounted(async () => {
      await setApartCode()
      await setList()
    })

    return {
      currentPage,
      movePage,
      memberList,
      pageNavigation,
      range,
      goToPage,
      categoryLabel,
      setCreatedAt,
      deleteMember
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

.searchBox {
  display: flex;
  flex-direction: row;
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
</style>
