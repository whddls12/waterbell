<template>
  <div class="report-create-container">
    <div class="report-page-title">
      <h1>신고접수 수정</h1>
      <div class="report-empty-box"></div>
    </div>
    <div class="report-write">
      <div class="report-box name">
        <div class="report-subtitle"><h5 style="margin: 0px">이름</h5></div>
        <div class="report-inputbox">
          <input type="text" v-model="report.name" />
        </div>
      </div>
      <div class="report-box phoneNumber">
        <div class="report-subtitle">
          <h5 style="margin: 0px">휴대폰번호</h5>
        </div>
        <div class="report-inputbox">
          <input type="text" v-model="report.phone" />
        </div>
      </div>
      <div class="report-box password">
        <div class="report-subtitle"><h5 style="margin: 0px">비밀번호</h5></div>
        <div class="report-inputbox">
          <input type="password" v-model="report.boardPassword" />
        </div>
      </div>
      <div class="report-box password-confirm">
        <div class="report-subtitle">
          <h5 style="margin: 0px">비밀번호 확인</h5>
        </div>
        <div class="report-inputbox">
          <input type="password" v-model="report.boardPasswordConfirm" />
          <p :class="{ 'p-msg': true, success: validate.confirmPass }">
            {{ confirmPassMsg }}
          </p>
        </div>
      </div>
      <div class="report-box titleName">
        <div class="report-subtitle"><h5 style="margin: 0px">제목</h5></div>
        <div class="report-inputbox">
          <input type="text" style="width: 800px" v-model="report.title" />
        </div>
      </div>
      <div class="report-box content">
        <div class="report-subtitle"><h5 style="margin: 0px">신고내용</h5></div>
        <div class="report-inputbox">
          <textarea
            name="content"
            rows="5"
            cols="40"
            v-model="report.content"
          ></textarea>
          <!-- <input type="text" v-model="report.content" /> -->
        </div>
      </div>
      <div class="report-box attachment">
        <div class="report-subtitle"><h5 style="margin: 0px">파일첨부</h5></div>
        <div class="report-filebox">
          <div class="report-file-list">
            <div class="report-file-list-name">
              <div v-for="(image, index) in imageList" :key="index">
                {{ image?.imageName }}
                <i @click="deleteFile(image?.id)" class="fas fa-backspace"></i>
              </div>
              <div v-for="(file, index) in selectedFiles" :key="index">
                {{ file.name }}
                <i
                  @click="unselectFile(file.name)"
                  class="fas fa-backspace"
                ></i>
              </div>
            </div>
          </div>
          <div class="report-file-attach">
            <div class="search-btn">
              <input
                type="file"
                ref="fileInput"
                accept="image/*"
                style="display: none"
                multiple
                @change="upload($event)"
              />
              <button @click="$refs.fileInput.click()">파일 찾기</button>
            </div>
            <div class="attach-notice">
              <p>* 첨부파일 용량은 최대 10MB를 초과할 수 없습니다.</p>
              <p>
                * 첨부 가능 파일 : JPG, GIF, JPEG, PEG, PNG, DOC, HUN, HWP, PDF,
                DOCX, PPT, PPTX, PTF
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="report-btn">
      <button class="cancel-btn" @click="goBack">취소</button>
      <button class="ok-btn" @click="updateReport()">수정</button>
    </div>
  </div>
</template>

<script lang="ts">
import { ref, watch, computed, onMounted, defineComponent } from 'vue'
import router from '@/router/index'
import axios from '@/types/apiClient'
import store from '@/store/index'
import { useRoute } from 'vue-router'

