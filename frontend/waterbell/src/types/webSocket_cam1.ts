// types/websocket.ts 파일
import { computed, ref } from 'vue'
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
      `${process.env.VUE_APP_WSAPI}/cam?facilityId=${facilityId.value}&camNum=1`
    )
    console.log('cam1 웹소켓과 연결이 되었습니다.')
  }
  // socket.onopen = function () {
  //   socket?.send(`token:${jwtToken.value}`)
  // }

  if (socket != null) {
    socket.onmessage = function (event) {
      const data = event.data
      // console.log(data)
      if (JSON.parse(data).camClient1 == 'camClient1') {
        store.commit('auth/setCamClient1', data.camClient1)
      } else {
        const base64Image = JSON.parse(data).temp_img
        // console.log('서버로부터 이미지string을 받았습니다: ' + base64Image)
        //이미지 처리 어떻게 할거야
        store.dispatch('auth/setCctvImg1', base64Image)
        const imgString = computed(() => store.getters['auth/cctvImg1'])
        // 이미지를 화면에 표시할 요소 생성
        const imgTag = document.getElementById('cctv1') as HTMLImageElement
        imgTag.src = 'data:image/jpeg;base64,' + imgString.value
      }
    }
    // 알림이 도착하면 알림 아이콘을 표시합니다.

    socket.onclose = function () {
      console.log(
        'cam1 WebSocket 연결이 종료되었습니다. 1초 후 재연결을 시도합니다.'
      )
      const imgTag = document.getElementById('cctv1') as HTMLImageElement

      try {
        if (imgTag) {
          setTimeout(() => connectWebSocket(), 1000)
        } else return
      } catch (error) {
        console.log('cam1 WebSocket 연결이 더 이상 불가합니다.')
      }
    }
  }
}

function closeWebSocket() {
  socket?.close()
  console.log('웹소켓과의 연결을 끊었습니다.')
}

export default {
  connectWebSocket,
  closeWebSocket
}
