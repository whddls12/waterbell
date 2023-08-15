<template>
  <div class="container" id="dash-cctv">
    <div class="dash-box">
      <div class="dash-box-title">
        <h3>강수량 그래프</h3>
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
import { ref, onMounted, nextTick } from 'vue'
import { defineComponent } from 'vue'
import axios from '@/types/apiClient'
import store from '@/store/index'

export default defineComponent({
  name: 'roadDashRainAmountVue',
  setup() {
    // const apiClient = axios.apiClient(store)
    const api = axios.api

    const chartRef = ref(null)
    const timeArr = ref<string[]>([])
    const amountArr = ref<string[]>([])
    // 현재 날짜 및 시간 가져오기
    const now = new Date()
    const year = now.getFullYear().toString()
    const month = (now.getMonth() + 1).toString().padStart(2, '0') // JavaScript의 getMonth()는 0(1월)에서 11(12월)까지의 값을 반환합니다.
    const day = now.getDate().toString().padStart(2, '0')
    const hour = now.getHours().toString().padStart(2, '0') // 24시간 형식
    const minute = now.getMinutes().toString().padStart(2, '0')
    const lon = store.state.location['lon']
    const lat = store.state.location['lat']

    const makeData = (i: Record<string, any>) => {
      for (const key in i) {
        timeArr.value.push(key)
        amountArr.value.push(i[key])
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
        // console.log('apiData')
        // console.log(apiData)
        return { apiData }
        // 차트 생성을 위한 데이터 가공
        //apiData를 인자로 넘겨줍니다
      } catch (error) {
        console.error('API 데이터 가져오기 실패:', error)
      }
    }

    async function drawChart(rainChartCanvas: HTMLElement | null) {
      // const labels = apiData.map((data) => data.label)
      // const values = apiData.map((data) => data.value)
      // document.addEventListener('DOMContentLoaded', function () {
      // -> onMounted에 의해 컴포넌트가 마운트 된 후에 실행된다. 중복되는 의미라서 주석처리

      // console.log('차트 그리기 시작')
      // console.log('drawChart에서 timeArr.value')
      // console.log(timeArr.value)
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
        options: {
          // 차트 옵션 설정 (생략 가능)
          scales: {
            y: {
              beginAtZero: true
            }
          }
        }
      })
      // })
    }

    onMounted(async () => {
      const apiData = await getData()
      if (apiData) {
        makeData(apiData.apiData)
      }
      await nextTick()
      // 차트 그리기
      // console.log('chartRef.value')
      // console.log(chartRef.value)
      drawChart(chartRef.value)
      // console.log('chartRef.value')
      // console.log(chartRef.value)
    })

    return { chartRef, timeArr, amountArr }
  }
})
</script>
<style>
#dash-cctv {
  height: 500px;
}
</style>