export default defineComponent({
  name: 'roadReportCreateVue',
  setup() {
    const route = useRoute()
    const report_id = route.params.report_id
    const imageList = ref(null)
    const removeFilesList = ref<string[]>([])

    const facility_id = computed(() => store.getters['auth/facilityId'])

    const nowUnderroad = computed(() => store.getters.nowUnderroad).value

    const apiClient = axios.apiClient(store)
    const api = axios.api

    let report = ref({
      name: '',
      phone: '',
      boardPassword: '',
      boardPasswordConfirm: '',
      title: '',
      content: '',
      uploadedfiles: null
    })

    const fileInputRef = ref<HTMLInputElement | null>(null)
    const selectedFiles = ref<File[]>([]) // 담긴 첨부파일을 저장할 변수
    // FormData 객체 만들기
    let formData = new FormData()

    // 기존 접수글 데이터 가져와서 저장
    function getReportData() {
      api
        .get(`/reports/undergroundRoad/detail/${report_id}`)
        .then((res) => {
          imageList.value = res.data.imageList
          report.value.name = res.data.board.name
          report.value.phone = res.data.board.phone
          report.value.boardPassword = res.data.board.boardPassword
          report.value.title = res.data.board.title
          report.value.content = res.data.board.content
        })
        .catch((err) => console.log(err))
    }

    // 사용자가 첨부한 파일들 저장
    function upload(event: any) {
      console.log('파일 저장함수 실행?')
      const files = event.target.files
      if (files && files.length > 0) {
        for (const file of files) {
          selectedFiles.value.push(file)
          formData.append('addUploadedfiles', file)
        }
        console.log(selectedFiles.value)

        // formData에 데이터 잘 들어가는지 확인
        // for (const values of formData.values()) {
        //   console.log(values)
        // }
      }
    }

    // 담긴 첨부파일들의 이름을 반환
    const getSelectedFileNames = computed(() => {
      const selectedFileNames = Array.from(selectedFiles.value)
        .map((file: any) => file.name)
        .join(', ')

      return selectedFileNames
    })
    // 기존에 있던 첨부파일에서 파일 제거
    function deleteFile(image_id: any) {
      removeFilesList.value.push(image_id)
      console.log(removeFilesList.value)
    }

    function unselectFile(file_name: any) {
      for (let file of selectedFiles.value) {
        if (file.name === file_name) {
          selectedFiles.value.splice(selectedFiles.value.indexOf(file), 1)
          break
        }
      }
      console.log(selectedFiles.value)
    }

    function goBack() {
      router.go(-1)
    }

    // 신고접수 등록
    async function updateReport() {
      // FormData에 양식에 채워진 값들 넣기 (필수값)
      formData.append('name', report.value.name)
      formData.append('title', report.value.title)
      formData.append('content', report.value.content)
      formData.append('boardPassword', report.value.boardPassword)

      // FormData에 삭제할 첨부파일의 id 담아주기
      for (let value of removeFilesList.value) {
        console.log(value)
        formData.append('removefiles', value)
      }

      // formData 의 밸류값을 확인하는 방법
      for (let values of formData.entries()) {
        console.log(values[0] + ', ' + values[1])
      }

      if (validate.value.confirmPass) {
        // 신고접수 수정하는 요청보내기
        await api
          .post(`/reports/update/${report_id}`, formData, {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          })
          .then((response) => {
            router.push(`/road/report/${report_id}/detail`)
          })
          .catch(function (error) {
            console.log(error)
          })
      } else {
        alert('비밀번호 확인이 일치하지 않습니다.')
      }
    }

    const validate = ref({
      confirmPass: false // 비밀번호와 일치하는가
    })
    const confirmPassMsg = ref('')

    const validateConfirmPass = () => {
      if (report.value.boardPasswordConfirm == '') {
        validate.value.confirmPass = false
        confirmPassMsg.value = '비밀번호 확인을 입력하세요.'
      } else if (
        report.value.boardPasswordConfirm != report.value.boardPassword
      ) {
        validate.value.confirmPass = false
        confirmPassMsg.value = '비밀번호 확인이 일치하지 않습니다.'
      } else if (
        report.value.boardPasswordConfirm == report.value.boardPassword
      ) {
        validate.value.confirmPass = true
        confirmPassMsg.value = '비밀번호 확인이 일치합니다.'
      }
    }

    // 비밀번호 확인 검증을 위한 watch 함수
    watch(
      () => report.value.boardPasswordConfirm,
      (newValue: any, oldValue: any) => {
        if (newValue != oldValue) {
          validateConfirmPass()
        }
      }
    )

    onMounted(() => {
      getReportData()
    })
    return {
      validate,
      confirmPassMsg,
      report,
      imageList,
      fileInputRef,
      selectedFiles,
      removeFilesList,
      upload,
      deleteFile,
      unselectFile,
      getSelectedFileNames,
      goBack,
      updateReport,
      getReportData,
      validateConfirmPass
    }
  }
})
</script>

