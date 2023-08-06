<template>
  <div class="main">
    <div class="input-field">
      <label for="firstMessage">1차 경고 메시지 설정</label>
      <div class="input-box">
        <input id="firstMessage" type="text" v-model="firstMessage" />
      </div>
    </div>
    <div class="input-field">
      <label for="activation">2차 경고 메시지 설정</label>
      <div>
        <input id="activation" type="text" v-model="activation" />
      </div>
    </div>
    <div class="input-field">
      <label for="deactivation">경고 해제 메시지 설정</label>
      <div>
        <input id="deactivation" type="text" v-model="deactivation" />
      </div>
    </div>
    <div class="input-field2">
      <label for="threshold1">1차 기준치 설정</label>
      <div>
        <input id="threshold1" type="number" v-model="threshold1" />
      </div>
    </div>
    <div class="input-field2">
      <label for="threshold2">2차 기준치 설정</label>
      <div>
        <input id="threshold2" type="number" v-model="threshold2" />
      </div>
    </div>
    <div class="btn-container">
      <button @click="setMessage">수정</button>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      firstMessage: '',
      activation: '',
      deactivation: '',
      threshold1: 0,
      threshold2: 0
    }
  },
  methods: {
    setMessage() {
      // 메시지 설정하는 로직이 들어갑니다.
      console.log(`1차 메시지: ${this.firstMessage}`)
      console.log(`2차 메시지: ${this.activation}`)
      console.log(`2차 메시지: ${this.deactivation}`)
      console.log(`기준치: ${this.threshold1}`)
      console.log(`기준치: ${this.threshold2}`)
    }
  }
}
</script>

<style scoped>
.main {
  width: 100%;
}

.input-field {
  margin-top: 5px;
  margin-bottom: 20px;
  width: 100%;
}

.input-field2 {
  margin-top: 50px;
  margin-bottom: 20px;
  width: 10%; /* 가로 크기 변경 */
  height: 50px; /* 세로 크기를 다른 입력 필드의 반으로 변경 */
}

.label-flex {
  display: flex;
  justify-content: flex-start; /* 왼쪽으로 정렬 */
}

label {
  display: inline-block;
  width: 200px;
  text-align: left;
}

button {
  display: inline-block;
  margin-top: 10px;
  padding: 10px 20px;
  border: none;
  background-color: #3498db;
  color: white;
  cursor: pointer;
}

input {
  width: 100%;
  height: 50px;
  margin-top: 5px;
  text-align: center;
  border-radius: 15px;
  border: 1px solid #ccc;
  padding: 10px;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
}

input:focus {
  outline: none;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.2);
}

.btn-container {
  display: flex;
  justify-content: center; /* 가운데 정렬 */
  margin-top: 50px; /* 버튼과 다른 입력 필드 간의 간격을 주기 위해 추가 */
}
</style>
