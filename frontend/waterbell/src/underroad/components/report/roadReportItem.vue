<template>
  <div class="each-report">
    <!-- 게시판 이름과 목록관련버튼들 -->
    <div class="report-header">
      <div class="board-title">
        <h2>신고접수</h2>
      </div>
      <div class="list-btn">
        <button @click="goReportList">목록</button>
        <!-- <button>윗글</button>
        <button>아랫글</button> -->
      </div>
    </div>
    <!-- 신고접수 글 -->
    <div class="report-body">
      <div class="report-title-box">
        <div class="report-title">
          <p>{{ reportInfo?.title }}</p>
        </div>
        <div class="report-info">
          <div class="report-info info-box">
            <p>{{ reportInfo?.name }}</p>
            <p>{{ reportInfo?.createDate }}</p>
          </div>
          <div class="report-info info-box">
            <select
              v-if="role == 'PUBLIC_MANAGER'"
              name="report-status"
              v-model="selectedStatus"
            >
              <option
                v-for="(status, index) in statusList"
                :key="index"
                :value="status.value"
              >
                {{ status.text }}
              </option>
            </select>
            <p v-else>{{ reportInfo?.status }}</p>
            <p>{{ reportInfo?.viewCount }}</p>
          </div>
        </div>
      </div>
      <div class="report-content">
        <div class="content-text">
          {{ reportInfo?.content }}
        </div>
        <div class="content-image">
          <img
            class="report-image"
            v-for="(image, index) in imageList"
            :key="index"
            :src="image.url"
            :alt="image.imageName"
          />
        </div>
      </div>
    </div>
    <!-- 수정, 삭제 버튼 -->
    <div class="report-footer">
      <!-- 관리자 -->
      <div v-if="role == 'PUBLIC_MANAGER'" class="manager-btn">
        <button @click="statusUpdate(reportInfo?.id)">수정</button>
        <button @click="deleteReportManager">삭제</button>
      </div>
      <!-- 작성자 -->
      <div v-else>
        <button @click="goToUpdate(reportInfo?.id)">수정</button>
        <button @click="openCheckModal">삭제</button>
      </div>
    </div>
    <!-- 삭제 전 비밀번호 확인 -->
    <div class="password-check-modal" v-if="pwCheckVisible">
      <label for="password-check">삭제하시려면 비밀번호를 입력해주세요</label>
      <input type="password" id="password-check" v-model="inputPassword" />
      <button @click="deleteReport">확인</button>
    </div>
  </div>
</template>
<script lang="ts">
import { ref, defineComponent, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/types/apiClient'
import store from '@/store/index'
import router from '@/router'

export default defineComponent({
  name: 'roadReportItemVue',
  setup() {
    const route = useRoute()
    const report_id = route.params.report_id

    const role = computed(() => store.getters['auth/role']).value

    const reportInfo = ref(null) // 신고접수 글 데이터
    const imageList = ref(null) // 첨부파일 데이터

    const apiClient = axios.apiClient(store)
    const api = axios.api

    // 글 삭제 전 비밀번호 체크를 위한 변수
    const pwCheckVisible = ref(false) // 비밀번호 체크창이 보이는지 여부
    const inputPassword = ref(null) // 사용자가 입력한 비밀번호

    const reportStatus = ref<string | null>()
    // 신고접수 글 처리상태 리스트
    const statusList = [
      {
        text: 'BEFORE',
        value: '0'
      },
      {
        text: 'PROCESSING',
        value: '1'
      },
      {
        text: 'COMPLETE',
        value: '2'
      }
    ]
    const selectedStatus = ref('0')

    function openCheckModal() {
      pwCheckVisible.value = true
    }

    function getReportData() {
      api
        .get(`/reports/undergroundRoad/detail/${report_id}`)
        .then((res) => {
          console.log(res.data)
          imageList.value = res.data.imageList
          reportInfo.value = res.data.board
          if (res.data.board.status == 'BEFORE') {
            selectedStatus.value = '0'
          } else if (res.data.board.status == 'PROCESSING') {
            selectedStatus.value = '1'
          } else {
            selectedStatus.value = '2'
          }
        })
        .catch((err) => console.log(err))
    }

    function goReportList() {
      router.push({ path: '/road/report' })
    }

    function goToUpdate(report_id: any) {
      router.push({ path: `/road/report/update/${report_id}` })
    }

    function statusNumToStr() {
      console.log(selectedStatus.value)
      if (selectedStatus.value === '0') {
        reportStatus.value = 'BEFORE'
      } else if (selectedStatus.value === '1') {
        reportStatus.value = 'PROCESSING'
      } else {
        reportStatus.value = 'COMPLETE'
      }
      return reportStatus.value
    }

    async function statusUpdate(report_id: any) {
      const boardStatus = await statusNumToStr()
      console.log('처리상태 업데이트: ', boardStatus)
      apiClient
        .get(`/reports/publicManager/updateStatus/${report_id}/${boardStatus}`)
        .then((res) => {
          console.log(res)
          alert('처리상태가 변경되었습니다.')
        })
        .catch((err) => console.log(err))
    }

    // 글 삭제 (작성자)
    function deleteReport() {
      console.log('deleteReport')
      console.log('입력한 비밀번호: ', inputPassword.value)
      api
        .post(
          `/reports/undergroundRoad/board/password/validation/${report_id}`,
          {
            boardPassword: inputPassword.value
          }
        )
        .then((res) => {
          api
            .get(`/reports/deleteBoard/${report_id}`)
            .then((res) => {
              console.log(res)
              alert('글이 삭제되었습니다.')
              router.push({ path: '/road/report' })
            })
            .catch((err) => console.log(err))
        })
        .catch((err) => {
          alert('비밀번호가 일치하지 않습니다.')
          console.log(err)
        })
    }

    // 글 삭제 (관리자)
    function deleteReportManager() {
      apiClient
        .get(`/reports/publicManager/deleteBoard/${report_id}`)
        .then((res) => {
          console.log(res)
          alert('글이 삭제되었습니다.')
          router.push({ path: '/road/report' })
        })
        .catch((err) => console.log(err))
    }

    onMounted(() => {
      getReportData()
    })

    return {
      statusList,
      selectedStatus,
      role,
      report_id,
      reportInfo,
      reportStatus,
      imageList,
      inputPassword,
      pwCheckVisible,
      openCheckModal,
      getReportData,
      goReportList,
      goToUpdate,
      deleteReport,
      deleteReportManager,
      statusUpdate,
      statusNumToStr
    }
  }
})
</script>
<style>
.each-report {
  width: 100%;
  padding: 20px;
}

.each-report > div {
  padding: 10px;
}

.list-btn {
  display: flex;
  justify-content: start;
}

.report-header {
  border-bottom: 2px solid black;
}

.report-body {
  border-bottom: 2px solid black;
}

.report-title-box {
  border-bottom: 1px solid black;
  text-align: start;
}

.report-info {
  display: flex;
  justify-content: space-between;
}

.report-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.info-box {
  gap: 10px;
}

.report-image {
  width: 300px;
  height: 200px;
}

.report-footer {
  display: flex;
  justify-content: end;
}
</style>
