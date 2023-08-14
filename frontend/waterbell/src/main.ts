import { createApp, computed, ref } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import auth from '@/store/index'
import axios from 'axios'
import VueDatePicker from '@vuepic/vue-datepicker'
import '@vuepic/vue-datepicker/dist/main.css'
import { fetchUnderroads } from './types/getUnderroadList'
// Use plugin with optional defaults
import FontAwesomeIcon from './util/fontawesome-icons'

import getMylocation, { getClosestLocation } from './types/getMylocation'

// 날짜 포맷
// import { Vue } from 'vue-property-decorator'
// import { formatDate } from './util/filters'
// Vue.filter('formatDate', formatDate)

// import 'bootstrap'
// import 'bootstrap/dist/css/bootstrap.min.css'

// import Geolocation from 'vue-geolocation-api'

// axios.defaults.baseURL = 'http://localhost:8080'

const app = createApp(App)
app.component('font-awesome-icon', FontAwesomeIcon)
app.component('VueDatePicker', VueDatePicker)
app.config.globalProperties.$http = axios
app.config.globalProperties.$store = store
app.use(store).use(router)
const facilityId = computed(() => auth.getters['auth/facilityId'])
// fetchUnderroads 액션을 실행하고 완료될 때까지 기다립니다.

fetchUnderroads()
  .then(() => {
    app.mount('#app')
  })

  // .then(async () => {
  //   // 액션이 완료되면 앱을 마운트합니다.
  //   const underroadList = computed(() => auth.getters['auth/underroadList'])
  //   // console.log(underroadList.value)
  //   // console.log(underroadList.value)
  //   if (!Array.isArray(underroadList.value)) {
  //     throw new Error('underroadList.value is not iterable')
  //   }

  //   const result = await getClosestLocation(underroadList.value)

  //   return result
  // })
  // .then((result) => {
  //   // 결과 값을 auth 모듈의 facilityId에 저장합니다.

  //   if (facilityId.value == null || facilityId.value == undefined) {
  //     auth.commit('auth/setFacilityId', result.id)
  //     // console.log(result)
  //     auth.commit('auth/setNowUnderroad', result)
  //   }

  .catch((error) => {
    console.error('Failed to fetch underroads: ', error)
  })
