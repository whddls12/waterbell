<template>
  <div>
    <div class="big-box">
      <h3>추가 정보 입력</h3>
      <div class="formBox">
        <form>
          <div class="labelBox">
            <label for="phoneNum">휴대폰번호</label><br />
            <div class="btnInput">
              <input
                type="text"
                label="phoneNum"
                id="phoneNum"
                v-model="phoneNum"
              />
              <button class="text" @click.prevent="requestVerification">
                인증 요청
              </button>
            </div>
            <p
              :class="{
                'p-msg': true,
                success: validate.phoneNum && offerPhone
              }"
            >
              {{ phoneMsg }}
            </p>
          </div>
          <div class="labelBox">
            <label for="verification" v-if="verificationVisible"
              >인증번호 입력</label
            ><br />
            <div class="btnInput" v-if="verificationVisible">
              <input
                type="text"
                label="verification"
                placeholder="인증번호를 입력해주세요."
                id="verification"
                v-model="verification"
              />
              <button class="text" @click.prevent="onConfirmClick">
                인증번호 확인
              </button>
              <p>{{ formattedCountdown }}</p>
            </div>
          </div>
          <div class="labelBox">
            <label for="apartCode">아파트 코드</label><br />
            <div class="btnInput">
              <input
                type="text"
                label="apartCode"
                id="apartCode"
                v-model="apartCode"
              />
              <button class="text" @click.prevent="findAddressByCode">
                주소 찾기
              </button>
              <p :class="{ 'p-msg': true }">
                {{ codeMsg }}
              </p>
            </div>
          </div>

          <div class="labelBox">
            <label for="address">아파트 주소</label><br />
            <input
              type="text"
              required
              label="address"
              id="address"
              v-model="address"
              readonly
            />
          </div>

          <div class="labelBox">
            <label for="detailAddress">아파트 호수</label><br />
            <input
              type="text"
              label="detailAddress"
              id="detailAddress"
              v-model="detailAddress"
            />
          </div>
        </form>
        <div class="btnBox">
          <button
            class="main-primary joinBtn joinBtn-text"
            @click.prevent="join"
          >
            회원가입
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent, ref, watch } from 'vue'
import apiModules from '@/types/apiClient'
// import store from '@/store/index'
import { useRoute, useRouter } from 'vue-router'
export default defineComponent({
  name: 'socialLoginExtraInfo',

  setup() {
    //api 통신을 위한 axios
    const api = apiModules.api
    const route = useRoute()
    const router = useRouter()

    const confirmPass = ref('')
    const phoneNum = ref('')
    const verification = ref('')
    const apartCode = ref('')
    const address = ref('')
    const detailAddress = ref('')

    //입력창 아래 띄워질 메시지
    const phoneMsg = ref('')
    const codeMsg = ref('')
    //휴대폰번호 인증 확인 여부
    const offerPhone = ref(false)

    // 인증번호 요청 및 확인 관련 변수
    const verificationVisible = ref(false)
    const countdown = ref(180)

    const validateRule = {
      verification: /^[0-9]{6}$/,
      phone: /^010[0-9]{8}$/,
      detailAddress: /^[0-9]{1,6}$/
    }

    const validate = ref({
      phoneNum: false, //양식이 맞는가
      phoneVerification: false, //인증 했는가
      apartCodeCheck: false,
      detailAddress: false
    })
    //각 입력된 정보에 대한 유효성 검사
    const validatePhone = () => {
      // validate.value.phoneVerification = true
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
    const validateDetailAddress = () => {
      // validate.value.phoneVerification = true
      if (detailAddress.value == '') {
        validate.value.detailAddress = false
        phoneMsg.value = '필수입력 항목입니다.'
      } else if (!validateRule.phone.test(phoneNum.value)) {
        validate.value.detailAddress = false
        phoneMsg.value = '거주 중이신 세대의 호수를 숫자로 입력해주세요.'
      } else {
        validate.value.detailAddress = true
        phoneMsg.value = ''
      }
    }

    //인증번호 요청 이후 인증번호 입력 input 보이기
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
          .post('/verification/code', null, {
            params: { phoneNumber: phoneNumber }
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

    //아파트 코드로 주소 조회
    const findAddressByCode = () => {
      try {
        api
          .post('/member/join/validationApartCode', {
            apartCode: apartCode.value
          })
          .then((res) => {
            if (res.data.message == 'success') {
              address.value = res.data.address
              validate.value.apartCodeCheck = true
              codeMsg.value = '' // Clear any error messages
            } else {
              codeMsg.value = '아파트 코드가 유효하지 않습니다.'
            }
            // console.log(apartCode.value)
            // console.log(res)
            // console.log(res.data)
          })
      } catch (error) {
        codeMsg.value = '아파트 코드가 유효하지 않습니다.'
      }
    }

    //회원가입 클릭
    const join = () => {
      try {
        const key = route.query.key
        console.log(key)
        if (Object.values(validate.value).every((v) => v === true)) {
          api
            .post(`/social/join/${key}`, {
              phone: phoneNum.value,
              apartCode: apartCode.value,
              addressNumber: detailAddress.value
            })
            .then((res) => {
              if ((res.data.message = 'success')) {
                alert('회원가입이 완료되었습니다.')

                //--------------------------------------------------------------------------------------
                //code 다시 끌어와서 로그인 시킬 것.
                //   store.dispatch('auth/socialLogin', {

                //   })
              }
            })
        }
      } catch (error) {
        alert('회원가입에 실패했습니다.')
      }
    }

    // 휴대폰번호 검증을 위한 watch 함수
    watch(phoneNum, (newValue, oldValue) => {
      if (newValue != oldValue) {
        validatePhone()
        console.log(phoneMsg.value)
      }
    })

    //호수 입력 여부 확인을 위한 watch 함수
    watch(detailAddress, (newValue, oldValue) => {
      if (newValue != oldValue) {
        validateDetailAddress()
      }
    })
    return {
      phoneNum,
      validate,
      address,
      verificationVisible,
      phoneMsg,
      codeMsg,
      detailAddress,
      confirmPass,
      apartCode,
      verification,
      countdown,
      offerPhone,
      validatePhone,
      validateDetailAddress,
      confirmVerification,
      findAddressByCode,
      requestVerification,
      onConfirmClick,
      join
    }
  }
})
</script>
<style scoped lang="css">
h3 {
  color: #000;
  /* 회원가입상자_제목 */
  font-family: Roboto;
  font-size: 30px;
  font-style: normal;
  font-weight: 500;
  line-height: 16px; /* 53.333% */
  letter-spacing: 3px;
}

.big-box {
  display: flex;
  padding: 80px 0px;
  flex-direction: column;
  align-items: center;
  gap: 50px;
}
.p-msg {
  color: red;
  margin-top: 5px;
}
.p-msg.success {
  color: blue;
}
.formBox {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  align-self: stretch;
}

label,
.text {
  color: #666;
  /* Body2 */
  font-family: Roboto;
  font-size: 20px;
  font-style: normal;
  font-weight: 300;
  line-height: 20px; /* 100% */
  letter-spacing: 0.25px;
}
button {
  display: flex;
  padding: 11px 16px;
  width: 120px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  justify-self: stretch;
  align-self: stretch;
  border-radius: 10px;
  border: 0;
  background: #aaa9a9;
}
.labelBox {
  display: flex;
  width: 665px;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 10px;
  /* border: 1px solid red; */
}
.btnInput {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-self: stretch;
  /* border: 1px solid blue; */
}
.btnInput input {
  margin-right: 10px; /* 오른쪽 마진을 추가하여 button과의 간격을 조절합니다. */
}

input {
  display: flex;
  height: 56px;
  padding: 16px 17px;
  justify-content: flex-end;
  align-items: center;
  align-self: stretch;
  gap: 10px;
  flex: 1 0 0;
  border-radius: 12px;
  border: 1px solid rgba(102, 102, 102, 0.35);
}

.joinBtn {
  display: flex;
  width: 644px;
  height: 60px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
}

.main-primary {
  border-radius: 40px;
  background: var(--2, #114cb1);
}

.joinBtn-text {
  color: #fff;
  text-align: center;
  /* 로그인_btn */
  font-family: Roboto;
  font-size: 24px;
  font-style: normal;
  font-weight: 600;
  line-height: 32px; /* 133.333% */
  letter-spacing: 7px;
}
</style>
