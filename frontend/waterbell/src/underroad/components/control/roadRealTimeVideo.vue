<template lang="">
  <div class="cctv">
    <div class="entrance">지하차도 입구</div>
    <div class="internal">지하차도 내부</div>
  </div>
</template>
<script>
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'roadControlCctvVue'
})
</script>
<style scoped>
.cctv {
  display: flex;
  width: 1232.255px;
  height: 341.163px;
  justify-content: space-between;
  align-items: center;
  flex-shrink: 0;
}

.entrance {
  width: 550px;
  height: 280px;
  flex-shrink: 0;
  background: url(<path-to-image>),
    lightgray -9.84px 0px / 120.982% 100% no-repeat;
}

.internal {
  width: 550px;
  height: 280px;
  flex-shrink: 0;
  background: url(<path-to-image>),
    lightgray -9.84px 0px / 120.982% 100% no-repeat;
}
</style>
