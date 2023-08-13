// types/websocket.ts 파일
import { computed, ref } from 'vue'
import store from '@/store/index'
import apiModul from '@/types/apiClient'

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
    socket.onmessage = async function (event) {
      const notification = JSON.parse(event.data)
      console.log('서버로부터 알림을 받았습니다: ' + notification)
      const isFlood = notification._flood
      const isApart = notification._apart
      const api = apiModul.api
      let facilityName = ''
      if (isApart) {
        api.get(`/facilities/${notification.facility_id}/apart`).then((res) => {
          facilityName = res.data.apart.apartName
        })
      } else {
        const underroadList = store.getters['auth/underroadList']
        for (const road of underroadList) {
          facilityName = road.undergroundRoadName
        }
      }

      //---------------------지하주차장 침수 메시지
      if (isFlood) {
        //침수경고일 때 메시지 만들기.
        const waterNotification = {
          regDate: notification.regDate,
          content: notification.content,
          facilityId: notification.facility_id,
          facilityName: facilityName,
          isApart: isApart
        }
        await store.commit('setWaterNotification', waterNotification)
        store.dispatch('toggleWaterModal')
      } else {
        const boardNotification = {
          regDate: notification.regDate,
          content: notification.content,
          facilityId: notification.facility_id,
          facilityName: facilityName,
          isAapart: isApart,
          boardId:
            isApart == true
              ? notification.apart_board_id
              : notification.underground_board_id
        }
        await store.commit('setBoardNotification', boardNotification)
        store.dispatch('toggleBoardModal')
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
