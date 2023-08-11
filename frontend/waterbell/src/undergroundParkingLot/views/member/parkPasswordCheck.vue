<template>
  <!-- 회원정보 수정하기 전 -->
  <!-- 비밀번호 확인 창 -->
  <div class="password-check">
    <div class="password-check-title">
      <h1>회원 정보 수정</h1>
    </div>
    <div class="password-check-content">
      <!-- 비밀번호 -->
      <div class="password-check-content-box password">
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="currentPassword" />
      </div>
      <!-- 버튼 -->
      <div class="password-check-btn">
        <button id="check" @click="isEqual">확인</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, defineComponent } from 'vue'
import router from '@/router'
import axios from '@/types/apiClient'
import store from '@/store/index'

export default defineComponent({
  name: 'parkMypage',
  setup() {
    const apiClient = axios.apiClient(store)
    // const api = axios.api
    const currentPassword = ref('')

    function isEqual() {
      console.log(currentPassword.value)
      apiClient
        .post(`/member/verificationPW`, {
          params: {
            password: currentPassword.value
          }
        })
        .then((res) => {
          console.log(res)
        })
        .catch((err) => console.log(err))

      // router.push({ path: '/park/mypage/update' })
    }
    return { isEqual }
  }
})
</script>

<style scoped>
.password-check {
  display: flex;
  width: inherit;
  flex-direction: column;
  align-items: center;
}

.password-check-content-box {
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

.password-check-btn {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

#check {
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

#check:hover {
  cursor: pointer;
}
</style>
