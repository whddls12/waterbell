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
    <button @click="setMessage">수정</button>
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
  margin-left: 40px;
  margin-right: 40px;
}

.input-field {
  margin-bottom: 20px;
  width: 100%;
}

.input-field2 {
  margin-bottom: 20px;
  width: 5%;
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
}
</style>
