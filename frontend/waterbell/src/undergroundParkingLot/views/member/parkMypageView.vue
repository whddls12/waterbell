<template>
  <!-- 회원정보 조회 창 -->
  <!-- 수정하기 버튼을 누르기 전에는 입력도 수정도 되지 않도록 readonly 설정 사용 -->
  <div class="myPage">
    <div class="myPage-title">
      <h1>회원정보</h1>
    </div>
    <div class="myPage-content">
      <!-- 이름 -->
      <div class="myPage-content-box name">
        <label for="name">이름</label>
        <input type="text" id="name" disabled :placeholder="memberInfo?.name" />
      </div>
      <!-- 아이디 -->
      <div class="myPage-content-box loginId">
        <label for="id">아이디</label>
        <input
          type="text"
          id="loginId"
          disabled
          :placeholder="memberInfo?.loginId"
        />
      </div>
      <!-- 휴대폰번호 -->
      <div class="myPage-content-box phone">
        <label for="phone">휴대폰 번호</label>
        <input
          type="text"
          id="phone"
          disabled
          :placeholder="memberInfo?.phone"
        />
      </div>
      <!-- 아파트 인증코드 -->
      <div class="myPage-content-box apartCode">
        <label for="apart-code">아파트 인증코드</label>
        <input
          type="text"
          id="apartCode"
          disabled
          :placeholder="memberInfo?.apartCode"
        />
      </div>
      <!-- 주소 -->
      <div class="myPage-content-box address">
        <label for="address">주소</label>
        <input
          type="text"
          id="address"
          disabled
          :placeholder="memberInfo?.address"
        />
      </div>
      <!-- 호수 -->
      <div class="myPage-content-box addressNumber">
        <label for="addressNumber">호수</label>
        <div class="addressNumber-box">
          <input
            type="text"
            id="addressNumber"
            disabled
            :placeholder="memberInfo?.addressNumber"
          />
          <p>호</p>
        </div>
      </div>
      <!-- 버튼 -->
      <div class="myPage-btn">
        <button id="update" @click="passwordCheck">회원정보 수정하기</button>
        <div class="withdrawal" @click="goWithdrawal">
          <i class="fas fa-arrow-right"> 회원탈퇴하기</i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, defineComponent } from 'vue'
import router from '@/router'
import axios from '@/types/apiClient'
import store from '@/store/index'
import { logout } from '@/types/authFunctionModule'

export default defineComponent({
  name: 'parkMypage',
  setup() {
    const apiClient = axios.apiClient(store)
    // const api = axios.api
    const memberInfo = ref(null)

    function getMemberData() {
      apiClient
        .get(`/member/mypage`)
        .then((res) => {
          memberInfo.value = res.data.memberInfo
          console.log(memberInfo.value)
        })
        .catch((error) => console.log(error))
    }
    function passwordCheck() {
      router.push({ path: '/park/mypage/passwordCheck' })
    }

    async function goWithdrawal() {
      apiClient
        .get(`/member/apartMember/withdrawal`)
        .then((res) => {
          console.log(res)
          // 회원탈퇴 후 로그아웃 / 메인화면으로 이동 처리
          alert('회원탈퇴가 완료되었습니다.')
        })
        .catch((err) => console.log(err))
    }

    onMounted(() => {
      getMemberData()
    })
    return { memberInfo, getMemberData, passwordCheck, goWithdrawal }
  }
})
</script>
<style scoped>
.myPage {
  display: flex;
  width: inherit;
  flex-direction: column;
  align-items: center;
}

.myPage-content-box {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 20px 0px;
  font-size: 1.5rem;
  gap: 5px;
}

input {
  border-radius: 8px;

  width: 660px;
  height: 60px;
  font-size: 1.5rem;
  padding-left: 10px;
}

#addressNumber {
  width: 214px;
}

.addressNumber-box {
  display: flex;
  align-items: center;
  gap: 10px;
}

.myPage-btn {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

#update {
  display: flex;
  width: 644px;
  height: 60px;
  padding: 11px 16px;
  gap: 8px;
  justify-content: center;
  align-items: center;
  align-self: center;

  border: none;
  border-radius: 40px;
  color: white;
  background-color: #114cb1;
  font-weight: 600;
  font-size: 1.5rem;
  letter-spacing: 7px;
}

#update:hover {
  cursor: pointer;
  background-color: #ff8901;
}

.withdrawal {
  color: #666666;
  align-self: end;
}

.withdrawal:hover {
  cursor: pointer;
  color: black;
}
</style>
