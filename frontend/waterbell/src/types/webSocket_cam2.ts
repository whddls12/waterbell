// types/websocket.ts 파일
import { computed } from 'vue'
import store from '@/store/index'

// const store = useStore()
let socket: WebSocket | null = null
function connectWebSocket(): void {
  const facilityId = computed(() => store.getters['auth/facilityId'])

  //이미 소켓 연결되어 있을 경우
  if (socket && socket.readyState === WebSocket.OPEN) {
    return
  }
  if (facilityId.value != null) {
    // WebSocket 연결
    socket = new WebSocket(
      `ws://localhost:8080/cam?facilityId=${facilityId.value}&camNum=2`
    )
    console.log('웹소켓과 연결이 되었습니다.')
  }
  // socket.onopen = function () {
  //   socket?.send(`token:${jwtToken.value}`)
  // }
  if (socket != null) {
    socket.onmessage = function (event) {
      const data = event.data
      if (data.camClient1) {
        store.commit('setCamClient2', data.camClient2)
      } else {
        const base64Image = data.temp_img2 // 받아온 이미지 데이터. Base64로 인코딩되어 있다고 가정.
        console.log('서버로부터 이미지string을 받았습니다: ' + base64Image)

        // 이미지를 화면에 표시할 요소 생성
        const imgTag = document.getElementById('cctv2') as HTMLImageElement
        imgTag.src = 'data:image/jpeg;base64,' + base64Image
      }
    }
    // 알림이 도착하면 알림 아이콘을 표시합니다.

    socket.onclose = function () {
      console.log(
        'WebSocket 연결이 종료되었습니다. 1초 후 재연결을 시도합니다.'
      )
      const imgTag = document.getElementById('cctv2') as HTMLImageElement

      try {
        if (imgTag) {
          setTimeout(() => connectWebSocket(), 1000)
        } else return
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

function closeWebSocket() {
  socket?.close()
  console.log('웹소켓과의 연결을 끊었습니다.')
}

export default {
  connectWebSocket,
  closeWebSocket
}
