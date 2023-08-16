<template>
  <!-- 회원정보 수정 페이지 -->
  <div class="myPage">
    <div class="myPage-title">
      <h1>회원정보 수정</h1>
    </div>
    <div class="myPage-content">
      <!-- 이름 -->
      <div v-if="role == 'APART_MEMBER'" class="myPage-content-box name">
        <label for="name">이름</label>
        <input
          type="text"
          id="name"
          :placeholder="memberInfo?.name"
          v-model="newName"
        />
      </div>
      <!-- 아이디 -->
      <div class="myPage-content-box loginId">
        <label for="id">아이디</label>
        <input type="text" id="loginId" disabled :value="memberInfo?.loginId" />
      </div>
      <!-- 비밀번호 -->
      <div v-if="role == 'APART_MEMBER'" class="myPage-content-box phone">
        <label for="password">현재 비밀번호</label>
        <div class="inputbtn">
          <input type="password" id="password" disabled value="********" />
          <button id="changebtn" @click="changePopState_PW">변경</button>
        </div>
        <parkPasswordModal v-if="popState_pw" @close="changePopState_PW" />
      </div>
      <!-- 휴대폰번호 -->
      <div class="myPage-content-box phone">
        <label for="phone">휴대폰 번호</label>
        <div class="inputbtn">
          <input type="text" id="phone" disabled :value="memberInfo?.phone" />
          <button id="changebtn" @click="changePopState_Phone">변경</button>
        </div>
        <parkPhoneModal
          v-if="popState_phone"
          @close="changePopState_Phone"
          @verify-success="tempPhoneNumber"
        />
      </div>
      <!-- 아파트 인증코드 -->
      <div
        v-if="role == 'APART_MEMBER' || role == 'APART_MANAGER'"
        class="myPage-content-box apartCode"
      >
        <label for="apartCode">아파트 인증코드</label>
        <div class="inputbtn">
          <input
            v-if="role == 'APART_MANAGER'"
            type="text"
            id="apartCode"
            disabled
            :placeholder="memberInfo?.apartCode"
            v-model="newApartCode"
          />
          <input
            v-else
            type="text"
            id="apartCode"
            :placeholder="memberInfo?.apartCode"
            v-model="newApartCode"
          />
          <button id="changebtn" @click.prevent="findAddressByCode">
            변경
          </button>
        </div>
      </div>
      <!-- 주소 -->
      <div
        v-if="role == 'APART_MEMBER' || role == 'APART_MANAGER'"
        class="myPage-content-box address"
      >
        <label for="address">주소</label>
        <input
          type="text"
          id="address"
          readonly
          :placeholder="memberInfo?.address"
        />
      </div>
      <!-- 호수 -->
      <div
        v-if="role == 'APART_MEMBER'"
        class="myPage-content-box addressNumber"
      >
        <label for="addressNumber">호수</label>
        <div class="addressNumber-box">
          <input
            type="text"
            id="addressNumber"
            :placeholder="memberInfo?.addressNumber"
            v-model="newApartAddressNumber"
          />
          <p>호</p>
        </div>
      </div>
      <!-- 버튼 -->
      <div class="myPage-btn">
        <button id="goBack" @click="goBack">취소</button>
        <button id="saveChange" @click="saveChange">저장</button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, onMounted, computed, defineComponent } from 'vue'
import router from '@/router'
import axios from '@/types/apiClient'
import store from '@/store/index'
import parkPasswordModal from './parkPasswordModal.vue'
import parkPhoneModal from './parkPhoneModal.vue'

