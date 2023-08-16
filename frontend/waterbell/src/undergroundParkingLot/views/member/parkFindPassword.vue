<template>
  <div class="password-check">
    <div class="password-check-title">
      <h1>비밀번호 찾기</h1>
    </div>
    <div class="password-check-content">
      <div class="password-check-content-box password">
        <label for="currentPW">비밀번호</label>
        <input type="password" id="currentPW" v-model="currentPW" />
      </div>
      <div class="password-check-bt">
        <button id="check" @click="isEqual">확인</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'parkFindPassword'
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

.password-check-bt {
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
