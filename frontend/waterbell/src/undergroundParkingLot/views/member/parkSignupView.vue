<template lang="">
  <div>
    <div>
      <h3 style="margin: 20px">회원가입</h3>
      <!-- <div class="report-empty-box"></div> -->

      <form>
        <div class="title-box-box clearfix">
          <label class="title-name-label" for="name">이름</label><br />
          <input
            class="title-input-box"
            type="text"
            label="name"
            id="name"
            v-model="name"
          />
          <p
            :class="{ 'p-msg': true, success: validate.name }"
            v-show="nameMsg.value != ''"
          >
            {{ nameMsg }}
          </p>
        </div>
        <div class="title-box-box">
          <label class="title-name-label" for="id">아이디</label><br />
          <div class="btn-and-input">
            <input
              class="title-input-box-btn"
              type="text"
              label="id"
              id="id"
              v-model="id"
            />
            <button class="check-btn" @click.prevent="checkId">중복확인</button>
          </div>
          <p :class="{ 'p-msg': true, success: validate.isDuplicated }">
            {{ idMsg }}
          </p>
        </div>
        <div class="title-box-box">
          <label class="title-name-label" for="password">비밀번호</label><br />
          <input
            class="title-input-box"
            type="password"
            label="password"
            id="password"
            v-model="password"
          />
          <p :class="{ 'p-msg': true, success: validate.password }">
            {{ passMsg }}
          </p>
        </div>

        <div class="title-box-box">
          <label class="title-name-label" for="confirmPass">비밀번호 확인</label
          ><br />
          <input
            class="title-input-box"
            type="password"
            label="confirmPass"
            id="confirmPass"
            v-model="confirmPass"
          />
          <p :class="{ 'p-msg': true, success: validate.confirmPass }">
            {{ confirmPassMsg }}
          </p>
        </div>
        <div class="title-box-box">
          <label class="title-name-label" for="phoneNum">휴대폰번호</label
          ><br />
          <div class="btn-and-input">
            <input
              class="title-input-box-btn"
              type="text"
              label="phoneNum"
              id="phoneNum"
              v-model="phoneNum"
            />
            <button class="check-btn" @click.prevent="requestVerification">
              인증번호 요청
            </button>
          </div>
          <p :class="{ 'p-msg': true, success: validate.phoneVerification }">
            {{ phoneMsg }}
          </p>
          <label for="verification" v-if="verificationVisible"
            >인증번호 입력</label
          ><br />
          <div
            v-if="verificationVisible"
            class="btn-and-input"
            style="margin-bottom: 20px"
          >
            <!-- <div v-if="true" class="btn-and-input"> -->
            <input
              class="title-input-box-btn"
              type="text"
              label="verification"
              placeholder="인증번호를 입력해주세요."
              id="verification"
              style="width: 320px"
              v-model="verification"
            />
            <p class="time-text">{{ formattedCountdown }}</p>
            <button class="check-btn" @click.prevent="onConfirmClick">
              인증번호<br />
              확인
            </button>
            <!-- <p class="time-text">{{ formattedCountdown }}</p> -->
          </div>
        </div>
        <!-- <div class="title-box-box" style="margin-top: 20px"> -->
        <div class="title-box-box">
          <label class="title-name-label" for="apartCode">아파트 코드</label
          ><br />
          <div class="btn-and-input">
            <input
              class="title-input-box-btn"
              type="text"
              label="apartCode"
              id="apartCode"
              v-model="apartCode"
            />
            <button class="check-btn" @click.prevent="findAddressByCode">
              주소 찾기
            </button>
          </div>
          <p :class="{ 'p-msg': true, success: validate.name }">
            {{ codeMsg }}
          </p>
        </div>

        <div class="title-box-box">
          <label class="title-name-label" for="address">아파트 주소</label
          ><br />
          <input
            class="title-input-box"
            type="text"
            required
            label="address"
            id="address"
            v-model="address"
            readonly
          />
        </div>

        <div class="title-box-box" style="margin-top: 20px">
          <label class="title-name-label" for="detailAddress">아파트 호수</label
          ><br />
          <input
            class="title-input-box"
            type="text"
            label="detailAddress"
            id="detailAddress"
            v-model="detailAddress"
          />
        </div>
        <div class="report-empty-box-bottom"></div>

        <div class="sign-btn">
          <button class="ok-btn" @click.prevent="join">회원가입</button>
          <button class="cancel-btn">취소</button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts">
