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
import { defineComponent, onMounted, ref } from 'vue'
import roadImgSrc0 from '@/assets/images/map_road_0.png'

export default defineComponent({
  name: 'roadDashMapVue',
  setup() {
    const map = ref<any>(null)

    const initMap = () => {
      const container = document.getElementById('map')
      const options = {
        center: new window.kakao.maps.LatLng(33.450705, 126.570677), //카카오 마커 확인용
        // center: new window.kakao.maps.LatLng(36.3549114724545, 127.345907414374),
        level: 6
      }
      map.value = new window.kakao.maps.Map(container, options)

      makeMarker()
    }

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
    // const roadImgSrc = "@/assets/map_roadIcon.png"

    const makeMarker = () => {
      let positions = [
        {
          title: '카카오',
          latlng: new window.kakao.maps.LatLng(33.450705, 126.570677)
        },
        {
          title: '생태연못',
          latlng: new window.kakao.maps.LatLng(33.450936, 126.569477)
        },
        {
          title: '텃밭',
          latlng: new window.kakao.maps.LatLng(33.450879, 126.56994)
        },
        {
          title: '근린공원',
          latlng: new window.kakao.maps.LatLng(33.451393, 126.570738)
        }
      ]
      // const roadImgSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"
      // const roadImgSrc = require('@/assets/images/map_roadIcon.png')
      // let markers = []

      for (let i of positions) {
        let imgSize = new window.kakao.maps.Size(25, 25)

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
    }

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
#outline {
  width: 400px;
  border: 3px;
  border-color: black;
}
</style>
