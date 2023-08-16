<template>
  <div class="find-id">
    <div class="find-id-title">
      <h1>아이디 찾기</h1>
    </div>
    <div class="find-id-content">
      <div class="find-id-content-box">
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" />
      </div>
      <div class="find-id-content-box">
        <label for="phoneNum">휴대폰번호</label>
        <input type="text" id="phoneNum" v-model="phoneNum" />
      </div>
      <div class="find-id-bt">
        <button id="check" @click="findId">확인</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ref, defineComponent } from 'vue'
import axios from '@/types/apiClient'
import store from '@/store/index'

export default defineComponent({
  name: 'parkFindId',
  setup() {
    // api 요청을 위한 변수
    const apiClient = axios.apiClient(store)
    const api = axios.api

    // 입력한 변수들
    const name = ref(null)
    const phoneNum = ref(null)

    function findId() {
      console.log(name.value, phoneNum.value)
      api
        .post(`/member/apart/searchId`, {
          name: name.value,
          phone: phoneNum.value
        })
        .then((res) => {
          console.log(res)
        })
        .catch((err) => console.log(err))
    }

    return { name, phoneNum, findId }
  }
})
</script>

<style scoped>
.find-id {
  display: flex;
  width: inherit;
  flex-direction: column;
  align-items: center;
}

.find-id-content-box {
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

.find-id-bt {
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
