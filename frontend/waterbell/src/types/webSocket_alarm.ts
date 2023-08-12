// types/websocket.ts 파일
import { computed, ref } from 'vue'
import store from '@/store/index'

// const store = useStore()
let socket: WebSocket | null = null
export function connectWebSocket(): void {
  const logState = computed(() => store.getters['auth/isLogin']).value
  let jwtToken = ref<string | null>(null)
  if (logState) {
    jwtToken = computed(() => store.getters['auth/accessToken'])
  }
  //이미 소켓 연결되어 있을 경우
  if (socket && socket.readyState === WebSocket.OPEN) {
    return
  }
  if (jwtToken.value != null) {
    // WebSocket 연결
    socket = new WebSocket(`ws://localhost:8080/ws?token=${jwtToken.value}`)
    console.log('웹소켓과 연결이 되었습니다.')
  }
  // socket.onopen = function () {
  //   socket?.send(`token:${jwtToken.value}`)
  // }
  if (socket != null) {
    socket.onmessage = function (event) {
      const notification = event.data
      console.log('서버로부터 알림을 받았습니다: ' + notification)
      const isFlood = notification._flood

      if (isFlood) {
        //침수 경고일 때 모달창 띄우기
        console.log('침수경고 모달창 띄우기')
      } else {
        //신고접수일 때 모달창 띄우기
        console.log('신고접수 모달창 띄우기')
      }

      // 알림이 도착하면 알림 아이콘을 표시합니다.
    }

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
  console.log(socket)
  socket?.close()
  console.log('웹소켓과의 연결을 끊었습니다.')
}

export default {
  connectWebSocket,
  closeWebSocket
}
