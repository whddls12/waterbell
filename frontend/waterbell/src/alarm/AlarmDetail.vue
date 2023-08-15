<template>
  <div class="mt-4">
    <h5 class="title">알림 상세</h5>
    <button @click="goBackToList" class="listBtn">목록</button>
    <div class="card">
      <div class="card-body">
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">알림 종류</label>
          <div class="col-10">
            <p class="form-control-plaintext">{{ alarm.alarmType }}</p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">발신자</label>
          <div class="col-10">
            <p class="form-control-plaintext">
              {{ alarm.sender }}
            </p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">등록일시</label>
          <div class="col-10">
            <p class="form-control-plaintext">
              {{ alarm.regDate }}
            </p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">수신확인</label>
          <div class="col-10">
            <p class="form-control-plaintext">
              {{ alarm.status }}
            </p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">내용</label>
          <div class="col-10">
            <p class="form-control-plaintext">
              {{ alarm.content }}
            </p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, computed } from 'vue'
import http from '@/types/http'
import { useRoute, useRouter } from 'vue-router'
import { mapGetters, useStore } from 'vuex'

export default defineComponent({
  name: 'AlarmDetail',
  computed: {
    ...mapGetters('auth', [
      'loginUser',
      'isLogin',
      'role',
      'accessToken',
      'refreshToken'
    ])
  },
  setup() {
    const route = useRoute()
    const router = useRouter()
    const alarm_id = route.params.alarm_id
    const store = useStore()
    const alarm = ref({
      id: '',
      content: '',
      alarmType: '',
      sender: '',
      regDate: '',
      status: ''
    })

    const goBackToList = () => {
      router.push('/alarm')
    }

    onMounted(() => {
      const role = computed(() => store.getters['auth/role']).value
      const token = computed(() => store.getters['auth/accessToken']).value
      http
        .get(`/alarm/${role}/detail/${alarm_id}`, {
          headers: {
            Authorization: `${token}`
          }
        })
        .then((res) => {
          alarm.value = res.data
        })
    })
    return {
      alarm,
      goBackToList
    }
  }
})
</script>

<style scoped lang="css">
.title {
  color: var(--typography-1, #1c2a53);
  text-align: center;
  font-family: score;
  /* 회원가입상자_제목 */
  font-size: 30px;
  font-style: normal;
  font-weight: 500;
  line-height: 16px; /* 53.333% */
  letter-spacing: 3px;
  margin-bottom: 40px;
  margin-top: 40px;
}
.container {
  width: 90%;
  margin: 0 auto;
}

.card {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 50px;
}

.label {
  font-weight: bold;
}

.form-control-plaintext {
  border: none;
  background-color: transparent;
  margin-left: 10px;
  margin-bottom: 0;
  padding: 0;
  font-size: 14px;
  line-height: 1.5;
}

/* 문단 간 테두리 스타일 */
.card-body > .row:not(:last-child) {
  border-bottom: 1px solid #e2e8f0;
}

.listBtn {
  display: flex;
  width: 99px;
  margin: 20px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 5px;
  border: 1px solid var(--1, #10316b);
  background-color: #f2f7ff;
  color: var(--1, #10316b);
  text-align: center;
  /* Subtitle2 */
  font-family: score;
  font-size: 14px;
  font-style: normal;
  font-weight: 500;
  line-height: 18px; /* 128.571% */
  letter-spacing: 0.1px;
}
</style>
