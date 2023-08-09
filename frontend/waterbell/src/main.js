import { createApp, computed } from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import axios from 'axios';
import FontAwesomeIcon from './util/fontawesome-icons';
import { getClosestLocation } from './types/getMylocation';
// 날짜 포맷
// import { Vue } from 'vue-property-decorator'
// import { formatDate } from './util/filters'
// Vue.filter('formatDate', formatDate)
// import 'bootstrap/dist/css/bootstrap.min.css'
// import Geolocation from 'vue-geolocation-api'
axios.defaults.baseURL = 'http://localhost:8080';
const app = createApp(App);
app.component('font-awesome-icon', FontAwesomeIcon);
app.config.globalProperties.$http = axios;
app.config.globalProperties.$store = store;
app.use(store).use(router);
const facilityId = computed(() => store.getters['auth/facilityId']);
// fetchUnderroads 액션을 실행하고 완료될 때까지 기다립니다.
store
    .dispatch('fetchUnderroads')
    .then(async () => {
    // 액션이 완료되면 앱을 마운트합니다.
    const underroadList = computed(() => store.getters['auth/underroadList']);
    // console.log(underroadList.value)
    if (!Array.isArray(underroadList.value)) {
        throw new Error('underroadList.value is not iterable');
    }
    const result = await getClosestLocation(underroadList.value);
    return result;
})
    .then((result) => {
    // 결과 값을 auth 모듈의 facilityId에 저장합니다.
    if (facilityId.value == null) {
        store.commit('auth/setFacilityId', result.id);
        // console.log(result)
        store.commit('auth/setNowUnderroad', result);
    }
    // console.log('facility_id는?')
    // console.log(store.getters['auth/facilityId'])
    // 앱을 마운트합니다.
    app.mount('#app');
})
    .catch((error) => {
    console.error('Failed to fetch underroads: ', error);
});
//# sourceMappingURL=main.js.map