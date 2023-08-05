import axios from 'axios';
// 토큰을 가져오는 함수
// 여기서는 localStorage를 예로 들었지만, 다른 곳에 토큰을 저장했다면 그에 맞게 수정하세요.
// state에 저장할 예정. 가져와야 함.
function getToken() {
    return localStorage.getItem('token');
}
// axios 인스턴스 생성
const apiClient = axios.create();
// request interceptor 설정
apiClient.interceptors.request.use((config) => {
    const token = getToken();
    if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});
export default apiClient;
//# sourceMappingURL=apiClient.js.map