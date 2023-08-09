<template lang="">
  <div>
    <div>
      <h3>회원가입</h3>
      <form>
        <div>
          <label for="name">이름</label><br />
          <input type="text" label="name" id="name" v-model="name" />
          <p :class="{ 'p-msg': true, success: validate.name }">
            {{ nameMsg }}
          </p>
        </div>
        <div>
          <label for="id">아이디</label><br />
          <input type="text" label="id" id="id" v-model="id" />
          <button @click.prevent="checkId">중복확인</button>
          <p :class="{ 'p-msg': true, success: validate.isDuplicated }">
            {{ idMsg }}
          </p>
        </div>
        <div>
          <label for="password">비밀번호</label><br />
          <input
            type="password"
            label="password"
            id="password"
            v-model="password"
          />
          <p :class="{ 'p-msg': true, success: validate.password }">
            {{ passMsg }}
          </p>
        </div>

        <div>
          <label for="confirmPass">비밀번호 확인</label><br />
          <input
            type="password"
            label="confirmPass"
            id="confirmPass"
            v-model="confirmPass"
          />
          <p :class="{ 'p-msg': true, success: validate.confirmPass }">
            {{ confirmPassMsg }}
          </p>
        </div>
        <div>
          <label for="phoneNum">휴대폰번호</label><br />
          <input
            type="text"
            label="phoneNum"
            id="phoneNum"
            v-model="phoneNum"
          />
          <button @click.prevent="requestVerification">인증번호 요청</button>
          <p :class="{ 'p-msg': true, success: validate.phoneVerification }">
            {{ phoneMsg }}
          </p>
          <label for="verification" v-if="verificationVisible"
            >인증번호 입력</label
          ><br />
          <div v-if="verificationVisible">
            <input
              type="text"
              label="verification"
              placeholder="인증번호를 입력해주세요."
              id="verification"
              v-model="verification"
            />
            <button @click.prevent="onConfirmClick">인증번호 확인</button>
            <p>{{ formattedCountdown }}</p>
          </div>
        </div>
        <div>
          <label for="apartCode">아파트 코드</label><br />
          <input
            type="text"
            label="apartCode"
            id="apartCode"
            v-model="apartCode"
          />
          <button @click.prevent="findAddressByCode">주소 찾기</button>
          <p :class="{ 'p-msg': true, success: validate.name }">
            {{ codeMsg }}
          </p>
        </div>

        <div>
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

        <div>
          <label for="detailAddress">아파트 호수</label><br />
          <input
            type="text"
            label="detailAddress"
            id="detailAddress"
            v-model="detailAddress"
          />
        </div>
        <button @click.prevent="join">회원가입</button>
        <button>취소</button>
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
  margin-top: 5px;
}
.p-msg.success {
  color: blue;
}
</style>

function async() { throw new Error('Function not implemented.') }
