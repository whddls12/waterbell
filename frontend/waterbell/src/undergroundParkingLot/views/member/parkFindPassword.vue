<template>
  <div class="find-password">
    <div class="find-password-title">
      <h1>비밀번호 찾기</h1>
    </div>
    <div class="find-password-content">
      <div class="find-password-content-box">
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" />
      </div>
      <div class="find-password-content-box">
        <label for="userId">아이디</label>
        <input type="text" id="userId" v-model="userId" />
      </div>
      <div class="find-password-content-box">
        <div class="phone-verification">
          <label for="phoneNum">휴대폰 번호</label><br />
          <input
            type="text"
            label="phoneNum"
            id="phoneNum"
            v-model="phoneNum"
          />
          <button id="verify-request" @click.prevent="requestVerification">
            인증하기
          </button>
        </div>
        <p :class="{ 'p-msg': true, success: validate.phoneVerification }">
          {{ phoneMsg }}
        </p>
        <div class="verification" v-if="verificationVisible">
          <label for="verification">인증번호 입력</label><br />
          <input
            type="text"
            label="verification"
            placeholder="인증번호를 입력해주세요."
            id="verification"
            v-model="verification"
          />
          <p>{{ formattedCountdown }}</p>
        </div>
      </div>

      <div class="find-password-bt">
        <button id="check" @click.prevent="onConfirmClick">확인</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { ref, watch, computed, defineComponent } from 'vue'
import axios from '@/types/apiClient'
import store from '@/store/index'
import router from '@/router/index'

export default defineComponent({
  name: 'parkFindPassword',
  setup() {
    // api 요청을 위한 변수
    const apiClient = axios.apiClient(store)
    const api = axios.api

    // 입력한 변수들
    const name = ref(null)
    const userId = ref(null)
    const phoneNum = ref('')
    const verification = ref('')

    // 입력창 아래 띄워질 메시지
    const phoneMsg = ref('')

    // 휴대폰번호 인증 확인 여부
    const offerPhone = ref(false)

    // 인증번호 요청 및 확인 관련 변수
    const verificationVisible = ref(false)
    const countdown = ref(180)

    const validateRule = {
      phone: /^010[0-9]{8}$/,
      verification: /^[0-9]{6}$/
    }

    const validate = ref({
      phoneNum: false,
      phoneVerification: false
    })

    const validatePhone = () => {
      if (phoneNum.value == '') {
        validate.value.phoneNum = false
        phoneMsg.value = '필수입력 항목입니다.'
      } else if (!validateRule.phone.test(phoneNum.value)) {
        validate.value.phoneNum = false
        phoneMsg.value = '010으로 시작하는 11자리 숫자를 입력해주세요.'
      } else if (!validate.value.phoneVerification) {
        validate.value.phoneNum = true
        phoneMsg.value = '휴대폰 인증을 진행하세요.'
      } else {
        validate.value.phoneNum = true
        phoneMsg.value = '휴대폰 인증이 완료되었습니다.'
      }
    }
    //인증번호 요청 이후 카운트 다운
    const requestVerification = async () => {
      if (validate.value.phoneNum) {
        await postVerification()
        verificationVisible.value = true
        // 2. 3분 카운트다운 시작
        setInterval(() => {
          countdown.value -= 1
          // 3. 인증시간 만료 시 메시지 표시 및 input 비활성화
          if (countdown.value === 0) {
            phoneMsg.value = '인증시간이 만료되었습니다. 인증을 재요청해주세요.'
            countdown.value = 180
            verification.value = ''
            verificationVisible.value = false
          }
        }, 1000)
      }
    }
    //인증번호 요청
    const postVerification = () => {
      offerPhone.value = true
      const phoneNumber = phoneNum.value
      try {
        api
          .post('/verification/code', {
            phoneNumber: phoneNumber
          })
          .then((res) => {
            if (res.data.code == 200) {
              //요청 보냈음을 확인하는 변수
              // offerPhone.value = true
              //시간 카운트 다운
              verificationVisible.value = true
            }
          })
      } catch (error) {
        validate.value.phoneVerification = false
        alert('인증 요청 실패. 다시 요청하세요.')
      }
    }
    //인증번호 유효시간 카운트다운
    const formattedCountdown = computed(() => {
      const minutes = Math.floor(countdown.value / 60)
      const seconds = countdown.value % 60
      return `${minutes.toString().padStart(2, '0')}:${seconds
        .toString()
        .padStart(2, '0')}`
    })
    //인증번호 확인
    const confirmVerification = async () => {
      try {
        await api
          .get('/verification/code', {
            params: {
              phoneNumber: phoneNum.value,
              code: verification.value
            }
          })
          .then((res) => {
            if (res.data.message == '인증완료') {
              validate.value.phoneVerification = true
              phoneMsg.value = '휴대폰 인증이 완료되었습니다.'
              setTimeout(
                () => router.push({ path: '/park/find/password/reset' }),
                1000
              )
            }
          })
      } catch (error) {
        validate.value.phoneVerification = false

        console.log(error)
        phoneMsg.value = '인증번호가 틀렸습니다. 시간 내에 다시 입력하세요.'
      }
    }
    //인증번호 확인 클릭
    const onConfirmClick = () => {
      confirmVerification()
    }

    // 휴대폰번호 검증을 위한 watch 함수
    watch(phoneNum, (newValue, oldValue) => {
      if (newValue != oldValue) {
        validatePhone()
      }
    })

    return {
      name,
      userId,
      phoneNum,
      verification,
      phoneMsg,
      offerPhone,
      verificationVisible,
      validateRule,
      validate,
      formattedCountdown,
      validatePhone,
      requestVerification,
      confirmVerification,
      onConfirmClick
    }
  }
})
</script>

<style scoped>
.find-password {
  display: flex;
  width: inherit;
  flex-direction: column;
  align-items: center;
}

.find-password-content-box {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin: 20px 0px;
  font-size: 1.5rem;
  gap: 5px;
}

input {
  border-radius: 8px;

  width: 660px;
  height: 60px;
  font-size: 1.5rem;
  padding-left: 10px;
}

.find-password-bt {
  flex-direction: column;
  gap: 40px;
}

#check {
  display: flex;
  width: 660px;
  height: 60px;
  justify-content: center;
  align-items: center;

  border: none;
  border-radius: 40px;
  color: white;
  background-color: #114cb1;
  font-weight: 600;
  font-size: 1.5rem;
  letter-spacing: 7px;
}

#check:hover {
  cursor: pointer;
}

.verification,
.phone-verification {
  text-align: start;
}

#phoneNum {
  width: 530px;
  margin-right: 15px;
}

#verify-request {
  width: 115px;
  height: 60px;

  border: none;
  border-radius: 10px;
  background: #aaa9a9;
}

.p-msg {
  color: red;
  /* margin-top: 5px; */
}
.p-msg.success {
  color: blue;
}
</style>
