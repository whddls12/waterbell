<template>
  <div class="container">
    <div class="dash-box">
      <div class="dash-box-title">
        <i class="fas fa-chart-line"></i>
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
import { useStore } from 'vuex'
import http from '@/types/http'

export default defineComponent({
  name: 'parkDashRainAmountVue',
  setup() {
    const chartRef = ref(null)
    const store = useStore()
    const timeArr = ref<string[]>([]) // 시간 데이터
    const amountArr = ref<string[]>([]) // 강수량 데이터

    // 현재 날짜 및 시간 가져오기
    const now = new Date()
    const year = now.getFullYear().toString()
    const month = (now.getMonth() + 1).toString().padStart(2, '0') // JavaScript의 getMonth()는 0(1월)에서 11(12월)까지의 값을 반환합니다.
    const day = now.getDate().toString().padStart(2, '0')
    const hour = now.getHours().toString().padStart(2, '0') // 24시간 형식
    const minute = now.getMinutes().toString().padStart(2, '0')
    const lon = store.state.location['lon']
    const lat = store.state.location['lat']

    // 차트에 들어갈 데이터로 가공하는 함수
    const makeData = (i: Record<string, any>) => {
      for (const key in i) {
        timeArr.value.push(key)
        amountArr.value.push(i[key])
      }
    }

    // API 데이터 가져오기 (예시를 위해 랜덤 데이터 사용)
    async function getData() {
      try {
        const response = await http.get('http://localhost:8080/dash/map/rain', {
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
              fill: true
            }
          },
          scales: {
            y: {
              grid: {
                display: false
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
/* #dash-cctv {
  height: 500px;
} */
</style>