import { computed, defineComponent, ref, watch } from 'vue'
import apiModules from '@/types/apiClient'
import store from '@/store/index'
export default defineComponent({
  name: 'parkJoin',

  setup() {
    //api 통신을 위한 axios
    const api = apiModules.api

    //각 input 에 입력된 값
    const name = ref('')
    const id = ref('')
    const password = ref('')
    const confirmPass = ref('')
    const phoneNum = ref('')
    const verification = ref('')
    const apartCode = ref('')
    const address = ref('')
    const detailAddress = ref('')

    //입력창 아래 띄워질 메시지
    const nameMsg = ref('')
    const idMsg = ref('')
    const passMsg = ref('')
    const confirmPassMsg = ref('')
    const phoneMsg = ref('')

    //휴대폰번호 인증 확인 여부
    const offerPhone = ref(false)

    // 인증번호 요청 및 확인 관련 변수
    const verificationVisible = ref(false)
    const countdown = ref(180)

    const validateRule = {
      name: /^[가-힣]{3,5}$/,
      id: /^[a-z0-9]{4,10}$/,
      password:
        /^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{6,20}$/,
      phone: /^010[0-9]{8}$/,
      verification: /^[0-9]{6}$/,
      detailAddress: /^[0-9]{1,6}$/
    }

    const validate = ref({
      name: false,
      id: false, //양식 맞는가
      isDuplicated: false,
      password: false, //양식 맞는가
      confirmPass: false, //양식 맞고 비밀번호와 일치하는가
      phoneNum: false, //양식이 맞는가
      phoneVerification: false,
      apartCodeCheck: false,
      detailAddress: false
    })
    //각 입력된 정보에 대한 유효성 검사
    const validatedName = () => {
      if (name.value == '') {
        validate.value.name = false
        nameMsg.value = '필수입력 항목 입니다.'
      } else if (!validateRule.name.test(name.value)) {
        validate.value.name = false
        nameMsg.value = '한글 이름을 입력하세요.'
      } else {
        validate.value.name = true
        nameMsg.value = ''
      }
    }

    const validateId = () => {
      validate.value.isDuplicated = false
      if (id.value == '') {
        validate.value.id = false
        validate.value.isDuplicated = false
        idMsg.value = '필수입력 항목 입니다.'
      } else if (!validateRule.id.test(id.value)) {
        validate.value.isDuplicated = false
        validate.value.id = false
        idMsg.value = '아이디는 영어 소문자+숫자로 4~10자 이내만 가능합니다.'
      } else if (!validate.value.isDuplicated) {
        validate.value.id = false
        idMsg.value = '아이디 중복확인을 진행하세요.'
      }
      // } else {
      //   validate.value.id = true
      //   idMsg.value = '사용가능한 아이디입니다.'
      // }
    }

    const validatePass = () => {
      validate.value.confirmPass = false
      if (!password.value || password.value == '') {
        passMsg.value = '필수입력 항목 입니다.'
        validate.value.password = false
      } else if (!validateRule.password.test(password.value)) {
        passMsg.value =
          '비밀번호는 영문자, 숫자, 특수기호를 포함하여 6~20자 이내만 가능합니다.'
        validate.value.password = false
      } else {
        passMsg.value = '사용가능한 비밀번호입니다.'
        validate.value.password = true
      }
      validateConfirmPass()
    }

    const validateConfirmPass = () => {
      if (confirmPass.value == '') {
        validate.value.confirmPass = false
        confirmPassMsg.value = '비밀번호 확인을 입력하세요.'
      } else if (confirmPass.value != password.value) {
        validate.value.confirmPass = false
        confirmPassMsg.value = '비밀번호 확인이 일치하지 않습니다.'
      } else if (confirmPass.value == password.value) {
        validate.value.confirmPass = true
        confirmPassMsg.value = '비밀번호 확인이 일치합니다.'
      }
    }

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

    //id 중복검사
    const checkId = async () => {
      try {
        await api.get(`/member/join/duplication/${id.value}`).then((res) => {
          if (res.data == 'success') {
            validate.value.isDuplicated = true
            validate.value.id = true
            idMsg.value = '사용가능한 아이디입니다.'
            // console.log(validate.value.isDuplicated)
          } else {
            validate.value.isDuplicated = false
            idMsg.value = '사용 불가한 아이디입니다.'
          }
        })
      } catch (error) {
        validate.value.isDuplicated = false
        idMsg.value = '사용 불가한 아이디입니다.'
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
            }
          })
      } catch (error) {
        validate.value.phoneVerification = false

        console.log(error)
        phoneMsg.value = '인증번호가 틀렸습니다. 시간 내에 다시 입력하세요.'
      }
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

    //인증번호 확인 클릭
    const onConfirmClick = () => {
      confirmVerification()
    }

    //회원가입 클릭
    const join = () => {
      try {
        console.log(validate.value)
        if (Object.values(validate.value).every((v) => v === true)) {
          api
            .post('/member/join', {
              name: name.value,
              loginId: id.value,
              password: password.value,
              phone: phoneNum.value,
              apartCode: apartCode.value,
              addressNumber: detailAddress.value
            })
            .then((res) => {
              if ((res.data.message = 'success')) {
                alert('회원가입이 완료되었습니다.')
                store.dispatch('auth/memberLogin', {
                  loginId: id.value,
                  password: password.value
                })
              }
            })
        }
      } catch (error) {
        alert('회원가입에 실패했습니다.')
      }
    }
    // 이름 검증을 위한 watch 함수
    watch(name, (newValue, oldValue) => {
      if (newValue != oldValue) {
        validatedName()
      }
    })

    watch(id, (newValue, oldValue) => {
      if (newValue != oldValue) {
        validateId()
      }
    })

    // 비밀번호 검증을 위한 watch 함수
    watch(password, (newValue, oldValue) => {
      if (newValue != oldValue) {
        validatePass()
      }
    })

    // 비밀번호 확인 검증을 위한 watch 함수
    watch(confirmPass, (newValue, oldValue) => {
      if (newValue != oldValue) {
        validateConfirmPass()
      }
    })

    // 휴대폰번호 검증을 위한 watch 함수
    watch(phoneNum, (newValue, oldValue) => {
      if (newValue != oldValue) {
        validatePhone()
      }
    })

    //호수 입력 여부 확인을 위한 watch 함수
    watch(detailAddress, (newValue, oldValue) => {
      if (newValue != oldValue) {
        validateDetailAddress()
      }
    })
    return {
      name,
      id,
      phoneNum,
      validate,
      address,
      nameMsg,
      phoneMsg,
      verificationVisible,
      password,
      detailAddress,
      confirmPass,
      apartCode,
      verification,
      countdown,
      offerPhone,
      idMsg,
      passMsg,
      confirmPassMsg,
      validatedName,
      validateId,
      validatePass,
      validateConfirmPass,
      validatePhone,
      validateDetailAddress,
      checkId,
      requestVerification,
      postVerification,
      formattedCountdown,
      confirmVerification,
      findAddressByCode,
      onConfirmClick,
      join
    }
  }
})
</script>
<style scoped lang="css">
.p-msg {
  color: red;
  /* margin-top: 5px; */
}
.p-msg.success {
  color: blue;
}

