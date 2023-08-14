<template>
  <div class="each-report">
    <!-- 게시판 이름과 목록관련버튼들 -->
    <div class="report-header">
      <div class="board-title">
        <h2>신고접수</h2>
      </div>
      <div class="list-btn">
        <button @click="goReportList">목록</button>
        <!-- <button>윗글</button>
        <button>아랫글</button> -->
      </div>
    </div>
    <!-- 신고접수 글 -->
    <div class="report-body">
      <div class="report-title-box">
        <div class="report-title">
          <p>{{ reportInfo?.title }}</p>
        </div>
        <div class="report-info">
          <div class="report-info info-box">
            <p>{{ reportInfo?.name }}</p>
            <p>{{ reportInfo?.createDate }}</p>
          </div>
          <div class="report-info info-box">
            <p>{{ reportInfo?.status }}</p>
            <p>{{ reportInfo?.viewCount }}</p>
          </div>
        </div>
      </div>
      <div class="report-content">
        <div class="content-text">
          {{ reportInfo?.content }}
        </div>
        <div class="content-image">
          <img
            class="report-image"
            v-for="(image, index) in imageList"
            :key="index"
            :src="image.url"
            :alt="image.imageName"
          />
        </div>
      </div>
    </div>
    <!-- 수정, 삭제 버튼 -->
    <div class="report-footer">
      <button @click="goToUpdate(reportInfo?.id)">수정</button>
      <button>삭제</button>
    </div>
  </div>
</template>
<script lang="ts">
import { ref, defineComponent, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from '@/types/apiClient'
import store from '@/store/index'
import router from '@/router'

export default defineComponent({
  name: 'roadReportItemVue',
  setup() {
    const route = useRoute()
    const report_id = route.params.report_id
    const reportInfo = ref(null)
    const imageList = ref(null)

    const apiClient = axios.apiClient(store)
    const api = axios.api

    function getReportData() {
      api
        .get(`/reports/undergroundRoad/detail/${report_id}`)
        .then((res) => {
          console.log(res.data)
          imageList.value = res.data.imageList
          reportInfo.value = res.data.board
        })
        .catch((err) => console.log(err))
    }

    function goReportList() {
      router.push({ path: '/road/report' })
    }

    function goToUpdate(report_id: any) {
      router.push({ path: `/road/report/update/${report_id}` })
    }

    onMounted(() => {
      getReportData()
    })

    return {
      report_id,
      reportInfo,
      imageList,
      getReportData,
      goReportList,
      goToUpdate
    }
  }
})
</script>
<style>
.each-report {
  width: 100%;
  padding: 20px;
}

.each-report > div {
  padding: 10px;
}

.list-btn {
  display: flex;
  justify-content: start;
}

.report-header {
  border-bottom: 2px solid black;
}

.report-body {
  border-bottom: 2px solid black;
}

.report-title-box {
  border-bottom: 1px solid black;
  text-align: start;
}

.report-info {
  display: flex;
  justify-content: space-between;
}

.report-content {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.info-box {
  gap: 10px;
}

.report-image {
  width: 300px;
  height: 200px;
}

.report-footer {
  display: flex;
  justify-content: end;
}
</style>
