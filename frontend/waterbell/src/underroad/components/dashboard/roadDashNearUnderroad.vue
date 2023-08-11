<template>
  <div class="container" id="outline bigbox">
    <div id="inner-box">
      <button>이 동하기</button>
      <div id="map"></div>
    </div>
  </div>
</template>

<script lang="ts">
// declare let kakao: any
declare global {
  interface Window {
    kakao: any
  }
}

import { computed, defineComponent, onMounted, ref } from 'vue'
import roadImgSrc0 from '@/assets/images/map_road_0.png'
import roadImgSrc1 from '@/assets/images/map_road_1.png'
import roadImgSrc2 from '@/assets/images/map_road_2.png'
import riverN from '@/assets/images/map_water_0.png'
import riverY from '@/assets/images/map_water_1.png'
import store from '@/store/index'
import http from '@/types/http'
import { useRoute, useRouter } from 'vue-router'
import { changeFacility } from '@/types/changeFacility'

import Geolocation from 'vue-geolocation-api'

export default defineComponent({
  name: 'roadDashMapVue',
  setup() {
    // const store = useStore()
    const router = useRouter()
    const route = useRoute()
    //지도에 찍을 지하차도 list
    const underroadList = computed(() => store.getters['auth/underroadList'])

    const nowUnderroad = computed(() => store.getters['auth/nowUnderroad'])

    //임시 선택된 위치가 저장된 state
    const tmpUnderroad = computed(() => store.getters.tmpUnderroad).value

    //임시 선택 위치에 원하는 값을 넣는 store의 mutation(click 이벤트 발생 시, 실행할 함수)
    const moveFacility = (id: any) => {
      // const nowRouter = route.
      changeFacility(id)
      // window.location.reload
      router.go(0)
    }

    //마커 위치가 담긴 배열들
    let position_ok = ref<any[]>([]) //underroad 형식에 statusMsg 추가된 형태의 객체
    let position_warn = ref<any[]>([]) //underroad 형식에 statusMsg 추가된 형태의 객체
    let position_block = ref<any[]>([]) //underroad 형식에 statusMsg 추가된 형태의 객체
    let position_Y = ref<
      { title: string; latlng: any; address: string; msg: string }[]
    >([])
    let position_N = ref<
      { title: string; latlng: any; address: string; msg: string }[]
    >([])

    // console.log('지하차도 리스트')
    // console.log(underroadList.value)

    // console.log('position ok')
    // console.log(position_ok.value)

    // console.log('position warn')
    // console.log(position_warn.value)

    // console.log('position block')
    // console.log(position_block.value)

    const setPositions = () => {
      for (let road of underroadList.value) {
        let roadobj = ref<{ road: any; statusMsg: string }>({
          road: road,
          statusMsg: ''
        }).value

        if (road.status == 'DEFAULT') {
          //진입가능한 지하차도

          roadobj.statusMsg = '진입 가능'
          position_ok.value.push(roadobj)
        } else if (road.status == 'FIRST') {
          roadobj.statusMsg = '1차 경고'
          position_warn.value.push(roadobj)
        } else if (road.status == 'SECOND') {
          roadobj.statusMsg = '2차 경고'
          position_warn.value.push(roadobj)
        } else {
          roadobj.statusMsg = '진입 금지'
          position_block.value.push(roadobj)
        }
      }

      // console.log('여기는 setPositions')
      // console.log(underroadList.value)
      // console.log(position_ok.value)
      // console.log(position_warn)
      // console.log(position_block)
    }

    //지도 script 추가하기1
    const loadScript = () => {
      const script = document.createElement('script')
      script.type = 'text/javascript'
      script.src =
        '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=374cff0d8903c1ed2bf1e9533bb0feab&libraries=clusterer,drawing,services'
      script.addEventListener('load', () => {
        window.kakao.maps.load(initMap)
      })
      document.head.appendChild(script)
    }

    //지도 생성
    const map = ref<any>(null)

    const initMap = async () => {
      const container = document.getElementById('map')
      const center = new window.kakao.maps.LatLng(
        nowUnderroad.value.latitude,
        nowUnderroad.value.longitude
      )

      console.log('lat')
      console.log(nowUnderroad.value)
      console.log(nowUnderroad.value.latitude)
      console.log(nowUnderroad.value.longitude)
      //내가 보고있는 지하차도의 경도 위도를 가져오자.
      const options = {
        center: center, //카카오 마커 확인용
        // center: new window.kakao.maps.LatLng(36.3549114724545, 127.345907414374),
        level: 8
      }
      map.value = new window.kakao.maps.Map(container, options)
      await getWaterHeight()
      await setPositions()
      await makeMarker()
    }
    //마커 만들기
    const makeMarker = () => {
      let imgSize = new window.kakao.maps.Size(25, 25)

      //하천 경보 발령된 하천 마커생성
      for (let i of position_Y.value) {
        // console.log('하천')
        let markerImage = new window.kakao.maps.MarkerImage(riverY, imgSize)

        let marker = new window.kakao.maps.Marker({
          map: map.value,
          position: i.latlng,
          title: i.title,
          image: markerImage,
          clickable: true
          // content: i.address
        })

        let infowindow = makeInfoWindowRiver(i)
        // 마커에 클릭이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseover', function () {
          // 마커 위에 인포윈도우를 표시합니다
          infowindow = makeInfoWindowRiver(i)
          infowindow.open(map.value, marker)
        })

        // 마커에 마우스아웃 이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseout', function () {
          // 마커에 마우스아웃 이벤트가 발생하면 인포윈도우를 제거합니다
          infowindow.close()
          infowindow = makeInfoWindowRiver(i)
        })
      }

      //하천 경보 미발령 하천 마커 생성
      for (let i of position_N.value) {
        let markerImage = new window.kakao.maps.MarkerImage(riverN, imgSize)

        let marker = new window.kakao.maps.Marker({
          map: map.value,
          position: i.latlng,
          title: i.title,
          image: markerImage,
          clickable: true
          // content: i.address
        })
        let infowindow = makeInfoWindowRiver(i)
        // 마커에 클릭이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseover', function () {
          // 마커 위에 인포윈도우를 표시합니다
          infowindow = makeInfoWindowRiver(i)
          infowindow.open(map.value, marker)
        })

        // 마커에 마우스아웃 이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseout', function () {
          // 마커에 마우스아웃 이벤트가 발생하면 인포윈도우를 제거합니다
          infowindow.close()
          infowindow = makeInfoWindowRiver(i)
        })
      }

      //통과 가능한 지하차도 마커 설정
      for (let i of position_ok.value) {
        // console.log(i.road.undergroundRoadName)
        let markerImage = new window.kakao.maps.MarkerImage(
          roadImgSrc0,
          imgSize
        )
        // console.log('통과가능 지하차도 마커설정')
        // console.log(i)

        let latlng = new window.kakao.maps.LatLng(
          i.road.latitude,
          i.road.longitude
        )
        let marker = new window.kakao.maps.Marker({
          map: map.value,
          position: latlng,
          title: i.road.title,
          image: markerImage,
          clickable: true
        })

        let infowindow = makeInfoWindowRoad(i)
        // 마커에 클릭이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseover', function () {
          // 마커 위에 인포윈도우를 표시합니다
          infowindow = makeInfoWindowRoad(i)
          infowindow.open(map.value, marker)
        })

        // 마커에 마우스아웃 이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseout', function () {
          // 마커에 마우스아웃 이벤트가 발생하면 인포윈도우를 제거합니다
          infowindow.close()
          infowindow = makeInfoWindowRoad(i)
        })

        window.kakao.maps.event.addListener(marker, 'click', function () {
          infowindow = makeInfoWindowRoad(i)
          moveFacility(i.road.id)
          infowindow.open(map.value, marker)
        })
      }
      // console.log(position_warn)
      //경고 받은 지하차도 마커 설정
      for (let i of position_warn.value) {
        let markerImage = new window.kakao.maps.MarkerImage(
          roadImgSrc1,
          imgSize
        )
        // console.log('경고지하차도')
        // console.log(i)

        let marker = new window.kakao.maps.Marker({
          map: map.value,
          position: i.latlng,
          title: i.title,
          image: markerImage,
          clickable: true
        })

        let infowindow = makeInfoWindowRoad(i)
        // 마커에 클릭이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseover', function () {
          // 마커 위에 인포윈도우를 표시합니다
          infowindow = makeInfoWindowRoad(i)
          infowindow.open(map.value, marker)
        })

        // 마커에 마우스아웃 이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseout', function () {
          // 마커에 마우스아웃 이벤트가 발생하면 인포윈도우를 제거합니다
          infowindow.close()
          infowindow = makeInfoWindowRoad(i)
        })

        window.kakao.maps.event.addListener(marker, 'click', function () {
          // if (!infowindow) {
          //   infowindow = makeInfoWindowRoad(i)
          // }
          // infowindow.open(map.value, marker)
          moveFacility(i.road.value.id)
        })
      }
      // console.log('진입금지 지하차도 목록')
      // console.log(position_block.value)

      //진입금지 지하차도 마커 설정
      for (let i of position_block.value) {
        // console.log('진입금지 지하차도')
        // console.log(i.value)
        let markerImage = new window.kakao.maps.MarkerImage(
          roadImgSrc2,
          imgSize
        )

        let marker = new window.kakao.maps.Marker({
          map: map.value,
          position: i.latlng,
          title: i.title,
          image: markerImage,
          clickable: true
        })

        let infowindow = makeInfoWindowRoad(i)
        // 마커에 클릭이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseover', function () {
          // 마커 위에 인포윈도우를 표시합니다
          infowindow = makeInfoWindowRoad(i)
          infowindow.open(map.value, marker)
        })

        // 마커에 마우스아웃 이벤트를 등록합니다
        window.kakao.maps.event.addListener(marker, 'mouseout', function () {
          // 마커에 마우스아웃 이벤트가 발생하면 인포윈도우를 제거합니다
          infowindow.close()
          infowindow = makeInfoWindowRoad(i)
        })

        window.kakao.maps.event.addListener(marker, 'click', function () {
          infowindow = makeInfoWindowRoad(i)
          // setTmplotation(i.road.latlng)
          infowindow.open(map.value, marker)
        })
      }
    }

    const makeInfoWindowRoad = (i: any) => {
      // 마커를 클릭했을 때 마커 위에 표시할 인포윈도우를 생성합니다
      // console.log('i가 뭐가 잘못됐나! ')
      // console.log(i.road.undergroundRoadName)
      let iwContent =
        '<div style="padding:5px; width: 200px;"> <p>' +
        i.road.undergroundRoadName +
        '</p> <p style="font-weight:bold;">' +
        i.statusMsg +
        '</p></div>'
      let iwRemoveable = true // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다
      let iwPosition = new window.kakao.maps.LatLng(
        i.road.latitude,
        i.road.longitude
      )
      // 인포윈도우를 생성합니다
      var infowindow = new window.kakao.maps.InfoWindow({
        content: iwContent,
        removable: iwRemoveable,
        position: iwPosition
      })

      return infowindow
    }

    const makeInfoWindowRiver = (i: any) => {
      // 마커를 클릭했을 때 마커 위에 표시할 인포윈도우를 생성합니다

      let iwContent =
        '<div style="padding:5px; width: 200px;"> <p>' +
        i.title +
        '</p> <p style="font-weight:bold;">' +
        i.msg +
        '</p></div>'
      let iwRemoveable = true // removeable 속성을 ture 로 설정하면 인포윈도우를 닫을 수 있는 x버튼이 표시됩니다
      let iwPosition = new window.kakao.maps.LatLng(i.latitude, i.longitude)
      // 인포윈도우를 생성합니다
      var infowindow = new window.kakao.maps.InfoWindow({
        content: iwContent,
        removable: iwRemoveable,
        position: iwPosition
      })

      return infowindow
    }

    const getWaterHeight = async () => {
      try {
        const res = await http.get('/dash/map/river')
        for (let i of res.data) {
          if (i.isDanger == 'N') {
            let obj = {
              title: i.name,
              latlng: new window.kakao.maps.LatLng(i.lat, i.lon),
              address: i.address,
              msg: '경보 미발령'
            }
            position_N.value.push(obj)
          } else {
            let obj = {
              title: i.name,
              latlng: new window.kakao.maps.LatLng(i.lat, i.lon),
              address: i.addressj,
              msg: '경보 발령'
            }
            position_Y.value.push(obj)
          }
        }
      } catch (error) {
        console.error('Failed to fetch water height data:', error)
      }
    } //getWaterHeight 함수 닫는 괄호

    onMounted(() => {
      if (window.kakao && window.kakao.maps) {
        initMap()
      } else {
        loadScript()
      }
    })
  }
})
</script>

<style lang="css">
button {
  width: 100px;
  align-self: end;
}

#inner-box {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  align-self: stretch; /* stretch to match the height of other components */
}

#map {
  margin-top: 10px;
  min-width: 300px;
  min-height: 200px;
  max-width: 600px;
  position: inherit;
  flex-grow: 2;
  align-self: stretch; /* stretch to match the height of other components */
}
/* button {
  width: 100px;
  align-self: end;
}
#inner-box {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  align-self: center;
}
#map {
  margin-top: 10px;
  min-width: 300px;
  min-height: 200px;
  max-width: 600px;
  position: inherit;
  flex-grow: 2;

} */
</style>
