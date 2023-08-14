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
            <p>{{ reportInfo?.status }}</p>
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
      <button @click="goToUpdate(reportInfo?.id)">수정</button>
      <button @click="openCheckModal">삭제</button>
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
import { ref, defineComponent, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/types/apiClient'
import store from '@/store/index'
import router from '@/router'

export default defineComponent({
  name: 'roadReportItemVue',
  setup() {
    const route = useRoute()
    const report_id = route.params.report_id
    const reportInfo = ref(null)
    const imageList = ref(null)

    const apiClient = axios.apiClient(store)
    const api = axios.api

    // 글 삭제 전 비밀번호 체크를 위한 변수
    const pwCheckVisible = ref(false) // 비밀번호 체크창이 보이는지 여부
    const inputPassword = ref(null) // 사용자가 입력한 비밀번호

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
        })
        .catch((err) => console.log(err))
    }

    function goReportList() {
      router.push({ path: '/road/report' })
    }

    function goToUpdate(report_id: any) {
      router.push({ path: `/road/report/update/${report_id}` })
    }

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

    onMounted(() => {
      getReportData()
    })

    return {
      report_id,
      reportInfo,
      imageList,
      inputPassword,
      pwCheckVisible,
      openCheckModal,
      getReportData,
      goReportList,
      goToUpdate,
      deleteReport
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
