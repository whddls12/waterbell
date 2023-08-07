<template lang="">
  <div class="main">
    <div class="controll1">전광판 제어</div>
    <div class="warning1">1차 경고 작동 중</div>
    <div class="buttons">
      <button class="button1" @click="onAction">동작</button>
      <button class="button2" @click="onRelease">해제</button>
    </div>
  </div>
</template>
<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'roadControlLedVue',
  methods: {
    onAction() {
      // 동작 버튼을 눌렀을 때 실행할 코드
      console.log('동작 버튼 클릭')
    },
    onRelease() {
      // 해제 버튼을 눌렀을 때 실행할 코드
      console.log('해제 버튼 클릭')
    }
  }
})
</script>

<style scoped>
.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 600px;
  height: 249px;
  background: #f2f7ff;
  box-shadow: 0px 4px 30px rgba(0, 0, 0, 0.17);
  border-radius: 12px;
}

.controll1 {
  width: 368px;
  height: 59px;
  text-align: center;
  color: black;
  font-size: 20px;
  font-family: Roboto;
  font-weight: 500;
  line-height: 28px;
  letter-spacing: 0.25px;
  word-wrap: break-word;
}

.warning1 {
  width: 300px;
  height: 80px;
  text-align: center;
  color: black;
  font-size: 34px;
  font-family: Roboto;
  font-weight: 600;
  line-height: 48px;
  letter-spacing: 0.25px;
  word-wrap: break-word;
}

.buttons {
  display: flex;
  justify-content: center;
  width: 100%;
  padding: 0 20px; /* 버튼 간의 거리를 늘리기 위한 패딩 */
}

.button1 {
  display: flex;
  width: 120px;
  height: 40px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  border-radius: 10px;
  background: var(--unnamed, #f86262);
  margin-right: 10px;
  color: #fff;
}

.button2 {
  display: flex;
  width: 120px;
  height: 40px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  flex-shrink: 0;
  border-radius: 10px;
  background: var(--1, #10316b);
  margin-left: 10px;
  color: #fff;
}
</style>
