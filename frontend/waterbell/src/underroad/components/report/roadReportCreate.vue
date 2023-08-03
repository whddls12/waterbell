<template>
  <div class="report-create-container">
    <div class="report-page-title"><h1>신고접수 글쓰기</h1></div>
    <div class="report-write">
      <div class="report-box name">
        <div class="report-subtitle">이름</div>
        <div class="report-inputbox"><input type="text" /></div>
      </div>
      <div class="report-box phoneNumber">
        <div class="report-subtitle">휴대폰번호</div>
        <div class="report-inputbox"><input type="text" /></div>
      </div>
      <div class="report-box password">
        <div class="report-subtitle">비밀번호</div>
        <div class="report-inputbox"><input type="text" /></div>
      </div>
      <div class="report-box title">
        <div class="report-subtitle">제목</div>
        <div class="report-inputbox"><input type="text" /></div>
      </div>
      <div class="report-box content">
        <div class="report-subtitle">신고내용</div>
        <div class="report-inputbox"><input type="text" /></div>
      </div>
      <div class="report-box attachment">
        <div class="report-subtitle">파일첨부</div>
        <div class="report-filebox">
          <div class="report-file-list">
            <div class="report-file-list-name">
              첨부된 파일 목록 나오는 곳(지하차도1.jpg, 지하차도2.jpg, ... )
            </div>
          </div>
          <div class="report-file-attach">
            <div class="search-btn">
              <button>파일 찾기</button>
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
      <button>취소</button>
      <button>등록</button>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'roadReportCreateVue'
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
