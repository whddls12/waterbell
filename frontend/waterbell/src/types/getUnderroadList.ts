import { computed } from 'vue'
import store from '@/store/index'
import axios from '@/types/apiClient'
import http from '@/types/http'
export async function setAxios() {
  const apiClient = await axios.apiClient(store)
  const api = axios.api
  return { apiClient, api }
}

export async function fetchUnderroads() {
  // try {
  //로그인된 유저의 자격이 지하차도 매니저이면 지하차도 목록을 list로 생성할 것.
  //지하차도 리스트가 새로고침돼도 개수 유지되도록 리셋
  const apiClient = (await setAxios()).apiClient
  const api = (await setAxios()).api
  await store.commit('auth/resetList')
  const isLogin = computed(() => store.getters['auth/isLogin']) //로그인 여부
  const role = computed(() => store.getters['auth/role']) //유저의 역할
  //pmanager일 때
  if (isLogin.value && role.value == 'PUBLIC_MANAGER') {
    let member = null as any //멤버 if문 내 전역 잡기
    await apiClient //현재 로그인된 토큰으로 멤버 정보 가져오기
      .get('/member/findMember/token')
      .then((res) => {
        // console.log(res.data.member)의 결과
        //{
        //     "id": 7,
        //     "loginId": "pManager1",
        //     "role": "PUBLIC_MANAGER",
        //     "phone": "01088742165",
        //     "accessToken": "",
        //     "refreshToken": null,
        //     "sidoId": 7,
        //     "facilityId": []
        // }
        member = res.data.member
      })
      .then(() => {
        http.get('/facilities/roads').then((res: { data: any }) => {
          res.data.forEach(async (element: any) => {
            // console.log(element)
            //결과
            //   {
            //     "gugunName": "대덕구",
            //     "sido": {
            //         "sidoName": "대전",
            //         "id": 3
            //     },
            //     "gugunId": 3,
            //     "underroads": [
            //         {
            //             "id": 10,
            //             "gugun": {
            //                 "id": 3,
            //                 "gugunName": "대덕구",
            //                 "sido": {
            //                     "sidoName": "대전",
            //                     "id": 3
            //                 }
            //             },
            //             "firstFloodMessage": "침수경고 현재수위:15mm 이상",
            //             "activation_message": "진입금지",
            //             "deactivation_message": "진입금지 해제",
            //             "firstAlarmValue": 15,
            //             "secondAlarmValue": 30,
            //             "hubIp": "172.20.10.8",
            //             "status": "SECOND",
            //             "undergroundRoadName": "대전IC지하차도",
            //             "latitude": 36.3599119,
            //             "longitude": 127.4480964,
            //             "apart": false
            //         }
            //     ]
            // }
            //element의 sido.id와 member의 sidoId가 일치하는 경우만
            // console.log(element.sido.id)
            // console.log(member.sidoId)
            if (element.sido.id == member.sidoId) {
              // console.log('여기 들어오냐')
              store.commit('auth/setUnderroadbygugun', element)
              for (const underroad of element.underroads) {
                // console.log(underroad)
                store.commit('auth/setUnderroadList', underroad) //지하차도 세팅하는거 로그인 때도 넣기
              }
            }
          })
        })
      })
    //아니라면(비회원) 지하차도 전부 리스트로 받아오기(시군구별 + 전체)
  } else {
    await http.get('/facilities/roads').then((res: { data: any }) => {
      res.data.forEach((element: any) => {
        if (element.underroads.length != 0) {
          store.commit('auth/setUnderroadbygugun', element)
          for (const e of element.underroads) {
            store.commit('auth/setUnderroadList', e)
            // console.log(e)
          }
        }
      })
    })
  }
}
