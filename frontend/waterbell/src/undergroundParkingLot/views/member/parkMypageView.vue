<template>
  <!-- 회원정보 조회 창 -->
  <!-- 수정하기 버튼을 누르기 전에는 입력도 수정도 되지 않도록 readonly 설정 사용 -->
  <div class="myPage">
    <div class="myPage-title">
      <h2>회원정보</h2>
    </div>
    <div class="myPage-content">
      <!-- 이름 -->
      <div class="myPage-content-box name">
        <label for="name">이름</label>
        <input type="text" id="name" readonly placeholder="이름 띄워주기" />
      </div>
      <!-- 아이디 -->
      <div class="myPage-content-box id">
        <label for="id">아이디</label>
        <input type="text" id="id" disabled />
      </div>
      <!-- 휴대폰번호 -->
      <div class="myPage-content-box phone">
        <label for="phone">휴대폰 번호</label>
        <input type="text" id="phone" disabled />
      </div>
      <!-- 아파트 인증코드 -->
      <div class="myPage-content-box apart-code">
        <label for="apart-code">아파트 인증코드</label>
        <input type="text" id="apart-code" disabled />
      </div>
      <!-- 주소 -->
      <div class="myPage-content-box address">
        <label for="address">주소</label>
        <input type="text" id="address" disabled />
      </div>
      <!-- 호수 -->
      <div class="myPage-content-box unit">
        <label for="unit">호수</label>
        <input type="text" id="unit" disabled />호
      </div>
      <button>회원정보 수정하기</button>
      <button>회원탈퇴하기</button>
    </div>
  </div>
</template>

<script>
import apiClient from '@/types/apiClient'
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'parkMypage'
  // setup() {
  //   function getMemberData() {

  //   }
  // }
  // 토큰을 백으로 보내서 해당 회원정보를 받아온 후 화면에 띄워준다.
})
</script>
<style>
.myPage-content-box {
  display: flex;
  flex-direction: column;
}

.myPage-content-box > label {
  display: flex;
  flex-direction: start;
}
</style>
