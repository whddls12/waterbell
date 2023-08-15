<template>
  <div class="each-report">
    <!-- 게시판 이름과 목록관련버튼들 -->
    <div class="report-header">
      <div class="board-title">
        <h2>신고접수</h2>
      </div>
      <div class="list-btn">
        <button class="list-btn-list" @click="goReportList">목록</button>
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
            <p class="into-status">{{ reportInfo?.name }}</p>
            <p>{{ reportInfo?.createDate }}</p>
          </div>
          <div class="report-info info-box">
            <select
              v-if="role == 'PUBLIC_MANAGER'"
              name="report-status"
              v-model="selectedStatus"
              class="custom-select"
            >
              <option
                v-for="(status, index) in statusList"
                :key="index"
                :value="status.value"
              >
                {{ status.text }}
              </option>
            </select>
            <p class="info-status" v-else>{{ reportInfo?.status }}</p>
            <p style="margin: 5px">{{ reportInfo?.viewCount }}</p>
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
        <button
          class="manager-btn-modify"
          @click="statusUpdate(reportInfo?.id)"
        >
          수정
        </button>
        <button class="btn-delete" @click="deleteReportManager">삭제</button>
      </div>
      <!-- 작성자 -->
      <div v-else>
        <button class="btn-modify" @click="goToUpdate(reportInfo?.id)">
          수정
        </button>
        <button class="btn-delete" @click="openCheckModal">삭제</button>
      </div>
    </div>
    <!-- 삭제 전 비밀번호 확인 -->
    <div class="password-check-modal" v-if="pwCheckVisible">
      <label for="password-check">삭제하시려면 비밀번호를 입력해주세요</label
      ><br />
      <input
        class="password-input"
        type="password"
        id="password-check"
        v-model="inputPassword"
      />
      <button class="password-check-btn" @click="deleteReport">확인</button>
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

.list-btn-list {
  display: flex;
  width: 101px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  border: 1px solid var(--1, #10316b);
  color: var(--1, #10316b);
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  background-color: #f2f7ff;
}

.report-header {
  border-bottom: 2px solid black;
}

.report-body {
  border-bottom: 2px solid black;
}

.report-title-box {
  border-bottom: 1px solid #939393;
  text-align: start;
}

.report-info {
  display: flex;
  justify-content: space-between;
  color: var(--unnamed, #939393);
  text-align: center;

  font-size: 15px;
  font-style: normal;
  font-weight: 500;
  line-height: 20px; /* 140% */
  letter-spacing: 0.25px;
}

.report-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding: 20px;
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

.btn-modify {
  width: 101px;
  margin: 10px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  /* background: #ffa132; */
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  border: 1px solid var(--unnamed, #ffa132);
  background-color: #ffa132;
  color: #fff;
  text-align: center;
}

.btn-delete {
  margin: 10px 10px 10px 0;
  width: 101px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  border: 1px solid var(--unnamed, #ffa132);
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  background-color: white;
  color: var(--unnamed, #ffa132);
  text-align: center;
}

.report-title {
  color: #000;
  font-size: 20px;
  font-style: normal;
  font-weight: 500;
  line-height: 28px; /* 140% */
  letter-spacing: 0.25px;
  margin: 5px;
}

.info-status {
  color: var(--ing, #0d7e83);
}

.password-check-modal {
  gap: 10px;
}

.password-input {
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.2);
  background: rgba(217, 217, 217, 0);
  line-height: 10px; /* 128.571% */
}

.password-check-btn {
  margin: 5px;
  width: 101px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  border: 1px solid var(--1, #10316b);
  color: var(--1, #10316b);
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  background-color: white;
}

.manager-btn-modify {
  width: 101px;
  margin: 10px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 10px;
  /* background: #ffa132; */
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 10px; /* 128.571% */
  letter-spacing: 0.1px;
  border: 1px solid var(--unnamed, #ffa132);
  background-color: #ffa132;
  color: #fff;
  text-align: center;
}

.custom-select {
  height: 30px;
  width: 100%;
  padding: px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background-color: #fff;
  appearance: none;
  cursor: pointer;
  color: var(--ing, #0d7e83);
  text-align: center;
  text-align: center;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
}

.custom-select:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.25);
}

.custom-select:after {
  content: '\25BC';
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  pointer-events: none;
  color: #888;
  color: var(--ing, #0d7e83);
  text-align: center;
}

/* Hover effect */
.custom-select:hover {
  border-color: #999;
}

/* Disabled state */
.custom-select:disabled {
  background-color: #f0f0f0;
  cursor: not-allowed;
}

/* Styling for options */
option {
  background-color: #fff;
  color: #333;
  color: var(--ing, #0d7e83);
  text-align: center;
}

/* Hover effect for options */
option:hover {
  background-color: #007bff;
  color: #fff;
}
</style>
