<template>
  <div class="container weather-dash-box">
    <!-- 날씨 -->
    <div class="dash-box">
      <div class="dash-box-title">
        <i class="fas fa-cloud dash-box-icon"></i>
        <h3>날씨</h3>
      </div>
      <img
        v-if="SKY && PTY"
        :src="getWeatherImageUrl()"
        alt="날씨 이미지"
        width="80"
        height="80"
      />
      <p v-else>관측되지 않는 지역입니다.</p>
    </div>
    <!-- 기온 -->
    <div class="dash-box">
      <p>기온: {{ current_temp }}</p>
    </div>
    <!-- 습도 -->
    <div class="dash-box">
      <p>습도: {{ current_humid }}</p>
    </div>
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
    const facility_id = computed(() => store.getters['auth/facilityId']).value

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

    // 이미지 폴더 경로
    const SKY = ref<string | null>(null) // 하늘 상태
    const PTY = ref<string | null>(null) // 기상 상태

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

    function makeWeatherImage() {
      // 폴더 지정
      if (status_sky.value === '1') {
        // 맑음
        SKY.value = 'sunny'
      } else if (status_sky.value === '3') {
        // 구름 많음
        SKY.value = 'cloudy'
      } else if (status_sky.value === '4') {
        // 흐림
        SKY.value = 'blur'
      }
      SKY.value = 'cloudy'
      // 이미지 지정
      if (type_rainfall.value === '0') {
        PTY.value = 'none'
      } else if (type_rainfall.value === '1' || type_rainfall.value === '5') {
        PTY.value = 'rain'
      } else if (type_rainfall.value === '2' || type_rainfall.value === '6') {
        PTY.value = 'rainsnow'
      } else if (type_rainfall.value === '3' || type_rainfall.value === '7') {
        PTY.value = 'snow'
      }
      PTY.value = 'rain'
    }

    function getWeatherImageUrl() {
      if (!SKY.value || !PTY.value) {
        return '' // SKY 또는 PTY 값이 없으면 빈 URL을 반환합니다.
      }

      // 예시 경로: assets/images/weather/blur/snow.png
      return require(`@/assets/images/weather/${SKY.value}/${PTY.value}.png`)
    }

    const current_temp = ref(null)
    const current_humid = ref(null)

    async function getTempAndHumidData() {
      try {
        const response = await http.get(`/dash/facilities/10/sensors`)

        current_temp.value = response.data.Temperature
        current_humid.value = response.data.Humidity
      } catch (error) {
        console.log('미세먼지 측정 데이터 가져오기 실패:', error)
      }
    }
    onMounted(async () => {
      await getTempAndHumidData()
      await getWeatherData()
      makeWeatherImage()
    })

    return {
      status_sky,
      type_rainfall,
      SKY,
      PTY,
      getWeatherData,
      getWeatherImageUrl,
      current_temp,
      current_humid,
      getTempAndHumidData
    }
  }
})
</script>
<style>
/* 날씨 기온 습도를 합치기 위함 */
.weather-dash-box {
  display: flex;
  flex-direction: row;
  gap: 20px;
}

.dash-box {
  display: flex;
  flex-direction: column;
  background-color: white;
}

.dash-box-title {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}
</style>
