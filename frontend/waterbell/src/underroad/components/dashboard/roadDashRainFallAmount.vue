<template>
    <div>
        <button> 이동하기 </button>
        <div id="map" style="width:500px;height:400px;"></div>
    </div>
</template>

<script lang="ts">
import { onMounted, ref } from 'vue'

export default {
  name: 'Map',
  setup() {
    const map = ref<any>(null)

    const initMap = () => {
      const container = document.getElementById('map')
      const options = {
        center: new (window as any).kakao.maps.LatLng(36.3549114724545, 127.345907414374),
        level: 8
      }
      map.value = new (window as any).kakao.maps.Map(container, options)
    }

    const loadScript = () => {
      const script = document.createElement('script')
      script.type = 'text/javascript'
      script.src = "//dapi.kakao.com/v2/maps/sdk.js?appkey=374cff0d8903c1ed2bf1e9533bb0feab&libraries=services,clusterer,drawing&autoload=true"
    //   document&libraries=clusterer,drawing,services'
      script.addEventListener('load', () => {
        (window as any).kakao.maps.load(initMap())
      })
      document.head.appendChild(script)
    }

    onMounted(() => {
      if (!(window as any).kakao || !(window as any).kakao.maps) {
        loadScript()
      }else{
        initMap()
      }

    })
  }
}
</script>

<style lang="">
</style>