// types/websocket.ts 파일
import { computed, ref } from 'vue'
import { useStore } from 'vuex'

let socket: WebSocket | null = null
const store = useStore()
export function connectWebSocket(websocket: WebSocket | null): void {
  const logState = computed(() => store.getters.logState).value
  let jwtToken = ref<string | null>(null)
  if (logState) {
    jwtToken = computed(() => store.getters.loginUser)
  }
  //이미 소켓 연결되어 있을 경우
  if (websocket && websocket.readyState === WebSocket.OPEN) {
    return
  }

  // WebSocket 연결
  socket = new WebSocket(`ws://localhost:8080/ws`)

  socket.onopen = function () {
    socket?.send(`token:${jwtToken.value}`)
  }

  socket.onmessage = function (event) {
    const notification = event.data

    console.log('서버로부터 알림을 받았습니다: ' + notification)

    // 알림이 도착하면 알림 아이콘을 표시합니다.
  }

  socket.onclose = function () {
    console.log('WebSocket 연결이 종료되었습니다. 1초 후 재연결을 시도합니다.')

    setTimeout(() => connectWebSocket(socket), 1000)
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

export function closeWebSocket(socket: WebSocket | null) {
  socket?.close()
  console.log('웹소켓과의 연결을 끊었습니다.')
}

export default {
  connectWebSocket,
  closeWebSocket
}