export default defineComponent({
  name: 'parkMypageUpdate',
  components: {
    parkPasswordModal,
    parkPhoneModal
  },
  setup() {
    const role = computed(() => store.getters['auth/role']).value

    const api = axios.api
    const apiClient = axios.apiClient(store)
    const memberInfo = ref(null)

    const popState_pw = ref(false)
    const popState_phone = ref(false)

    // 수정할 회원정보
    const newName = ref(null)
    const newPhoneNum = ref(null)
    const newApartCode = ref(null)
    const newApartAddressNumber = ref(null)

    // 회원정보 가져오기
    function getMemberData() {
      apiClient
        .get(`/member/mypage`)
        .then((res) => {
          memberInfo.value = res.data.memberInfo
          console.log(memberInfo.value)
          // 수정 시 넘겨줄 데이터에 기존값 저장
          newName.value = res.data.memberInfo.name
          console.log(newName.value)
          newPhoneNum.value = res.data.memberInfo.phone
          newApartCode.value = res.data.memberInfo.apartCode
          newApartAddressNumber.value = res.data.memberInfo.addressNumber
        })
        .catch((error) => console.log(error))
    }

    function changePopState_PW() {
      popState_pw.value = !popState_pw.value
    }
    function changePopState_Phone() {
      popState_phone.value = !popState_phone.value
    }

    // 바꿀 휴대폰 번호로 인증을 완료했을 때 수정 페이지에서 임시로 휴대폰 번호 변경
    function tempPhoneNumber(tempNum) {
      // console.log('tempPhoneNumber 실행')
      // console.log(tempNum)
      memberInfo.value.phone = tempNum
      newPhoneNum.value = tempNum
    }
    //아파트 코드로 주소 조회
    const findAddressByCode = () => {
      try {
        api
          .post('/member/join/validationApartCode', {
            apartCode: newApartCode.value
          })
          .then((res) => {
            if (res.data.message == 'success') {
              memberInfo.value.address = res.data.address // 수정할 아파트 코드에 맞추어 주소를 바꿔 보여주기
            } else {
              alert('일치하는 아파트가 없습니다.')
            }
            // console.log(apartCode.value)
            console.log(res)
            console.log(res.data)
          })
      } catch (error) {
        alert('일치하는 아파트가 없습니다.')
      }
    }

    function goBack() {
      router.push({ path: '/mypage' })
    }

    function saveChange() {
      if (role == 'APART_MEMBER') {
        apiClient
          .post(`/member/apartMember/modify`, {
            id: memberInfo.value.id,
            phone: newPhoneNum.value,
            name: newName.value,
            apartCode: newApartCode.value,
            addressNumber: newApartAddressNumber.value
          })
          .then((res) => {
            console.log(res)
            router.push({ path: '/mypage' })
          })
          .catch((err) => console.log(err))
      } else if (role == 'APART_MANAGER' || role == 'PUBLIC_MANAGER') {
        apiClient
          .post(`/member/manager/modify`, {
            id: memberInfo.value.id,
            phone: newPhoneNum.value,
            name: newName.value,
            apartCode: newApartCode.value,
            addressNumber: newApartAddressNumber.value
          })
          .then((res) => {
            console.log(res)
            router.push({ path: '/mypage' })
          })
          .catch((err) => console.log(err))
      }
    }

    // 토큰을 백으로 보내서 해당 회원정보를 받아온 후 화면에 띄워준다.
    onMounted(() => {
      getMemberData()
    })
    return {
      role,
      memberInfo,
      newName,
      newApartAddressNumber,
      newPhoneNum,
      newApartCode,
      popState_pw,
      popState_phone,
      getMemberData,
      changePopState_PW,
      changePopState_Phone,
      tempPhoneNumber,
      findAddressByCode,
      goBack,
      saveChange
    }
  }
})
</script>
<style scoped>
.myPage {
  display: flex;
  width: inherit;
  flex-direction: column;
  align-items: center;
}

.myPage-content-box {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  width: 660px;
  margin: 20px 0px;
  font-size: 1.5rem;
  gap: 5px;
}

input {
  border-radius: 8px;
  box-sizing: border-box;
  width: inherit;
  height: 60px;
  font-size: 1.5rem;
  padding-left: 10px;
}

#addressNumber {
  width: 214px;
}

.addressNumber-box {
  display: flex;
  align-items: center;
  gap: 10px;
}

.myPage-btn {
  display: flex;
  justify-content: center;
  gap: 40px;
}

.myPage-btn > button {
  width: 288px;
  height: 60px;
  border: none;
  border-radius: 40px;

  color: white;
  font-family: Roboto;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 32px; /* 133.333% */
  letter-spacing: 7px;
}

.myPage-btn > button:hover {
  cursor: pointer;
}

#goBack {
  background-color: #7b7979;
}

#saveChange {
  background-color: #ff8901;
}

#password,
#phone,
#apartCode {
  width: 530px;
}

#changebtn {
  border: none;
  border-radius: 10px;
  height: 60px;
  width: 114px;

  background-color: #ffa132;
  color: white;
  font-size: 20px;
  font-weight: 300;
  letter-spacing: 0.5px;
}

#changebtn:hover {
  cursor: pointer;
  background-color: #ff8901;
}

.inputbtn {
  display: flex;
  width: 660px;
  gap: 20px;
}
</style>
