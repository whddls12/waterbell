<template lang="">
  <div>
    <div>
      <h3>회원정보 입력</h3>
      <form @submit.prevent="submitForm" ref="signupFormRef">
        <!-- 이름 입력란 -->
        <div>
          <label for="name">이름</label>
          <input
            placeholder="이름을 입력하세요."
            type="text"
            id="name"
            :value="name"
          />
        </div>
        <!-- 아이디 입력란 -->
        <div>
          <label for="id">아이디</label>
          <input type="text" id="id" :value="id" />
          <button>중복확인</button>
          <div>{{ idValidation }}</div>
        </div>
        <!-- 비밀번호 입력란 -->
        <div>
          <label for="password">비밀번호</label>
          <input type="password" id="password" :value="password" />
          <div>{{ passwordValidation }}</div>
        </div>
        <!-- 비밀번호 확인 입력란 -->
        <div>
          <label for="conformPass">비밀번호 확인</label>
          <input type="password" id="confirmPass" :value="confirmPass" />
          <span :value="isOkayPassword">{{ conformPassValidation }}</span>
        </div>
        <!-- 휴대폰 번호 입력란 -->
        <div>
          <label for="phonenumber">휴대폰 번호</label>
          <input type="text" id="phonenumber" :value="phonenumber" />
          <button @click="checkPhone">인증하기</button>
          <div>{{ phoneValidation }}</div>
        </div>
        <button v-if="isFinish" type="submit">회원가입</button>
      </form>

      <!-- <form
        @submit.prevent="submitForm"
        :model="signupForm"
        :rules="rules"
        ref="signupFormRef"
      >
        <div>
          <label for="name">이름</label>
          <input "이름을 입력하세요." type="text" id="name" v-model="name" />
          <div v-model></div>
        </div>
        <div>
          <label for="id">아이디</label>
          <input type="text" id="id" v-model="id" />
          <button>중복확인</button>
        </div>
        <div>
          <label for="password">비밀번호</label>
          <input type="password" id="password" v-model="password" />
        </div>
        <div>
          <label for="conformPass">비밀번호 확인</label>
          <input type="password" id="confirmPass" v-model="confirmPass" />
          <div v-model="isOkaypassword">
            비밀번호 확인이 일치하는지 아닌지 텍스트
          </div>
        </div>
        <div>
          <label for="phonenumber">휴대폰 번호</label>
          <input type="text" id="phonenumber" v-model="phonenumber" />
          <button @click="checkPhone" v-model="checkPhone">인증하기</button>
          <div v-model="isOkayPhonenum">
            휴대폰 번호 유효성 검사 결과가 들어갈 자리
          </div>
        </div>

        <button v-if="isFinish" type="submit">회원가입</button>
      </form> -->
    </div>
  </div>
</template>
<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter } from 'vue-router'
export default defineComponent({
  name: 'parkJoin',
  setup() {
    // const router = useRouter()
    const name = ref('')
    const id = ref('')
    const password = ref('')
    const confirmPass = ref('')
    const phonenumber = ref('')

    const idValidation = ref('')
    const passwordValidation = ref('')
    const conformPassValidation = ref('')
    const phoneValidation = ref('')

    const isFinish = ref(false)

    const validateId = () => {
      const pattern = /^[A-Za-z0-9]{4,10}$/
      if (!pattern.test(id.value)) {
        idValidation.value =
          '아이디는 영문+숫자 조합으로 4~10자 이내여야 합니다.'
        return false
      }
      idValidation.value = '검사결과를 만족했습니다.'
      return true
    }

    const validatePassword = () => {
      const pattern = /^[A-Za-z0-9]{8,20}$/
      if (!pattern.test(password.value)) {
        passwordValidation.value =
          '비밀번호는 영문+숫자 조합으로 8~20자 이내여야 합니다.'
        return false
      }
      passwordValidation.value = '검사결과를 만족했습니다.'
      return true
    }

    const validateConformPass = () => {
      if (confirmPass.value !== password.value || confirmPass.value === '') {
        conformPassValidation.value = '비밀번호와 일치해야 합니다.'
        return false
      }
      conformPassValidation.value = '검사결과를 만족했습니다.'
      return true
    }

    const validatePhoneNumber = () => {
      const pattern = /^010\d{8}$/
      if (!pattern.test(phonenumber.value)) {
        phoneValidation.value =
          '휴대폰 번호는 010으로 시작해야 하며 11자리여야 합니다.'
        return false
      }
      phoneValidation.value = '검사결과를 만족했습니다.'
      return true
    }

    const checkPhone = () => {
      validatePhoneNumber()
    }

    const submitForm = () => {
      if (
        validateId() &&
        validatePassword() &&
        validateConformPass() &&
        validatePhoneNumber()
      ) {
        isFinish.value = true
        // 여기에서 회원가입 로직을 추가하실 수 있습니다.
      }
    }

    return {
      name,
      id,
      password,
      confirmPass,
      phonenumber,
      isFinish,
      idValidation,
      passwordValidation,
      conformPassValidation,
      phoneValidation,
      checkPhone,
      submitForm
    }

    // const signupFormRef = ref<FormInstance>()

    // const signupForm = reactive({
    //   name: '',
    //   password: '',
    //   confirmPass: '',
    //   phone: '',
    //   apartCode: '',
    //   addressNumber: ''
    // })

    // const rules = reactive<FormRule>
    // const responseToken = ref('')
    // const checkName = () => {}

    // const idRules = []

    // const checkId = () => {
    //   const checkId = /^[A-Za-z0-9]{4,12}$/
    // }

    // // validation for password
    // const checkPassword = (
    //   rule: any,
    //   value: string,
    //   callback: (arg?: Error) => void
    // ) => {
    //   let passwordReg = /^(?=.*[0-9])(?=.*[a-zA-Z]).*([a-zA-Z0-9]+?)?$/gi
    //   if (!value) {
    //     callback(new Error("Password can't be empty"))
    //   } else if (value.length < 8) {
    //     return callback(new Error('Minimum length is 8 characters'))
    //   } else if (!passwordReg.test(value)) {
    //     callback(new Error('Required at least 1 digit and 1 letter'))
    //   } else {
    //     callback()
    //   }
    // }
    // // validation for confirm password
    // const checkPassConfirm = (
    //   rule: any,
    //   value: string,
    //   callback: (arg?: Error) => void
    // ) => {
    //   if (!value) {
    //     callback(new Error('Re-enter the password'))
    //   } else if (value !== signupForm.password) {
    //     callback(new Error("Passwords don't match!"))
    //   } else {
    //     callback()
    //   }
    // }
  }
})
</script>
<style lang=""></style>
