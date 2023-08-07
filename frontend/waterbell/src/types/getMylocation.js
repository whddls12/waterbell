import { getDistance } from 'geolib';
const getMylocation = () => {
    // HTML5의 geolocation으로 사용할 수 있는지 확인합니다
    return new Promise((resolve) => {
        if (navigator.geolocation) {
            // GeoLocation을 이용해서 접속 위치를 얻어옵니다
            navigator.geolocation.getCurrentPosition(function (position) {
                const lat = position.coords.latitude; // 위도
                const lon = position.coords.longitude; // 경도
                resolve({ latitude: lat, longitude: lon });
            }, () => resolve({ latitude: '36.3405', longitude: '127.3939' }) // 위치 정보를 가져오는 데 실패했을 경우의 에러 처리
            );
        }
        else {
            resolve({ latitude: '36.3405', longitude: '127.3939' });
        }
    });
};
const getClosestLocation = async (underroads) => {
    const myLocation = await getMylocation();
    let closestLocation = null;
    let minDistance = Number.MAX_VALUE;
    // console.log(underroads)
    for (const road of underroads) {
        const distance = getDistance({ latitude: myLocation.latitude, longitude: myLocation.longitude }, { latitude: road.latitude, longitude: road.longitude });
        if (distance < minDistance) {
            minDistance = distance;
            closestLocation = road;
        }
    }
    return closestLocation;
};
export default getMylocation;
export { getClosestLocation };
//# sourceMappingURL=getMylocation.js.map