.title-box-box {
  display: flex;
  flex: 1;
  /* flex-grow: 0; */
  flex-direction: column;
  align-items: flex-start;
  gap: 0;
  margin: 0;
  width: 500px;
}
.title-name-label {
  color: #666;
  /* Body2 */
  font-size: 20px;
  font-style: normal;
  font-weight: 300;
  line-height: 20px;
  letter-spacing: 0.25px;
  margin: 0;
}

.title-input-box {
  height: 40px;
  align-self: stretch;
  border-radius: 12px;
  border: 1px solid rgba(102, 102, 102, 0.3);
  margin: 0;
}

.title-box p {
  margin-top: 5px; /* 메시지 텍스트와의 상단 간격을 조절 */
}

.title-input-box-btn {
  height: 40px;
  align-self: stretch;
  border-radius: 12px;
  border: 1px solid rgba(102, 102, 102, 0.3);
  margin: 0;
  width: 380px;
}

.btn-and-input {
  display: flex;
  align-items: flex-start;
  /* gap: 15px; */
  align-self: stretch;
}

.clearfix::ater {
  content: '';

  display: block;

  clear: both;
}

.check-btn {
  display: flex;
  width: 114.001px;
  /* padding: 11px 16px; */
  justify-content: center;
  align-items: center;
  gap: 8px;
  align-self: stretch;
  border-radius: 10px;
  background: #aaa9a9;
  height: 40px;
  margin-left: 15px;
  color: var(--unnamed, #424242);
  text-align: center;
  /* Body2 */
  /* font-family: Roboto; */
  font-size: 15px;
  font-style: normal;
  font-weight: 300;
  line-height: 20px; /* 100% */
  letter-spacing: 0.25px;
  border: 0;
}

.time-text {
  margin-left: 10px;
}

.report-empty-box {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  height: 55px;
}
.report-empty-box-bottom {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  height: 40px;
}

/* 하단 버튼 */
.sign-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 20px;
  /* 버튼 사이 간격 */
  align-self: stretch;
  margin-top: 10px;
}

.cancel-btn {
  display: flex;
  width: 90px;
  height: 32px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 8px;
  background: var(--unnamed, #939393);
  color: #fff;
  text-align: center;
  text-transform: uppercase;
  width: 114.001px;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
  height: 40px;
  margin-left: 15px;
  text-align: center;
  /* Body2 */
  /* font-family: Roboto; */
  font-size: 15px;
  font-style: normal;
  font-weight: 300;
  line-height: 20px; /* 100% */
  letter-spacing: 0.25px;
  border: 0;
}

.ok-btn {
  display: flex;
  width: 90px;
  height: 32px;
  padding: 11px 16px;
  justify-content: center;
  align-items: center;
  gap: 8px;
  border-radius: 8px;
  background: var(--1, #10316b);
  color: #fff;
  text-align: center;
  /* Button */
  /* font-family: Roboto; */
  font-size: 14px;
  font-style: normal;
  font-weight: 400;
  line-height: 15px; /* 142.857% */
  letter-spacing: 1.25px;
  text-transform: uppercase;
  width: 114.001px;
  justify-content: center;
  align-items: center;
  border-radius: 10px;
  height: 40px;
  margin-left: 15px;
  text-align: center;
  /* Body2 */
  /* font-family: Roboto; */
  font-size: 15px;
  font-style: normal;
  font-weight: 300;
  line-height: 20px; /* 100% */
  letter-spacing: 0.25px;
  border: 0;
}
</style>

function async() { throw new Error('Function not implemented.') }
