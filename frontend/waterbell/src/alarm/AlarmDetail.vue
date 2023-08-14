<template>
  <div class="container mt-4">
    <h5 class="my-4">알림 상세</h5>
    <div class="card">
      <div class="card-body">
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">내용:</label>
          <div class="col-10">
            <p class="form-control-plaintext">
              <b>내용:</b> {{ alarm.content }}
            </p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">알림 종류:</label>
          <div class="col-10">
            <p class="form-control-plaintext">
              <b>알림 종류:</b> {{ alarm.alarmType }}
            </p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">발신자:</label>
          <div class="col-10">
            <p class="form-control-plaintext">
              <b>발신자:</b> {{ alarm.sender }}
            </p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">등록일시:</label>
          <div class="col-10">
            <p class="form-control-plaintext">
              <b>등록일시:</b> {{ alarm.regDate }}
            </p>
          </div>
        </div>
        <div class="row mb-3">
          <label class="col-2 col-form-label fw-bold">처리상태:</label>
          <div class="col-10">
            <p class="form-control-plaintext">
              <b>처리상태:</b> {{ alarm.status }}
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="mt-4">
      <button @click="goBackToList" class="btn btn-primary">목록</button>
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

<style>
.container {
  width: 90%;
  margin: 0 auto;
}

.card {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 20px;
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
</style>
