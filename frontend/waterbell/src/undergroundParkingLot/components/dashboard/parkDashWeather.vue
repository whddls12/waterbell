<template>
  <div class="container">
    <div class="weather-dash-box">
      <!-- 날씨 -->
      <div class="dash-box">
        <div class="dash-box-title">
          <i class="fas fa-cloud fa-lg dash-box-icon"></i>
          <h4>날씨</h4>
        </div>
        <div class="dash-box-content">
          <img
            v-if="SKY && PTY"
            :src="getWeatherImageUrl()"
            alt="날씨 이미지"
            width="80"
            height="80"
          />
          <p v-else>관측되지 않는 지역입니다.</p>
        </div>
      </div>
      <!-- 기온 -->
      <div class="dash-box">
        <div class="dash-box-title">
          <i class="fas fa-thermometer-three-quarters fa-lg"></i>
          <h4>기온</h4>
        </div>
        <div class="dash-box-content">{{ current_temp }} ℃</div>
      </div>
      <!-- 습도 -->
      <div class="dash-box">
        <div class="dash-box-title">
          <i class="fas fa-tint fa-lg"></i>
          <h4>습도</h4>
        </div>
        <div class="dash-box-content">{{ current_humid }} ％</div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
declare global {
  interface Window {
    kakao: any
  }
}
import { ref, onMounted, computed, defineComponent } from 'vue'
import store from '@/store/index'
import axios from '@/types/apiClient'

export default defineComponent({
  name: 'parkDashWeather',
  setup() {
    const apiClient = axios.apiClient(store)
    const api = axios.api

    // 시설 아이디 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value

    // 현재 날짜 및 시간 가져오기
    const now = new Date()
    const year = now.getFullYear().toString()
    const month = (now.getMonth() + 1).toString().padStart(2, '0') // JavaScript의 getMonth()는 0(1월)에서 11(12월)까지의 값을 반환합니다.
    const day = now.getDate().toString().padStart(2, '0')
    const hour = now.getHours().toString().padStart(2, '0') // 24시간 형식
    const minute = now.getMinutes().toString().padStart(2, '0')
    // const address = ref('')
    let lon = store.state.location['lon']
    let lat = store.state.location['lat']
    // console.log(now)

    // 날씨 정보 데이터
    const status_sky = ref(null) // 하늘 상태
    const type_rainfall = ref(null) // 기상 상태

    // 이미지 폴더 경로
    const SKY = ref<string | null>(null) // 하늘 상태
    const PTY = ref<string | null>(null) // 기상 상태

    //카카오맵 script 추가하기 (geocorder 쓰기 위해)
    //지도 script 추가하기1
    const loadScript = async () => {
      return new Promise((resolve, reject) => {
        const script = document.createElement('script')
        script.type = 'text/javascript'
        script.src =
          '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=374cff0d8903c1ed2bf1e9533bb0feab&libraries=clusterer,drawing,services'
        script.addEventListener('load', async () => {
          await window.kakao.maps.load(getWeatherData)
        })
        script.onload = resolve // 스크립트 로드 완료시 resolve
        script.onerror = reject // 에러 발생시 reject

        document.head.appendChild(script)
      })
    }

    //현재 facilityId의 아파트 주소 받아오기
    // const getAddress = (): Promise<string> => {
    //   return new Promise((resolve, reject) => {
    //     apiClient
    //       .get(`/facilities/${facility_id}/apart`)
    //       .then((res) => {
    //         if (!res.data.apart || !res.data.apart.address) {
    //           reject('올바르지 않은 주소 데이터입니다.')
    //           return
    //         }
    //         let result = res.data.apart.address
    //         // console.log(result)
    //         resolve(result)
    //       })
    //       .catch((error) => {
    //         reject(error)
    //       })
    //   })
    // }

    async function getAddress(): Promise<any> {
      try {
        const res = await apiClient.get(`/facilities/${facility_id}/apart`)
        if (!res.data.apart || !res.data.apart.address) {
          throw new Error('올바르지 않은 주소 데이터입니다.')
        }
        return res.data.apart.address
      } catch (error) {
        console.log(error)
      }
    }

    async function getCoordinate() {
      try {
        const address = await getAddress() // 1. getAddress 기능
        const geocoder = new window.kakao.maps.services.Geocoder()

        return new Promise<void>((resolve, reject) => {
          geocoder.addressSearch(address, (result: any, status: any) => {
            if (status === window.kakao.maps.services.Status.OK) {
              lon = result[0].x
              lat = result[0].y
              console.log('주소 검색: ', lon, lat)
              resolve()
            } else {
              reject('주소 검색에 실패했습니다.')
            }
          })
        })
      } catch (error) {
        console.error(error)
      }
    }
    async function getWeatherData() {
      try {
        const response = await api.get(`/dash/weather`, {
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
      } else {
        SKY.value = 'sunny'
      }

      // 이미지 지정
      if (type_rainfall.value === '0') {
        PTY.value = 'none'
      } else if (type_rainfall.value === '1' || type_rainfall.value === '5') {
        PTY.value = 'rain'
      } else if (type_rainfall.value === '2' || type_rainfall.value === '6') {
        PTY.value = 'rainsnow'
      } else if (type_rainfall.value === '3' || type_rainfall.value === '7') {
        PTY.value = 'snow'
      } else {
        PTY.value = 'none'
      }
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
        const response = await api.get(
          `/dash/facilities/${facility_id}/sensors`
        )

        current_temp.value = response.data.Temperature
        current_humid.value = response.data.Humidity
      } catch (error) {
        console.log('미세먼지 측정 데이터 가져오기 실패:', error)
      }
    }
    onMounted(async () => {
      // await getCoordinate() // 주소를 통한 좌표 검색 완료까지 기다림
      // await loadScript() // 스크립트 로딩 완료까지 기다림
      // await getWeatherData() // 날씨 데이터 가져오기 완료까지 기다림
      // await getTempAndHumidData() // 온도 및 습도 데이터 가져오기 완료까지 기다림
      // makeWeatherImage() // 날씨 이미지 생성
      // // makeWeatherImage()
      await loadScript() // 먼저 Kakao Map 스크립트를 로드합니다.
      await getCoordinate() // 2. address 값을 addressSearch 함수로 좌표값으로 변환
      await getWeatherData() // 3. 좌표값을 가지고 getWeatherData 실행
      makeWeatherImage() // 4. 결과를 바탕으로 makeWeatherImage 실행
      await getTempAndHumidData()
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
<style scoped>
/* 날씨 기온 습도를 합치기 위함 */
.weather-dash-box {
  display: flex;
  gap: 20px;
}

.weather-dash-box > div {
  width: 155px;
}

.dash-box-content {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  font-size: 30px;
}
</style>
