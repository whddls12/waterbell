<template>
  <div class="container mt-4">
    <h5 class="my-4">알림 상세</h5>
    <div class="card">
      <div class="card-body">
        <div class="row mb-3">
          <label class="col-2 col-form-label">내용:</label>
          <div class="col-10">
            <p class="form-control-plaintext">{{ alarm.content }}</p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label">알림 종류:</label>
          <div class="col-10">
            <p class="form-control-plaintext">{{ alarm.alarmType }}</p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label">발신자:</label>
          <div class="col-10">
            <p class="form-control-plaintext">{{ alarm.sender }}</p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label">등록일시:</label>
          <div class="col-10">
            <p class="form-control-plaintext">{{ alarm.regDate }}</p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label">처리상태:</label>
          <div class="col-10">
            <p class="form-control-plaintext">{{ alarm.status }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref } from 'vue'
import http from '@/types/http'
import { useRoute } from 'vue-router'

export default defineComponent({
  name: 'AlarmDetail',

  setup() {
    const alarm = ref({
      alarm_id: '',
      content: '',
      alarmType: '',
      sender: '',
      regDate: '',
      status: ''
    })

    const route = useRoute()
    const alarm_id = route.params.alarm_id

    onMounted(() => {
      const token =
        'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3Iiwicm9sZSI6WyJQVUJMSUNfTUFOQUdFUiJdLCJpYXQiOjE2OTEwMzg1MzUsImV4cCI6MTY5MTA0MjEzNX0.MvqxLLuOf4wIlmDThGqbHUwcdFEMEBeAyBABiMxesmY' // 실제 사용할 토큰 값으로 변경

      http
        .get(`alarm/APART_MEMBER/detail/${alarm_id}`, {
          headers: {
            Authorization: token
          }
        })
        .then((res) => {
          alarm.value = res.data
        })
    })

    return {
      alarm
    }
  }
})
</script>

<style>
.container {
  width: 90%;
  margin: 0 auto;
}

.card {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 20px;
}

.label {
  font-weight: bold;
}

.form-control-plaintext {
  border: none;
  background-color: transparent;
  margin-left: 10px;
  margin-bottom: 0;
  padding: 0;
  font-size: 14px;
  line-height: 1.5;
}

/* 문단 간 테두리 스타일 */
.card-body > .row:not(:last-child) {
  border-bottom: 1px solid #e2e8f0;
}
</style>