<style scoped>
.report-create-container {
  width: 100%;
}

.report-page-title {
  color: #000;
  text-align: center;
  /* font-family: Roboto; */
  font-size: 30px;
  font-style: normal;
  font-weight: 600;
  line-height: 16px; /* 53.333% */
  letter-spacing: 3px;
  border-bottom: 2px solid black;
}

.report-page-title h1 {
  /* font-family: Roboto; */
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 16px; /* 53.333% */
  letter-spacing: 3px;
}

.report-empty-box {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  height: 55px;
}

.report-empty-box-bottom {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  height: 40px;
}

/* 각 입력 컬럼 */
.report-box {
  display: flex;
  border-bottom: 1px solid #939393;
}

/* 각 컬럼명 */
.report-subtitle {
  width: 150px;
  border-right: 1px solid #939393;
  padding: 10px 10px;
}

/* .report-inputbox {
  padding: 10px 10px;
} */

.report-file-list > .report-file-list-name {
  width: 800px;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 20%);
  /* background: rgba(217, 217, 217, 0); */
  /* border: 1px solid #939393; */
  padding: 10px;
  /* height: 28px; */
  /* color: #939393; */
  text-align: start;
  display: flex;
  gap: 10px;
}

.report-filebox {
  display: flex;
  flex-direction: column;
}

.report-filebox > div {
  padding: 10px;
}

.report-file-attach {
  display: flex;
}

.search-btn > button {
  width: 100%;
  height: 100%;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 0.4);
  background: rgba(217, 217, 217, 0);
}

.search-btn {
  width: 93px;
  height: 36px;
  flex-shrink: 0;
}

.attach-notice {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 1px;
  margin-bottom: 20px;
}

.attach-notice > p {
  font-size: 8px;
  text-align: start;
  margin: 0px;
  padding-left: 10px;
}

.report-create-container h5 {
  color: #000;
  text-align: center;
  /* font-family: Roboto; */
  font-style: normal;
  font-weight: 600;
  font-size: 15px;
  line-height: 28px;
  letter-spacing: 0.25px;
  margin: 0px, 0px;
}

input {
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 20%);
  background: rgba(217, 217, 217, 0);
  margin: 10px;
}

textarea {
  width: 800px;
  border-radius: 8px;
  border: 1px solid rgba(0, 0, 0, 20%);
  background: rgba(217, 217, 217, 0);
  margin: 10px;
}

/* 하단 버튼 */
.report-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  /* 버튼 사이 간격 */
  align-self: stretch;
  margin-top: 10px;
}

.cancel-btn {
  display: flex;
  width: 90px;
  height: 32px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 8px;
  background: var(--unnamed, #939393);
  color: #fff;
  text-align: center;
  /* Button */
  /* font-family: Roboto; */
  font-size: 14px;
  font-style: normal;
  font-weight: 700;
  line-height: 20px; /* 142.857% */
  letter-spacing: 1.25px;
  text-transform: uppercase;
  border: 0;
}

.ok-btn {
  display: flex;
  width: 90px;
  height: 32px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 8px;
  background: var(--1, #10316b);
  color: #fff;
  text-align: center;
  /* Button */
  /* font-family: Roboto; */
  font-size: 14px;
  font-style: normal;
  font-weight: 700;
  line-height: 20px; /* 142.857% */
  letter-spacing: 1.25px;
  text-transform: uppercase;
  border: 0;
}

.report-create-container {
  width: 90%;
}
</style>
