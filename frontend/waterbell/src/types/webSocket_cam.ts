// types/websocket.ts 파일
import { computed, ref } from 'vue'
import store from '@/store/index'

// const store = useStore()
let socket: WebSocket | null = null
export function connectWebSocket(): void {
  const logState = computed(() => store.getters['auth/isLogin']).value
  let facilityId = ref<string | null>(null)
  if (logState) {
    facilityId = computed(() => store.getters['auth/facilityId'])
  }
  //이미 소켓 연결되어 있을 경우
  if (socket && socket.readyState === WebSocket.OPEN) {
    return
  }
  if (facilityId.value != null) {
    // WebSocket 연결
    socket = new WebSocket(
      `ws://localhost:8080/cam?facilityId=${facilityId.value}`
    )
    console.log('웹소켓과 연결이 되었습니다.')
  }
  // socket.onopen = function () {
  //   socket?.send(`token:${jwtToken.value}`)
  // }
  if (socket != null) {
    socket.onmessage = function (event) {
      const base64Image = event.data // 받아온 이미지 데이터. Base64로 인코딩되어 있다고 가정.
      console.log('서버로부터 이미지string을 받았습니다: ' + base64Image)
      //이미지 처리 어떻게 할거야

      // 이미지를 화면에 표시할 요소 생성
      let img = document.createElement('img')

      // 받아온 Base64 데이터를 이용해 이미지 URL 생성
      img.src = 'data:image/png;base64,' + base64Image

      // img 요소를 화면에 붙이기

      //붙이고 싶은 요소 수정할 것.
      document.body.appendChild(img)
    }
    // 알림이 도착하면 알림 아이콘을 표시합니다.

    socket.onclose = function () {
      console.log(
        'WebSocket 연결이 종료되었습니다. 1초 후 재연결을 시도합니다.'
      )
      try {
        setTimeout(() => connectWebSocket(), 1000)
      } catch (error) {
        console.log('WebSocket 연결이 더 이상 불가합니다.')
      }
    }
  }
}

// export function showAlarmModal(modalState): void {
//   modalState.value = true
//   // 알림 모달을 표시하는 로직
// }

// export function closeAlarmModal(modalState): void {
//   // 알림 모달을 닫는 로직
//   modalState.value = false
// }

export function closeWebSocket() {
  socket?.close()
  console.log('웹소켓과의 연결을 끊었습니다.')
}

export default {
  connectWebSocket,
  closeWebSocket
}
