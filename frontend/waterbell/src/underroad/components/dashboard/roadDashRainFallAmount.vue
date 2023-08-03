<template>
  <div>
    <div class="container" id="dash-cctv">
      <p>지하차도 대시보드 강수량 그래프</p>
      <canvas
        ref="chartCanvas"
        id="chartCanvas"
        width="400"
        height="200"
      ></canvas>
    </div>
  </div>
</template>
<script lang="ts">
import { ref, onMounted, nextTick } from 'vue'
import { defineComponent } from 'vue'
import { useStore } from 'vuex'
import axios from 'axios'
import Chart from 'chart.js/auto'

export default defineComponent({
  name: 'roadDashRainAmountVue',
  setup() {
    const chartRef = ref(null)
    const store = useStore()
    const timeArr = ref<string[]>([])
    const amountArr = ref<string[]>([])

    const makeData = (i: Record<string, any>) => {
      // any 대신에 좀 더 구체적인 타입을 사용하려면 Record<string, any>를 사용하세요.
      for (const key in i) {
        timeArr.value.push(key)
        amountArr.value.push(i[key])
      }
      // console.log(timeArr.value)
      // console.log(amountArr.value)
    }

    async function getData() {
      try {
        // 현재 날짜 및 시간 가져오기
        const now = new Date()
        const year = now.getFullYear().toString()
        const month = (now.getMonth() + 1).toString().padStart(2, '0') // JavaScript의 getMonth()는 0(1월)에서 11(12월)까지의 값을 반환합니다.
        const day = now.getDate().toString().padStart(2, '0')
        const hour = now.getHours().toString().padStart(2, '0') // 24시간 형식
        const minute = now.getMinutes().toString().padStart(2, '0')
        const lon = store.state.location['lon']
        const lat = store.state.location['lat']
        // API 데이터 가져오기 (예시를 위해 랜덤 데이터 사용)
        const response = await axios.get(
          'http://localhost:8080/dash/map/rain',
          {
            params: {
              year: year,
              month: month,
              day: day,
              hour: hour,
              minute: minute,
              lon: lon, //여기 변경됨
              lat: lat //여기 변경됨
            }
          }
        )
        const apiData = response.data
        console.log('apiData')
        console.log(apiData)
        return { apiData }
        // 차트 생성을 위한 데이터 가공
        //apiData를 인자로 넘겨줍니다
      } catch (error) {
        console.error('API 데이터 가져오기 실패:', error)
      }
    }

    async function drawChart(chartCanvas: HTMLElement | null) {
      // const labels = apiData.map((data) => data.label)
      // const values = apiData.map((data) => data.value)
      // document.addEventListener('DOMContentLoaded', function () {
      console.log('차트 그리기 시작')
      console.log('drawChart에서 timeArr.value')
      console.log(timeArr.value)
      const canvas = document.getElementById('chartCanvas') as HTMLCanvasElement
      const ctx = canvas.getContext('2d')
      // 차트 그리기
      new Chart(ctx, {
        type: 'bar', // 차트 타입 (bar, line 등)

        data: {
          labels: timeArr.value,
          datasets: [
            {
              label: '강수량 데이터',
              data: amountArr.value,
              backgroundColor: 'rgba(75, 192, 192, 0.2)',
              borderColor: 'rgba(75, 192, 192, 1)',
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
        makeData(apiData)
      }
      await nextTick()
      // 차트 그리기
      // console.log('chartRef.value')
      // console.log(chartRef.value)
      // drawChart(chartRef.value)
      // console.log('chartRef.value')
      // console.log(chartRef.value)
    })

    // return { chartRef, timeArr, amountArr }
  }
})
</script>
<style lang="css">
#dash-cctv {
  height: 500px;
}
</style>
