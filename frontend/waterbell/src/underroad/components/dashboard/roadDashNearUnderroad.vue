<template lang="">
    <div>
        <button> 이동하기 </button>
        <div id="map" style="width:500px;height:400px;"></div>
    </div>
</template>

<script lang="ts">
import { defineComponent, onMounted } from 'vue'

export default defineComponent({
  setup() {
    const kakao = (window as any).kakao

  },
})

// eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
function loadScript () {
  const script = document.createElement('script')
  script.type = 'text/javascript'
    script.src =
    '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=[key]&libraries=clusterer,drawing,services'
      /* eslint를 사용한다면 kakao 변수가 선언되지 않았다고
     * 오류를 내기 때문에 아래 줄과 같이 전역변수임을
     * 알려주어야 한다. */

      script.addEventListener('load', () => {
        this.window.kakao.maps.load(() => {
        // 카카오맵 API가 로딩이 완료된 후 지도의 기본적인 세팅을 시작해야 한다.
          this.initMap()
        })
      })
      document.head.appendChild(script)
    //      const script1 = document.createElement('script')
    //      const script = document.createElement('script')
    //       script1.src =
    //     'https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js'
    //     script.src =
    //     '//dapi.kakao.com/v2/maps/sdk.js?appkey=b7b8e1778c7980e210173939a25d2b2b&libraries=services,clusterer,drawing&autoload=true'
    //   document.head.appendChild(script1)
    //   script.onload = () => {
    //     window.kakao.maps.load(this.loadMap)
    //   }
    //   document.head.appendChild(script)
    }
    // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
    function initMap () {
      const container = document.getElementById('map')
      const options = {
      // 처음 지도의 위치를 lat, lng(위도, 경도) 값으로 설정한다.
        center: new kakao.maps.LatLng(36.3549114724545,
          127.345907414374),
        level: 8
      }

      this.map = new kakao.maps.Map(container, options)
    }

    //   this.setMarkerClusterer()


  // 스크립트와 지도를 생성하기 위한 과정.
  // eslint-disable-next-line @typescript-eslint/explicit-module-boundary-types
  onMounted (() => {
    if (window.kakao && window.kakao.maps) {
      loadMap()
    } else {
      loadScript()
    }

  })



</script>

<style lang="">

</style>

function loadMap() {
  throw new Error('Function not implemented.')
}
