<template>
  <div class="reset-password">
    <div class="reset-password-title">
      <h1>비밀번호 재설정</h1>
    </div>
    <div class="reset-password-content">
      <div class="reset-password-content-box">
        <label for="newPassword">새로운 비밀번호</label>
        <input type="password" id="newPassword" v-model="newPassword" />
      </div>
      <div class="reset-password-content-box">
        <label for="newPasswordConfirm">새로운 비밀번호 확인</label>
        <input
          type="password"
          id="newPasswordConfirm"
          v-model="newPasswordConfirm"
        />
      </div>
      <div class="reset-password-bt">
        <button id="check" @click="resetPassword">확인</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ref, defineComponent } from 'vue'
import axios from '@/types/apiClient'
import store from '@/store/index'

export default defineComponent({
  name: 'parkPasswordReset',
  setup() {
    const apiClient = axios.apiClient(store)

    // input 입력 값
    const newPassword = ref('')
    const newPasswordConfirm = ref('')

    // function resetPassword() {
    //   if (
    //     newPassword.value != '' &&
    //     newPassword.value === newPasswordConfirm.value
    //   ) {
    //     console.log('새 비밀번호 확인값 일치')
    //     apiClient.post(`/member/reset/password`, {
    //     })
    //   }
    // }

    return {}
  }
})
</script>
<style>
.reset-password {
  display: flex;
  width: inherit;
  flex-direction: column;
  align-items: center;
}

.reset-password-content-box {
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

.reset-password-bt {
  flex-direction: column;
  gap: 40px;
}

#check {
  display: flex;
  width: 660px;
  height: 60px;
  justify-content: center;
  align-items: center;

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
