<template lang="">
  <div class="main">
    <img class="siren" :src="sirenImage" />
    <div class="height">
      <img class="water" src="@/assets/images/Megaphone.png" />
      <div class="state">{{ currentState }}</div>
    </div>
    <div class="height">
      <img class="water" src="@/assets/images/Vector.png" />
      <div class="heigth-value">{{ currentHeight }}</div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, computed, ref, onMounted, watch } from 'vue'
import { useStore } from 'vuex'
import { mapGetters } from 'vuex'
import apiModule from '@/types/apiClient'
import SirenGreenImage from '@/assets/images/Siren_green.png'
import SirenOrange from '@/assets/images/Siren_orange.png'
import SirenRed from '@/assets/images/Siren_red.png'

// import http from '@/types/http'

export default defineComponent({
  name: 'ParkSirenControl',
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
    const apiClient = apiModule.apiClient
    const api = apiModule.api
    const store = useStore()
    const facility_id = computed(() => store.getters['auth/facilityId']).value
    const sirenImage = ref(SirenGreenImage)
    const currentState = ref('윤영이가 넣어줄 문구')
    const currentHeight = ref('')
    const actionTriggered = computed(() => store.state.actionTriggered)
    const fetchHeightData = async () => {
      try {
        const response = await apiClient.get(
          `/system/manager/facilities/${facility_id}/sensors/HEIGHT/latest`
        )
        currentHeight.value = '현재수위 : ' + response.data + 'mm'
      } catch (error) {
        console.error('Error fetching height data:', error)
      }
    }

    const fetchStatusData = async () => {
      try {
        const response = await api.get(`/facilities/${facility_id}/status`)
        if (response.data == 'DEFAULT') {
          sirenImage.value = SirenGreenImage
        } else if (response.data == 'FIRST' || response.data == 'SECOND') {
          sirenImage.value = SirenOrange
        } else {
          sirenImage.value = SirenRed
        }
      } catch (error) {
        console.error('Error fetching status data:', error)
      }
    }

    watch(
      () => actionTriggered.value,
      (newValue: any) => {
        console.log('변경감지')
        fetchHeightData()
        fetchStatusData()
      }
    )

    onMounted(async () => {
      await fetchHeightData()
      await fetchStatusData()
    })

    return {
      sirenImage,
      currentState,
      currentHeight,
      store
    }
  }
})
</script>

<style scoped>
.main {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 400px;
  height: 180.5px;
  background: #f2f7ff;
  box-shadow: 0px 2px 15px rgba(0, 0, 0, 0.17); /* 크기가 줄어드므로 그림자도 조절 */
  border-radius: 6px; /* 반경도 절반으로 줄임 */
}

.siren {
  width: 60px;
  height: 60px;
  margin-bottom: 20px;
}

.state {
  color: #114cb1;
  font-size: 20px;
  font-family: Roboto;
  font-weight: 600;
  line-height: 70px;
  word-wrap: break-word;
}

.height {
  width: 345px;
  height: 35px;
  align-items: center;
  gap: 10px;
  display: flex; /* 가로 정렬을 위한 추가 */
}

.heigth-value {
  color: #114cb1;
  font-size: 20px;
  font-family: Roboto;
  font-weight: 600;
  line-height: 24px;
  word-wrap: break-word;
}

.water {
  width: 30px;
  height: 30px;
}
</style>
