<template>
  <div>
    <p>지하차도 대시보드 강수량 그래프</p>
    <canvas ref="chartCanvas" width="400" height="200"></canvas>
  </div>
</template>
<script>
import Chart from "chart.js/auto";
import { ref, onMounted } from "vue";
import { defineComponent } from "vue";
import axios from "axios";

export default defineComponent({
  name: "roadDashRainAmountVue",
  setup() {
    const chartRef = ref(null);

    onMounted(() => {
      // 차트 그리기
      drawChart(chartRef.value);
    });

    return { chartRef };
  },
});

async function drawChart(chartCanvas) {
  try {
    // 현재 날짜 및 시간 가져오기
    const now = new Date();
    const year = now.getFullYear().toString();
    const month = (now.getMonth() + 1).toString().padStart(2, "0"); // JavaScript의 getMonth()는 0(1월)에서 11(12월)까지의 값을 반환합니다.
    const day = now.getDate().toString().padStart(2, "0");
    const hour = now.getHours().toString().padStart(2, "0"); // 24시간 형식
    const minute = now.getMinutes().toString().padStart(2, "0");

    // API 데이터 가져오기 (예시를 위해 랜덤 데이터 사용)
    const response = await axios.get("http://localhost:8080/dash/map/weather", {
      params: {
        year: year,
        month: month,
        day: day,
        hour: hour,
        minute: minute,
      },
    });
    const apiData = response.data;
    console.log(apiData);

    // 차트 생성을 위한 데이터 가공
    const labels = apiData.map((data) => data.label);
    const values = apiData.map((data) => data.value);

    // 차트 그리기
    new Chart(chartCanvas, {
      type: "bar", // 차트 타입 (bar, line 등)
      data: {
        labels: labels,
        datasets: [
          {
            label: "강수량 데이터",
            data: values,
            backgroundColor: "rgba(75, 192, 192, 0.2)",
            borderColor: "rgba(75, 192, 192, 1)",
            borderWidth: 1,
          },
        ],
      },
      options: {
        // 차트 옵션 설정 (생략 가능)
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    });
  } catch (error) {
    console.error("API 데이터 가져오기 실패:", error);
  }
}
</script>
<style lang=""></style>
