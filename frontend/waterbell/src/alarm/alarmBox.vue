<template lang="">
  <div class="container mt-4">
    <div><h5>최근 신고접수 내역</h5></div>
    <table class="table table-hover table-bordered">
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
          :key="alarm.alarm_id"
          class="tr"
          @click="movePage(alarm.alarm_id)"
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
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, computed, ref } from 'vue'
import http from '@/types/http'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'

export default defineComponent({
  name: 'roadDashReportVue',

  setup() {
    const store = useStore()

    // getters에서 nowUnderroad 가져오기
    const nowUnderroad = computed(() => store.getters.nowUnderroad).value

    const facility_id = nowUnderroad.id

    let AlarmList = ref<
      {
        alarm_id: string
        content: string
        alarmType: string
        sender: string
        regDate: string
        status: string
      }[]
    >([])

    const setList = () => {
      const token =
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3Iiwicm9sZSI6WyJQVUJMSUNfTUFOQUdFUiJdLCJpYXQiOjE2OTEwMjYwNTksImV4cCI6MTY5MTAyOTY1OX0.J38jUW9xZUw9tGvlnD0DCKmfqlVlu5ABnkERbAxP3yM' // 실제 사용할 토큰 값으로 변경

      http
        .get(`alarm/APART_MEMBER`, {
          headers: {
            Authorization: token
          }
        })
        .then((res) => {
          AlarmList.value = res.data
        })

      console.log('ㅇㅇ')
      console.log(AlarmList)
    }
    const router = useRouter()
    const movePage = (board_id: any) => {
      router.push(`/road/report/item?board_id=${board_id}`)
    }
    onMounted(() => {
      setList()
    })
    return {
      AlarmList,
      movePage,
      setList
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
</style>