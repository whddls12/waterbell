import store from '@/store/index';
import { computed } from 'vue';
//facility_id를 매개변수로 넣으면 그 facility 정보를 교체하는 함수
function changeFacility(id) {
    const underroadList = computed(() => store.getters['auth/underroadList']);
    console.log(underroadList.value);
    for (const road of underroadList.value) {
        //그 id와 일치하는 차도 찾아서 현재 지하차도 정보 교체
        if (id == road.id) {
            store.commit('auth/setFacilityId', road.id);
            store.commit('auth/setNowUnderroad', road);
            break;
        }
    }
}
export { changeFacility };
//# sourceMappingURL=changeFacility.js.map