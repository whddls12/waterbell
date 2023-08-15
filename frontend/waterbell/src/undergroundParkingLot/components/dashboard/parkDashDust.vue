<template>
  <div class="container">
    <div class="dash-box">
      <div class="dash-box-title">
        <h4>미세먼지</h4>
        <p>{{ current_dust }}</p>
      </div>
      <div class="dash-box-content">
        <canvas
          ref="dustChartCanvas"
          id="dustChartCanvas"
          width="400"
          height="180"
        ></canvas>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import Chart from 'chart.js/auto'
import { ref, onMounted, computed, defineComponent, nextTick } from 'vue'
import axios from '@/types/apiClient'
import store from '@/store/index'

export default defineComponent({
  name: 'parkDashDust',
  setup() {
    // const apiClient = axios.apiClient(store)
    const api = axios.api
    // 시설 아이디 가져오기
    const facility_id = computed(() => store.getters['auth/facilityId']).value

    const chartRef = ref(null)
    const current_dust = ref<number | null>() // 미세먼지 측정 값
    const left_dust = ref(250) // (미세먼지 측정 최대치) - (현재 측정값)

    async function getDustData() {
      try {
        const response = await api.get(
          `/dash/facilities/${facility_id}/sensors`
        ) // 10 -> 시설 아이디로 교체해야함.
        console.log(response.data)
        current_dust.value = response.data.Dust
        if (current_dust.value) {
          if (current_dust.value >= 250) {
            left_dust.value = 0
          } else {
            left_dust.value = 250 - Number(current_dust.value)
          }
        }

        return { current_dust }
      } catch (error) {
        console.log('미세먼지 측정 데이터 가져오기 실패:', error)
      }
    }

    async function drawChart(dustChartCanvas: HTMLElement | null) {
      const canvas = document.getElementById(
        'dustChartCanvas'
      ) as HTMLCanvasElement
      const ctx = canvas.getContext('2d')

      if (!ctx) {
        return
      }

      // 차트 그리기
      await new Chart(ctx, {
        type: 'doughnut', // 차트 타입
        data: {
          datasets: [
            {
              label: '미세먼지 그래프',
              data: [current_dust.value, left_dust.value],
              // backgroundColor: ['rgb(255, 72, 72)', 'rgb(147, 147, 147)'], // 그래프 색상
              backgroundColor: function (context: any) {
                const index = context.dataIndex
                const value = context.dataset.data[index]
                if (!index) {
                  if (value >= 250) {
                    return 'rgb(255, 72, 72, 0.8)'
                  } else if (value >= 100) {
                    return 'rgba(251, 233, 24, 0.8)'
                  } else if (value >= 50) {
                    return 'rgb(27, 218, 110, 0.8)'
                  } else {
                    return 'rgb(16, 96, 254, 0.8)'
                  }
                } else {
                  return 'rgba(147, 147, 147, 0.2)'
                }
              },
              borderWidth: 0,
              borderRadius: 8,
              hoverBackgroundColor: function (context: any) {
                const index = context.dataIndex
                const value = context.dataset.data[index]
                if (!index) {
                  if (value >= 250) {
                    return 'rgb(255, 72, 72)'
                  } else if (value >= 100) {
                    return 'rgb(251, 233, 24)'
                  } else if (value >= 50) {
                    return 'rgb(27, 218, 110)'
                  } else {
                    return 'rgb(16, 96, 254)'
                  }
                } else {
                  return 'rgba(147, 147, 147, 0.2)'
                }
              },
              hoverOffset: 4
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          // plugin이 있어야 적용될거같음.
          elements: {
            center: {
              text: current_dust,
              fontSize: '50',
              fontStyle: 'Helvetica'
            }
          },
          cutoutPercentage: 70, // 파이 차트의 가운데 부분을 얼마나 자를 건지
          rotation: -90, // 호를 그릴 시작 각도
          circumference: 180, // 호를 그리는 각도
          // 그래프 가운데에 미세먼지 측정값을 띄우는 플러그인 구현해보기
          // plugins: {
          //   beforeDraw: function (context: any) {
          //     console.log('차트 그리기 전!')
          //     console.log(context)
          //     let centerConfig = context.options.elements.center
          //     let fontSize = centerConfig.fontSize || '50'
          //     let fontStyle = centerConfig.fontStyle || 'Arial'
          //     let txt = centerConfig.text
          //   }
          // },
          layout: {
            padding: 30
          }
        }
      })
    }

    onMounted(async () => {
      await getDustData()
      await nextTick()
      drawChart(chartRef.value)
    })

    return { chartRef, getDustData }
  }
})
</script>
<style></style>
