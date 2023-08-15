<template>
  <div>
    <p>신고접수 글 상세보기</p>
    <p>{{ report_id }}번 글</p>
  </div>
</template>
<script lang="ts">
import { defineComponent } from 'vue'
import { useRoute } from 'vue-router'

export default defineComponent({
  name: 'roadReportItemVue',
  setup() {
    const route = useRoute()
    const report_id = route.params.report_id

    return { report_id }
  }
})
</script>
<style></style>
