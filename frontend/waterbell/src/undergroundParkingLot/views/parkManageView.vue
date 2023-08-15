<template>
  <div class="main">
    <div class="select-region">
      <div class="select-region-box">
        <router-link to="/park/manage/custom">메시지/기준치 설정</router-link>
      </div>
      <div class="select-region-box">
        <router-link to="/park/manage/member">입주민관리</router-link>
      </div>
    </div>
    <router-view class="router-view-style"></router-view>
  </div>
</template>

<script>
export default {}
</script>

<style scoped>
.main {
  width: 100%;
}
.select-region {
  display: flex;
  justify-content: center;
}

.select-region-box {
  border: 1px solid #939393;
  border-radius: 8px;
  background-color: white;

  width: 200px;
  margin: 10px 10px;
}

a {
  color: #10316b;
  text-decoration: none;
}

.router-view-style {
  padding: 0 30px;
  min-height: 500px;
  min-width: 600px;
  overflow: hidden;
}
</style>
