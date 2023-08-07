package com.ssafy.fcc.MQTT;

import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.log.SensorType;
import com.ssafy.fcc.repository.FacilityRepository;

import com.ssafy.fcc.service.ApartService;
import com.ssafy.fcc.service.FacilityService;
import com.ssafy.fcc.service.SystemService;
import com.ssafy.fcc.service.UndergroundRoadService;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.*;

import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Base64;


@Component
@Transactional
@RequiredArgsConstructor
public class MqttSubscriber implements MqttCallback {

    public MqttClient mqttclient;

    public final SystemService systemService;

    public final FacilityService facilityService;

    public final FacilityRepository facilityRepository;

    public final UndergroundRoadService undergroundRoadService;

    public final ApartService apartService;


    //Mqtt프로토콜를 이용해서 broker에 연결하면서 연결정보를 설정할 수 있는 객체
    public MqttConnectOptions mqttOption;

    public MqttSubscriber init(String server, String clientId) {
        try {
            mqttOption = new MqttConnectOptions();
            mqttOption.setCleanSession(true);
            mqttOption.setKeepAliveInterval(30);

            mqttclient = new MqttClient(server, clientId);

            mqttclient.setCallback(this);
            mqttclient.connect(mqttOption);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {


        try {
            System.out.println(topic + " " + message.toString());
            // TODO 만들어지는 topic 계층에 따라 (facility_id, Temp,Dust,Humid or Cam) 뽑아야함

            // 예를들어 Temp
            // facility_id = 7, category = Temp
            String[] result = message.toString().split("/");

            int facilityId = Integer.parseInt(result[0]);
            SensorType category = SensorType.valueOf(topic.toUpperCase());
            int value = Integer.parseInt(result[1]);

            Facility facility = facilityRepository.findById(facilityId);

            if (category.equals("height")) {
                checkSituation(facility, value);
            }


            // TODO category에 따라 프론트로 웹소켓 통신 or 측정 로그 저장
            if (topic.equals("Cam")) {

                Base64.Encoder encode = Base64.getEncoder();

                byte[] encodeByte = encode.encode(message.getPayload());

                System.out.println("인코딩 후 : " + new String(encodeByte));

            } else {
            systemService.insertLog(facilityId, category, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void checkSituation(Facility facility, int value) throws Exception {

        if (value > facility.getSecondAlarmValue()) {
            if (facility.getStatus() == WaterStatus.FIRST || facility.getStatus() == WaterStatus.DEFAULT) {
                facilityService.updateStatus(facility, WaterStatus.SECOND);
                if (facility.isApart()) { // 아파트
                    apartService.sendAutoNotificationToManager(facility.getId(), facility.getStatus(), value);
                } else { // 지하차도
                    undergroundRoadService.sendAutoNotification(facility.getId(), facility.getStatus(), value);
                }
            }

        } else if (value > facility.getFirstAlarmValue()) {
            if (facility.getStatus() == WaterStatus.DEFAULT) {
                facilityService.updateStatus(facility, WaterStatus.FIRST);
                if (facility.isApart()) { // 아파트
                    apartService.sendAutoNotificationToManager(facility.getId(), facility.getStatus(), value);
                    apartService.sendAutoNotificationToMember(facility.getId());
                } else { // 지하차도
                    undergroundRoadService.sendAutoNotification(facility.getId(), facility.getStatus(), value);
                }
            } else if (facility.getStatus() == WaterStatus.SECOND) {
                facilityService.updateStatus(facility, WaterStatus.FIRST);
            }

        } else {
            facilityService.updateStatus(facility, WaterStatus.DEFAULT);
        }

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }


    // 구독 신청
    public boolean subscribe(String topic) {

        boolean result = true;
        if (topic != null) {
            try {
                mqttclient.subscribe(topic, 0);
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
        }
        return result;
    }
}
