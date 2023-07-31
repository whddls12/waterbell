<template>
  <div id="outline">
    <button>이동하기</button>
    <div id="map" style="width: 500px; height: 400px"></div>
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
import { useStore } from 'vuex'


export default defineComponent({
  name: 'roadDashMapVue',
  setup() {

    const store = useStore()
    //지도에 찍을 지하차도 list
    const underroadList = computed(() => store.getters.underroadList).value

    let position_ok = ref<{ title: string; latlng: any }[]>([])
    let position_warn = ref<{ title: string; latlng: any }[]>([])
    let position_block = ref<{ title: string; latlng: any }[]>([])

    const setPositions = () => {
      for (let roads of underroadList) {
        for (let road of roads.underroads) {
          if (road.status == 'DEFAULT') {
            position_ok.value.push({
              title: road.undergroundRoadName,
              latlng: new window.kakao.maps.LatLng(
                road.latitude,
                road.longitude
              )
            })
          } else if (road.status == '1차' || road.status == '2차') {
            position_warn.value.push({
              title: road.undergroundRoadName,
              latlng: new window.kakao.maps.LatLng(
                road.latitude,
                road.longitude
              )
            })
          } else {
            console.log(road.status)
            position_block.value.push({
              title: road.undergroundRoadName,
              latlng: new window.kakao.maps.LatLng(
                road.latitude,
                road.longitude
              )
            })
          }
        }
      }
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


    const initMap = () => {
      const container = document.getElementById('map')
      const options = {
        center: new window.kakao.maps.LatLng(37.5640907, 126.9979403), //카카오 마커 확인용
        // center: new window.kakao.maps.LatLng(36.3549114724545, 127.345907414374),
        level: 6
      }
      map.value = new window.kakao.maps.Map(container, options)






      makeMarker()
    }
    //마커 만들기
    const makeMarker = () => {
      // const roadImgSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"
      // const roadImgSrc = require('@/assets/images/map_roadIcon.png')
      // let markers = []
      setPositions()
      let imgSize = new window.kakao.maps.Size(25, 25)
      for (let i of position_ok.value) {
        let markerImage = new window.kakao.maps.MarkerImage(
          roadImgSrc0,
          imgSize
        )

        let marker = new window.kakao.maps.Marker({
          map: map.value,
          position: i.latlng,
          title: i.title,
          image: markerImage
        })
      }

      for (let i of position_warn.value) {
        let markerImage = new window.kakao.maps.MarkerImage(
          roadImgSrc1,
          imgSize
        )

        let marker = new window.kakao.maps.Marker({
          map: map.value,
          position: i.latlng,
          title: i.title,
          image: markerImage
        })
      }
      for (let i of position_block.value) {
        let markerImage = new window.kakao.maps.MarkerImage(
          roadImgSrc2,
          imgSize
        )

        let marker = new window.kakao.maps.Marker({
          map: map.value,
          position: i.latlng,
          title: i.title,
          image: markerImage
        })
      }
    }
    //
    onMounted(async () => {
      await store.dispatch('fetchUnderroads')

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
#outline {
  width: 400px;
  border: 3px;
  border-color: black;
}
</style>
