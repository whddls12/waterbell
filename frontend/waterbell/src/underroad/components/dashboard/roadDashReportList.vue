<template lang="">
  <div class="container">
    <div><h5>최근 신고접수 내역</h5></div>
    <div>
      <thead>
        <tr>
          <th scope="col" class="text-center">제목</th>
          <th scope="col" class="text-center">처리상태</th>
          <th scope="col" class="text-center">등록일시</th>
        </tr>
      </thead>
      <tbody v-if="reportList.length != 0">
        <!-- <tr

        >

</tr> -->

        <tr
          v-for="board in reportList"
          :key="board.board_id"
          :title="board.title"
          :status="board.status"
          :dateTime="board.create_date"
          class="tr"
          @click="movePage(board.board_id)"
          align="center"
        >
          <th scope="row">{{ no }}</th>
          <td>{{ title }}</td>
          <td>{{ status }}</td>

          <td>{{ dateTime }}</td>
        </tr>
      </tbody>

      <tbody v-if="reportList.length == 0">
        <td colspan="4" class="text-center">등록된 신고접수가 없습니다.</td>
      </tbody>
    </div>
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

    let reportList = ref<
      {
        board_id: string
        title: string
        status: string
        create_date: string
      }[]
    >([])

    const setList = () => {
      http.get(`dash/facilities/{facility_id}/reports`).then((res) => {
        //가져온 신고접수 리스트 데이터를 준비된 배열에 넣기.
        reportList.value = res.data
      })
    }
    const router = useRouter()
    const movePage = (board_id: any) => {
      router.push(`/road/report/item?board_id=${board_id}`)
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
