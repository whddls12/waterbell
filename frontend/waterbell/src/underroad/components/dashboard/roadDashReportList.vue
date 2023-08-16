<template>
  <div class="container">
    <div class="dash-box">
      <div class="dash-box-title">
        <i class="fas fa-bell fa-lg"></i>
        <h4>최근 신고접수 내역</h4>
      </div>
      <div class="dash-box-content">
        <thead>
          <tr>
            <th scope="col" class="text-center" style="width: 200px">제목</th>
            <th scope="col" class="text-center" style="width: 100px">
              처리상태
            </th>
            <th scope="col" class="text-center" style="width: 150px">
              등록일시
            </th>
          </tr>
        </thead>
        <tbody v-if="reportList.length != 0">
          <tr
            v-for="(report, index) in reportList"
            :key="index"
            class="tr"
            @click="movePage(report.id)"
            align="center"
            onmousemove="this.style.backgroundColor='#f0f0f0';"
            onmouseout="this.style.backgroundColor='transparent';"
          >
            <!-- <th scope="row">{{ no }}</th> -->
            <td class="report-title-summary">
              {{ truncateText(report.title, 10) }}
            </td>
            <td
              :class="{
                'text-primary': report.status == 'COMPLETE',
                'text-success': report.status == 'PROCESSING',
                'text-danger': report.status == 'BEFORE'
              }"
            >
              {{ statusEngToKr(report.status) }}
            </td>
            <td>{{ setDate(report.createDate) }}</td>
          </tr>
        </tbody>

        <tbody v-else>
          <td colspan="4" class="text-center">등록된 신고접수가 없습니다.</td>
        </tbody>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, computed, ref } from 'vue'
import store from '@/store/index'
import axios from '@/types/apiClient'
import { useRouter } from 'vue-router'

export default defineComponent({
  name: 'roadDashReportVue',

  setup() {
    // const apiClient = axios.apiClient(store)
    const api = axios.api

    const facility_id = computed(() => store.getters['auth/facilityId']).value

    let reportList = ref<
      {
        board_id: string
        title: string
        status: string
        create_date: string
      }[]
    >([])
    //  로 바꿔보기
    const setList = () => {
      try {
        api.get(`/reports/dash/${facility_id}`).then((res) => {
          //가져온 신고접수 리스트 데이터를 준비된 배열에 넣기.
          // console.log(res.data.list)
          reportList.value = res.data.list
        })
      } catch (error) {
        return
      }
    }

    const setDate = (dateString: any) => {
      const date = new Date(dateString)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0') // +1 이유: getMonth()는 0-11의 값을 반환하기 때문입니다.
      const day = String(date.getDate()).padStart(2, '0')

      return `${year}-${month}-${day}`
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
    const router = useRouter()
    const movePage = (board_id: any) => {
      router.push(`/road/report/${board_id}/detail`)
    }
    // 신고접수 제목 일정 글자 이상 ... 처리
    const truncateText = (text: any, maxLength: any) => {
      if (text.length > maxLength) {
        return text.slice(0, maxLength) + '...'
      }
      return text
    }

    onMounted(() => {
      setList()
    })
    return {
      reportList,
      movePage,
      setList,
      setDate,
      statusEngToKr,
      truncateText
    }
  }
})
</script>
<style scoped lang="css">
.report-title-summary {
  text-align: start;
}

thead tr:last-child th {
  border-bottom: 1px solid #939393;
}

tbody tr {
  padding: 10px 0; /* 위아래로 10px 패딩 추가 */
}

tbody td {
  padding: 10px; /* 모든 방향으로 10px 패딩 추가 */
}

tbody:hover {
  cursor: pointer;
}
</style>
