<template>
  <div class="report-create-container">
    <div class="report-page-title"><h1>신고접수 글쓰기</h1></div>
    <div class="report-write">
      <div class="report-box name">
        <div class="report-subtitle">이름</div>
        <div class="report-inputbox">
          <input type="text" v-model="report.name" />
        </div>
      </div>
      <div class="report-box phoneNumber">
        <div class="report-subtitle">휴대폰번호</div>
        <div class="report-inputbox">
          <input type="text" v-model="report.phone" />
        </div>
      </div>
      <div class="report-box password">
        <div class="report-subtitle">비밀번호</div>
        <div class="report-inputbox">
          <input type="text" v-model="report.boardPassword" />
        </div>
      </div>
      <div class="report-box title">
        <div class="report-subtitle">제목</div>
        <div class="report-inputbox">
          <input type="text" v-model="report.title" />
        </div>
      </div>
      <div class="report-box content">
        <div class="report-subtitle">신고내용</div>
        <div class="report-inputbox">
          <input type="text" v-model="report.content" />
        </div>
      </div>
      <!-- <div class="report-box attachment">
        <div class="report-subtitle">파일첨부</div>
        <div class="report-filebox">
          <div class="report-file-list">
            <div class="report-file-list-name">
              {{ selectedFiles }}
              첨부된 파일 목록 나오는 곳(지하차도1.jpg, 지하차도2.jpg, ... )
            </div>
          </div>
          <div class="report-file-attach">
            <div class="search-btn">
              <input
                type="file"
                ref="fileInput"
                style="display: none"
                @change="onFileChange"
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
        </div> -->
      <!-- </div> -->
    </div>
    <div class="report-btn">
      <button>취소</button>
      <button @click="writeReport()">등록</button>
    </div>
  </div>
</template>

<script lang="ts">
import { ref, computed, defineComponent } from 'vue'
import { useStore } from 'vuex'
import http from '@/types/http'

export default defineComponent({
  name: 'roadReportCreateVue',
  setup() {
    const store = useStore()

    // getters에서 nowUnderroad 가져오기
    const nowUnderroad = computed(() => store.getters.nowUnderroad).value

    const facility_id = nowUnderroad.id

    let report = ref({
      name: '',
      phone: '',
      boardPassword: '',
      title: '',
      content: '',
      uploadedfiles: null
    })

    // const fileInputRef = ref<HTMLInputElement | null>(null)
    // const selectedFiles = ref<File[]>([]) // 담긴 첨부파일을 저장할 변수

    // function onFileChange() {
    //   console.log('파일 저장함수 실행?')
    //   // 사용자가 첨부한 파일들 저장
    //   console.log(fileInputRef.value)
    //   if (fileInputRef.value && fileInputRef.value.files) {
    //     selectedFiles.value = Array.from(fileInputRef.value.files)
    //   } else {
    //     selectedFiles.value = []
    //   }
    //   // 첨부한 파일들을 보여주기 위함
    //   const fileListName = fileInputRef.value?.nextElementSibling
    //   if (fileListName) {
    //     fileListName.textContent = getSelectedFileNames()
    //   }
    // }

    // // 담긴 첨부파일들의 이름
    // function getSelectedFileNames() {
    //   return selectedFiles.value.map((file) => file.name).join(', ')
    // }

    function writeReport() {
      // FormData 객체 만들기
      const formData = new FormData()
      // FormData에 양식 필드 넣기
      console.log(report.value)
      formData.append('name', report.value.name)
      formData.append('phone', report.value.phone)
      formData.append('boardPassword', report.value.boardPassword)
      formData.append('title', report.value.title)
      formData.append('content', report.value.content)
      console.log(formData)

      // FormData에 첨부파일 넣기
      // if (selectedFiles.value) {
      //   for (let i = 0; i < selectedFiles.value.length; i++) {
      //     const file = selectedFiles.value[i]
      //     formData.append('uploadedfiles', file)
      //   }
      // }
      const config = {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      }
      http
        .post(`http://localhost:8080/reports/write/1`, formData, config)
        .then((response) => {
          if (response.data.success) {
            console.log(response)
          }
        })
        .catch(function (error) {
          console.log(error)
        })
    }
    return {
      report,
      // fileInputRef,
      // selectedFiles,
      // onFileChange,
      // getSelectedFileNames,
      writeReport
    }
  }
})
</script>

<style>
.report-create-container {
  width: 100%;
}

.report-page-title {
  border-bottom: 2px solid black;
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

.report-inputbox {
  padding: 10px 10px;
}

.report-file-list-name {
  border: 1px solid #939393;
  padding: 10px;
  color: #939393;
  text-align: start;
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

/* 하단 버튼 */
.report-btn {
  margin-top: 10px;
}

.report-btn > button {
  margin: 10px;
}

.search-btn > button {
  width: 100%;
  height: 100%;
  background-color: white;
}

.attach-notice > p {
  font-size: 8px;
  text-align: start;

  padding-left: 10px;
}
</style>
