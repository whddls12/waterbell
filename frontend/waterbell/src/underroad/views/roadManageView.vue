<template>
  <div class="main">
    <div class="input-field">
      <label for="firstMessage">1차 경고 LED 메시지 설정</label>
      <div class="input-box">
        <input
          id="firstMessage"
          type="text"
          v-model="custom.firstFloodMessage"
        />
      </div>
    </div>
    <div class="input-field">
      <label for="activation">2차 경고 LED 메시지 설정</label>
      <div>
        <input
          id="activation"
          type="text"
          v-model="custom.activation_message"
        />
      </div>
    </div>
    <div class="input-field">
      <label for="deactivation">경고 해제시 LED 메시지 설정</label>
      <div>
        <input
          id="deactivation"
          type="text"
          v-model="custom.deactivation_message"
        />
      </div>
    </div>
    <div class="input-field2">
      <label for="threshold1">1차 경고 기준치 설정</label>
      <div>
        <input id="threshold1" type="number" v-model="custom.firstAlarmValue" />
      </div>
    </div>
    <div class="input-field2">
      <label for="threshold2">2차 경고 기준치 설정</label>
      <div>
        <input
          id="threshold2"
          type="number"
          v-model="custom.secondAlarmValue"
        />
      </div>
    </div>
    <div class="btn-container">
      <button @click="customMessage">수정</button>
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
  width: 250px;
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

.main .input-field {
  margin-left: 50px;
}

.main .input-field2 {
  margin-left: 50px;
}

.main .input-field label {
  margin-left: 50px;
}
</style>
