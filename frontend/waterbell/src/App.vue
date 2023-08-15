<template>
  <div id="nav">
    <Home />

    <!-- <button
      type="button"
      class="btn btn-primary"
      data-bs-toggle="modal"
      data-bs-target="#exampleModal"
    >
      Launch demo modal
    </button> -->

    <!-- Modal -->
    <!-- -------------------------------침수관련 모달 ------------------------------------ -->
    <div class="modal fade" id="waterModal" ref="waterModalRef">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="waterModalLabel">침수 경고 알림</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <h4>알림</h4>

            <p class="text-start">
              위치 :
              {{ waterNotification ? waterNotification.facilityName : '' }}
            </p>
            <p>내용 {{ waterNotification ? waterNotification.content : '' }}</p>
          </div>

          <div class="modal-footer justify-content-center">
            <button
              type="button"
              class="btn modal-btn btn-lg col-5"
              @click="moveToControl"
              v-if="isControlBtn"
              data-bs-dismiss="modal"
            >
              제어하기
            </button>
            <button
              type="button"
              class="btn modal-close-btn btn-lg col-5"
              data-bs-dismiss="modal"
            >
              닫기
            </button>
          </div>
        </div>
      </div>
    </div>
    <!-- Modal -->
    <!-- -------------------------------신고접수관련 모달 ------------------------------------ -->
    <div class="modal fade" id="boardModal" ref="boardModalRef">
      <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="boadModalLabel">신고접수 알림</h5>
            <button
              type="button"
              class="btn-close"
              data-bs-dismiss="modal"
              aria-label="Close"
            ></button>
          </div>
          <div class="modal-body">
            <h4>알림</h4>
            <p class="text-end">
              접수일시 :
              {{ boardNotification ? boardNotification.regDate : '' }}
            </p>
            <p class="text-start">
              위치 :
              {{ boardNotification ? boardNotification.facilityName : '' }}
            </p>
            <p class="text-start">내용</p>
            <p>{{ boardNotification ? boardNotification.content : '' }}</p>
          </div>

          <div class="modal-footer justify-content-center">
            <button
              type="button"
              class="btn modal-btn btn-lg col-5"
              @click="moveToBoardDetail"
              data-bs-dismiss="modal"
            >
              접수하기
            </button>
            <button
              type="button"
              class="btn modal-close-btn btn-lg col-5"
              data-bs-dismiss="modal"
            >
              닫기
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import {
  defineComponent,
  ref,
  onMounted,
  computed,
  watch,
  onBeforeUnmount
} from 'vue'
// import TheHeader from './components/TheHeader.vue'
import Home from './views/Home.vue'
import webSocket from '@/types/webSocket_alarm'
import store from '@/store/index'
import { useRouter } from 'vue-router'
// import { useStore } from 'vuex'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
import * as bootstrap from 'bootstrap'
import { changeFacility } from '@/types/changeFacility'
export default defineComponent({
  name: 'App',
  components: {
    Home
    // TheHeader
  },
  setup() {
    const router = useRouter()
    const showModal = computed(() => store.getters['showWaterModal'])
    const waterNotification = computed(() => store.state.waterNotification)
    const boardNotification = computed(() => store.state.boardNotification)
    const waterModal = ref<bootstrap.Modal | null>(null)
    const boardModal = ref<bootstrap.Modal | null>(null)
    const role = computed(() => store.getters['auth/role'])
    const isControlBtn = computed(() => {
      return role.value == 'APART_MANAGER' || role.value == 'PUBLIC_MANAGER'
    })

    const waterModalRef = ref<HTMLElement | null>(null)
    const boardModalRef = ref<HTMLElement | null>(null)

    let waterModalInstance: bootstrap.Modal | null = null
    let boardModalInstance: bootstrap.Modal | null = null

    const toggleWaterModal = () => {
      store.dispatch('toggleWaterModal')
      // if (waterModalInstance) {
      //   if (store.state.showWaterModal) {
      //     waterModalInstance.show()
      //   } else {
      //     waterModalInstance.hide()
      //   }
      // }
    }

    const toggleBoardModal = () => {
      store.dispatch('toggleBoardModal')
    }

    const setWaterModal = () => {
      // console.log('onMounted에서 모달이 set됩니다.')
      const element = document.getElementById('waterModal')
      if (element) {
        waterModal.value = new bootstrap.Modal(element)
      }
    }
    const setBoardModal = () => {
      // console.log('onMounted에서 모달이 set됩니다.')
      const element = document.getElementById('boardModal')
      if (element) {
        boardModal.value = new bootstrap.Modal(element)
      }
    }
    const closeWaterModal = () => {
      store.dispatch('closeWaterModal')
      if (document.querySelector('.modal-backdrop')) {
        document.querySelector('.modal-backdrop')?.remove()
      }
    }
    const closeBoardModal = () => {
      store.dispatch('closeBoardModal')
      if (document.querySelector('.modal-backdrop')) {
        document.querySelector('.modal-backdrop')?.remove()
      }
    }

    const moveToControl = async () => {
      if (waterNotification.value.facilityId) {
        await changeFacility(waterNotification.value.facilityId)
      }
      //park인지 아파트인지 구분 ......
      if (role.value == 'PUBLIC_MANAGER') {
        router.push('/road/control')
      } else {
        router.push('/park/control')
      }
    }

    const moveToBoardDetail = async () => {
      if (boardNotification.value && boardNotification.value.facilityId) {
        await changeFacility(waterNotification.value.facilityId)
      }

      if (role.value == 'APART_MANAGER') {
        router.push(`/park/report?boardId=${boardNotification.value.boardId}`)
      } else {
        router.push(`/road/report?boardId=${boardNotification.value.boardId}`)
      }
    }
    watch(
      () => store.state.showWaterModal,
      (newValue) => {
        if (waterModal.value) {
          if (newValue == true) {
            waterModal.value.show()
          } else {
            waterModal.value.hide()
          }
        }
      }
    )
    watch(
      () => store.state.showBoardModal,
      (newValue) => {
        if (boardModal.value) {
          if (newValue == true) {
            boardModal.value.show()
          } else {
            boardModal.value.hide()
          }
        }
      }
    )

    onMounted(async () => {
      const isLogin = computed(() => store.getters['auth/isLogin'])
      // console.log(isLogin.value)
      if (isLogin.value) {
        await webSocket.connectWebSocket() //새로고침 시, 웹소켓 연결
      }
      // 침수 모달 설정
      if (waterModalRef.value) {
        waterModalInstance = new bootstrap.Modal(waterModalRef.value)
        waterModalRef.value.addEventListener('hidden.bs.modal', closeWaterModal)
      }

      // 신고접수 모달 설정
      if (boardModalRef.value) {
        boardModalInstance = new bootstrap.Modal(boardModalRef.value)
        boardModalRef.value.addEventListener('hidden.bs.modal', closeBoardModal)
      }

      setWaterModal()
      setBoardModal()
    })

    onBeforeUnmount(() => {
      // 침수 모달 이벤트 리스너 제거
      if (waterModalRef.value && waterModalInstance) {
        waterModalRef.value.removeEventListener(
          'hidden.bs.modal',
          closeWaterModal
        )
      }

      // 신고접수 모달 이벤트 리스너 제거
      if (boardModalRef.value && boardModalInstance) {
        boardModalRef.value.removeEventListener(
          'hidden.bs.modal',
          closeBoardModal
        )
      }
    })
    return {
      showModal,
      waterNotification,
      closeWaterModal,
      closeBoardModal,
      toggleWaterModal,
      setWaterModal,
      moveToControl,
      isControlBtn,
      toggleBoardModal,
      boardNotification,
      moveToBoardDetail,
      waterModalRef,
      boardModalRef
    }
  }
})
</script>

