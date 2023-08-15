<template>
  <div class="main">
    <div class="input-field">
      <div class="subtitle">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="32"
          height="32"
          viewBox="0 0 32 32"
          fill="none"
        >
          <circle cx="16" cy="16" r="16" fill="#FF8901" />
          <text
            x="16"
            y="16"
            font-family="Arial"
            font-size="16"
            text-anchor="middle"
            dy=".3em"
            fill="white"
          >
            1
          </text>
        </svg>
        <label for="firstMessage">1차 경고 알림 메시지 설정</label>
      </div>
      <div class="input-box">
        <textarea
          id="firstMessage"
          type="text"
          v-model="custom.firstFloodMessage"
        ></textarea>
      </div>
    </div>
    <div class="input-field">
      <div class="subtitle">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="32"
          height="32"
          viewBox="0 0 32 32"
          fill="none"
        >
          <circle cx="16" cy="16" r="16" fill="#FF8901" />
          <text
            x="16"
            y="16"
            font-family="Arial"
            font-size="16"
            text-anchor="middle"
            dy=".3em"
            fill="white"
          >
            2
          </text>
        </svg>
        <label for="activation">2차 경고 알림 메시지 설정</label>
      </div>
      <div>
        <textarea
          id="activation"
          type="text"
          v-model="custom.activation_message"
        ></textarea>
      </div>
    </div>
    <div class="input-field">
      <div class="subtitle">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="32"
          height="32"
          viewBox="0 0 32 32"
          fill="none"
        >
          <circle cx="16" cy="16" r="16" fill="#FF8901" />
          <text
            x="16"
            y="16"
            font-family="Arial"
            font-size="16"
            text-anchor="middle"
            dy=".3em"
            fill="white"
          >
            3
          </text>
        </svg>
        <label for="deactivation">경고 해제시 알림 메시지 설정</label>
      </div>
      <div>
        <textarea
          id="deactivation"
          type="text"
          v-model="custom.deactivation_message"
        ></textarea>
      </div>
    </div>
    <div class="input-field2">
      <div class="subtitle">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          width="32"
          height="32"
          viewBox="0 0 32 32"
          fill="none"
        >
          <circle cx="16" cy="16" r="16" fill="#FF8901" />
          <text
            x="16"
            y="16"
            font-family="Arial"
            font-size="16"
            text-anchor="middle"
            dy=".3em"
            fill="white"
          >
            4
          </text>
        </svg>
        <label for="deactivation">기준치 설정</label>
      </div>
      <label for="threshold1">1차 </label>
      <div>
        <input id="threshold1" type="number" v-model="custom.firstAlarmValue" />
      </div>
    </div>
    <div class="input-field2">
      <label for="threshold2">2차 </label>
      <div>
        <input
          id="threshold2"
          type="number"
          v-model="custom.secondAlarmValue"
        />
      </div>
    </div>
    <div class="btn-container">
      <button id="updateBtn" @click="customMessage">수정</button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, computed, ref } from 'vue'
import store from '@/store/index'
import axios from '@/types/apiClient'

export default defineComponent({
  name: 'parkCustom',

  setup() {
    const apiClient = axios.apiClient(store)
    const facilityId = computed(() => store.getters['auth/facilityId']).value
    let custom = ref<{
      firstFloodMessage: string
      activation_message: string
      deactivation_message: string
      firstAlarmValue: number
      secondAlarmValue: number
    }>({
      firstFloodMessage: '',
      activation_message: '',
      deactivation_message: '',
      firstAlarmValue: 0,
      secondAlarmValue: 0
    })

    const setMessage = async () => {
      try {
        const res = await apiClient.get(
          `/management/manager/view/controlInfo/${facilityId}`
        )
        console.log(res.data)
        custom.value = res.data.facility
        console.log(custom.value)
      } catch (error) {
        console.error('Error fetching height data:', error)
      }
    }

    const customMessage = async () => {
      try {
        const res = await apiClient.post(
          `/management/manager/modify/controlInfo/${facilityId}`,
          custom.value
        )
        if (res.status === 202) {
          alert('수정 완료')
          window.location.reload()
        }
      } catch (error) {
        console.error('Error fetching height data:', error)
      }
    }

    onMounted(() => {
      setMessage()
    })
    return {
      custom,
      setMessage,
      customMessage
    }
  }
})
</script>

<style scoped>
.main {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-self: center;
}
.subtitle {
  display: flex;
  align-items: center;
  gap: 2px;
}
.input-field {
  margin-top: 30px;
  margin-bottom: 20px;
  width: 100%;
}

.input-field2 {
  align-self: flex-start;
  margin-top: 50px;
  margin-bottom: 20px;
  width: 100%; /* 가로 크기 변경 */
  height: 50px; /* 세로 크기를 다른 입력 필드의 반으로 변경 */
}
.input-field2 label {
  margin-bottom: 12px;
}

.label-flex {
  display: flex;
  justify-content: flex-start; /* 왼쪽으로 정렬 */
}

label {
  display: inline-block;
  width: 100%;
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
  border-radius: 8px;
  border: 1px solid var(--1, #cdd1de);
  display: flex;
  width: 70px;
  padding: 13px 0px 13px 16px;
  align-items: center;
  /* width: 100%;
  height: 50px;
  margin-top: 5px;
  text-align: center;
  border-radius: 15px;
  border: 1px solid #ccc;
  padding: 10px;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease; */
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

textarea {
  width: 100%;
  min-height: 80px;
  vertical-align: middle;
  height: auto; /* 자동 높이 설정 */
  margin-top: 5px;
  /* text-align: center; */
  border-radius: 15px;
  border: 1px solid #ccc;
  padding: 10px;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  resize: vertical; /* 사용자가 수직으로 크기를 조절할 수 있게 설정 */
}

textarea:focus {
  outline: none;
  box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.2);
}

#updateBtn {
  border-radius: 8px;
  background: var(--brand-color-1, #ff8901);
}
</style>
