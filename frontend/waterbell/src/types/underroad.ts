// types/Underroad.ts
export interface Sido {
  sidoName: string
  id: number
}

export interface Gugun {
  id: number
  gugunName: string
  sido: Sido
}

export interface Underroad {
  id: string
  gugun: Gugun
  firstFloodMessage: string
  secondFloodMessage: string
  firstAlarmValue: number
  secondAlarmValue: number
  hubIp: string
  status: string
  undergroundRoadName: string
  latitude: number
  longitude: number
  apart: boolean
  //   statusMsg: string
  //   //진입가능 /1차 경고/ 2차 경고/ 진입 금지
}