<style>
@font-face {
  font-family: 'score';
  font-weight: 100;
  src: url('./font/SCDream1.otf');
}
@font-face {
  font-family: 'score';
  font-weight: 200;
  src: url('./font/SCDream2.otf');
}
@font-face {
  font-family: 'score';
  font-weight: 300;
  src: url('./font/SCDream3.otf');
}
@font-face {
  font-family: 'score';
  font-weight: 400;
  src: url('./font/SCDream4.otf');
}
@font-face {
  font-family: 'score';
  font-weight: 500;
  src: url('./font/SCDream5.otf');
}
@font-face {
  font-family: 'score';
  font-weight: 600;
  src: url('./font/SCDream6.otf');
}
@font-face {
  font-family: 'score';
  font-weight: 700;
  src: url('./font/SCDream7.otf');
}
@font-face {
  font-family: 'score';
  font-weight: 800;
  src: url('./font/SCDream8.otf');
}
@font-face {
  font-family: 'score';
  font-weight: 900;
  src: url('./font/SCDream9.otf');
}

#app {
  margin: auto;
  text-align: center;
  color: #2c3e50;
  background-color: #f2f7ff;
}

/* 홈페이지 배경색 설정 & 테두리여백 제거 */
* body {
  background-color: #f2f7ff;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 80px;
}

/* 홈페이지 구성요소 세로로 정렬 */
#nav {
  font-family: 'score';
  font-weight: 500;

  display: flex;
  flex-direction: column;
  flex-grow: 1;
  /* justify-content: center; */
  /* align-content: flex-end; */
  padding: 0;
  align-items: stretch;
  width: 100%;

  /* height: 100vh; */
}

/* 헤더상단 환영메시지 정렬을 위해 블럭화 */
#hello-msg {
  display: inline-block;
}

/* 자주 쓰이는 컴포넌트 */

.container {
  border-radius: 10px;
  background: var(--unnamed, #f2f7ff);
  box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
}

/* 각 대시보드 박스들 */
.dash-box {
  display: flex;
  flex-direction: column;
  padding: 10px;
}
/* 대시보드 제목 */
.dash-box-title {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  gap: 4px; /* 아이콘과 제목 사이 간격 */
}
/* 대시보드 내용 */
.dash-box-content {
  border: 1px solid #cdd1de;
  background-color: white;
  border-radius: 10px;
  padding: 20px;
}

.modal-btn {
  border: 1px solid #ffa132;
  border-radius: 10px;
  background: var(--unnamed, #ffa132);
  color: #fff;
  text-align: center;
}
.modal-btn:hover {
  border-color: #ffa132; /* 호버될 때 테두리 색상을 #ffa132로 설정합니다. */
}

.modal-close-btn {
  border-radius: 10px;
  border: 1px solid rgba(123, 121, 121, 0.7);
  background: rgba(123, 121, 121, 0.7);
  color: #fff;
  text-align: center;
}

.modal-close-btn:hover {
  border-color: rgba(123, 121, 121, 0.7);
}
</style>
