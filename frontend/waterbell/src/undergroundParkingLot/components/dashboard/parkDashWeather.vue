<template>
  <div class="container">
    <p>날씨 컴포넌트</p>
  </div>
</template>
<script lang="ts">
import { ref, onMounted, computed, defineComponent } from 'vue'
import store from '@/store/index'
import http from '@/types/http'

export default defineComponent({
  name: 'parkDashWeather',
  setup() {
    // 시설 아이디 가져오기
    // const facility_id = computed(() => store.getters['auth/facilityId']).value

    // 현재 날짜 및 시간 가져오기
    const now = new Date()
    const year = now.getFullYear().toString()
    const month = (now.getMonth() + 1).toString().padStart(2, '0') // JavaScript의 getMonth()는 0(1월)에서 11(12월)까지의 값을 반환합니다.
    const day = now.getDate().toString().padStart(2, '0')
    const hour = now.getHours().toString().padStart(2, '0') // 24시간 형식
    const minute = now.getMinutes().toString().padStart(2, '0')
    const lon = store.state.location['lon']
    const lat = store.state.location['lat']

    // 날씨 정보 데이터
    const status_sky = ref(null) // 하늘 상태
    const type_rainfall = ref(null) // 기상 상태

    async function getWeatherData() {
      try {
        const response = await http.get(`/dash/weather`, {
          params: {
            year: year,
            month: month,
            day: day,
            hour: hour,
            minute: minute,
            lon: lon, //여기 변경됨
            lat: lat //여기 변경됨
          }
        })

        status_sky.value = response.data.SKY.fcstValue
        type_rainfall.value = response.data.PTY.fcstValue
        console.log(status_sky.value)
        console.log(type_rainfall.value)
      } catch (error) {
        console.log('기상상태 데이터 가져오기 실패:', error)
      }
    }
    onMounted(async () => {
      await getWeatherData()
    })

    return { status_sky, type_rainfall, getWeatherData }
  }
})
</script>
<style></style>
