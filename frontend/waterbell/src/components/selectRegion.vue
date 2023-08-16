<template>
  <div class="select-region">
    <select
      class="select-region-box"
      v-model="selectedSido"
      @change="updateDistricts"
    >
      <option disabled value="">시도 선택</option>
      <option v-for="region in regions" :key="region.id" :value="region.id">
        {{ region.sidoName }}
      </option>
    </select>
    <!-- </div> -->

    <!-- 시구군 선택 -->
    <select
      class="select-region-box"
      v-model="selectedGugun"
      @change="updateUndergroundRoads"
    >
      <option disabled value="">시구군 선택</option>
      <option
        v-for="district in districts"
        :key="district.gugunId"
        :value="district.gugunId"
      >
        {{ district.gugunName }}
      </option>
    </select>

    <!-- 지하차도 선택 -->
    <select class="select-region-box" v-model="selectedUnderground">
      <option disabled value="">지하차도 선택</option>
      <option v-for="road in undergroundRoads" :key="road.id" :value="road.id">
        {{ road.undergroundRoadName }}
      </option>
    </select>

    <div>
      <button class="go-selected-btn" @click="moveFacility">이동</button>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted, watch } from 'vue'
import store from '@/store/index'
import { changeFacility } from '@/types/changeFacility'

export default defineComponent({
  name: 'selectRegion',
  setup() {
    // const regions = computed(() => store.getters['auth/underroadListByGugun'])
    const data = computed(() => store.getters['auth/underroadListByGugun'])
    const now = computed(() => store.getters['auth/nowUnderroad'])
    const facilityId = computed(() => store.getters['auth/facilityId'])
    const regions = ref([{ id: 3, sidoName: '대전' }])

    type District = {
      gugunName: string
      gugunId: number
      underroads: any[]
    }
    const districts = ref<District[]>([])
    type UndergroundRoad = {
      id: number
      undergroundRoadName: string
    }
    const undergroundRoads = ref<UndergroundRoad[]>([])

    let selectedSido = ref(null)
    let selectedGugun = ref(null)
    let selectedUnderground = ref(null)

    const updateDistricts = () => {
      districts.value = []
      const matchingDistricts = data.value.filter(
        (item: any) => item.sido.id === selectedSido.value
      )
      matchingDistricts.forEach((item: any) => {
        districts.value.push({
          gugunName: item.gugunName,
          gugunId: item.gugunId,
          underroads: item.underroads
        })
      })
    }

    const updateUndergroundRoads = () => {
      const selectedDistrict = districts.value.find(
        (district) => district.gugunId === selectedGugun.value
      )
      undergroundRoads.value = selectedDistrict
        ? selectedDistrict.underroads
        : []
    }

    const moveFacility = () => {
      changeFacility(selectedUnderground.value)
      window.location.reload()
    }

    const setFirst = async () => {
      selectedSido.value = now.value.gugun.sido.id
      updateDistricts()
      selectedGugun.value = now.value.gugun.id
      updateUndergroundRoads()
      selectedUnderground.value = now.value.id
    }

    watch(
      facilityId,
      async (newVal, oldVal) => {
        if (newVal !== oldVal) {
          await setFirst()
        }
      },
      { immediate: true }
    )

    onMounted(async () => {
      await setFirst()
    })

    return {
      regions,
      districts,
      undergroundRoads,
      selectedSido,
      selectedGugun,
      selectedUnderground,
      updateDistricts,
      updateUndergroundRoads,
      moveFacility
    }
  }
})
</script>
<style scoped>
.select-region {
  display: flex;
  justify-content: center;
  margin: 10px 0px;
}
.select-region-box {
  border: 1px solid #939393;
  border-radius: 8px;
  background-color: white;

  width: 130px;
  margin: 10px 10px;
}
/* 이동 버튼 */
.go-selected-btn {
  display: block;
  border: 1px solid #10316b;
  border-radius: 8px;
  color: white;
  background-color: #10316b;

  width: 50px;
  margin: 10px 10px;
}
</style>
