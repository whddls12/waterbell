<template>
  <div class="container">
    <div class="dash-box">
      <div class="dash-box-title">
        <i class="fas fa-chart-line fa-lg"></i>
        <div><h4>강수량 그래프</h4></div>
      </div>
      <div class="dash-box-content">
        <canvas
          ref="rainChartCanvas"
          id="rainChartCanvas"
          width="400"
          height="200"
        ></canvas>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import Chart from 'chart.js/auto'
import { ref, onMounted, nextTick, computed } from 'vue'
import { defineComponent } from 'vue'
import axios from '@/types/apiClient'
import store from '@/store/index'

export default defineComponent({
  name: 'parkDashRainAmountVue',
  setup() {
    const apiClient = axios.apiClient(store)
    const api = axios.api

    // 시설 아이디 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value

    const chartRef = ref(null)
    const timeArr = ref<string[]>([]) // 시간 데이터
    const amountArr = ref<string[]>([]) // 강수량 데이터

    // 현재 날짜 및 시간 가져오기
    const now = new Date()
    const year = now.getFullYear().toString()
    const month = (now.getMonth() + 1).toString().padStart(2, '0') // JavaScript의 getMonth()는 0(1월)에서 11(12월)까지의 값을 반환합니다.
    const day = now.getDate().toString().padStart(2, '0')
    const hour = now.getHours().toString().padStart(2, '0') // 24시간 형식
    const minute = now.getMinutes().toString().padStart(2, '0')

    let lon = store.state.location['lon']
    let lat = store.state.location['lat']

    // 차트에 들어갈 데이터로 가공하는 함수
    const makeData = (i: Record<string, any>) => {
      for (const key in i) {
        timeArr.value.push(key)
        amountArr.value.push(i[key])
      }
    }

    //카카오맵 script 추가하기 (geocorder 쓰기 위해)
    //지도 script 추가하기1
    const loadScript = async () => {
      return new Promise((resolve, reject) => {
        const script = document.createElement('script')
        script.type = 'text/javascript'
        script.src =
          '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=374cff0d8903c1ed2bf1e9533bb0feab&libraries=clusterer,drawing,services'
        script.addEventListener('load', async () => {
          await window.kakao.maps.load(getData)
        })
        script.onload = resolve // 스크립트 로드 완료시 resolve
        script.onerror = reject // 에러 발생시 reject

        document.head.appendChild(script)
      })
    }

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

    // API 데이터 가져오기 (예시를 위해 랜덤 데이터 사용)
    async function getData() {
      try {
        const response = await api.get('/dash/map/rain', {
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
        const apiData = response.data
        console.log(apiData)
        return { apiData }
        // 차트 생성을 위한 데이터 가공
        // apiData를 인자로 넘겨줍니다
      } catch (error) {
        console.error('API 데이터 가져오기 실패:', error)
      }
    }
    // 차트를 화면에 그려주는 함수
    async function drawChart(rainChartCanvas: HTMLElement | null) {
      const canvas = document.getElementById(
        'rainChartCanvas'
      ) as HTMLCanvasElement
      const ctx = canvas.getContext('2d')

      // 차트 그리기
      new Chart(ctx, {
        type: 'line', // 차트 타입 (bar, line 등)

        data: {
          labels: timeArr.value,
          datasets: [
            {
              label: '강수량 데이터',
              data: amountArr.value,
              backgroundColor: 'rgba(151, 143, 237, 0.2)',
              borderColor: 'rgba(151, 143, 237, 1)',
              borderWidth: 1
            }
          ]
        },
        // 차트 옵션 설정 (생략 가능)
        options: {
          elements: {
            line: {
              fill: false
            }
          },
          scales: {
            y: {
              grid: {
                // 가로축 격자선
                display: true
              },
              title: {
                display: true,
                text: '시간당 강수량(mm)'
              },
              beginAtZero: true
            },
            x: {
              grid: {
                display: false
              },
              title: {
                display: true,
                text: '시각(시)'
              }
            }
          },
          plugins: {
            legend: {
              display: false // 범례 제거
            }
          }
        }
      })
    }

    onMounted(async () => {
      await loadScript() // 먼저 Kakao Map 스크립트를 로드합니다.
      await getCoordinate() // 2. address 값을 addressSearch 함수로 좌표값으로 변환
      const apiData = await getData()
      if (apiData) {
        makeData(apiData.apiData)
      }
      await nextTick()
      drawChart(chartRef.value)
    })

    return { chartRef, timeArr, amountArr }
  }
})
</script>
<style>
.fas {
  margin-bottom: 8px;
}
</style>
