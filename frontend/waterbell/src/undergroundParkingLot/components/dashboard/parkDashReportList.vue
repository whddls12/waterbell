<template>
  <div class="container">
    <div class="dash-box">
      <div class="dash-box-title">
        <h4>최근 신고접수 내역</h4>
      </div>
      <div class="dash-box-content">
        <thead>
          <tr>
            <th scope="col" class="text-center">제목</th>
            <th scope="col" class="text-center">처리상태</th>
            <th scope="col" class="text-center">등록일시</th>
          </tr>
        </thead>
        <tbody v-if="reportList.length != 0">
          <tr
            v-for="(report, index) in reportList"
            :key="index"
            class="tr"
            @click="movePage(report.id)"
            align="center"
          >
            <!-- <th scope="row">{{ no }}</th> -->
            <td>{{ report.title }}</td>
            <td>{{ report.status }}</td>
            <td>{{ report.createDate }}</td>
          </tr>
        </tbody>

        <tbody v-if="reportList.length == 0">
          <td colspan="4" class="text-center">등록된 신고접수가 없습니다.</td>
        </tbody>

        <!-- <i class="far fa-bell"></i>
        <h3>신고접수 내역</h3> -->
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, onMounted, computed, ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from '@/types/apiClient'
import store from '@/store/index'
// import { setAxios } from '@/types/authFunctionModule'

export default defineComponent({
  name: 'roadDashReportVue',

  setup() {
    const apiClient = axios.apiClient(store)
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
        apiClient.get(`reports/dash/apart/${facility_id}`).then((res) => {
          //가져온 신고접수 리스트 데이터를 준비된 배열에 넣기.
          console.log(res.data.list)
          reportList.value = res.data.list
        })
      } catch (error) {
        // hasReport.value = false
        console.log(error.response)
      }
    }
    const router = useRouter()
    const movePage = (board_id: any) => {
      router.push(`/park/report/${board_id}/detail`)
    }
    onMounted(() => {
      setList()
    })
    return {
      reportList,
      movePage,
      setList
    }
  }
})
</script>
<style lang="css"></style>
