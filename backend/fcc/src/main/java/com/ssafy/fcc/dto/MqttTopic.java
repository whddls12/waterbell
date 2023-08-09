package com.ssafy.fcc.dto;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.ssafy.fcc.domain.facility.Facility;
import com.ssafy.fcc.domain.facility.WaterStatus;
import com.ssafy.fcc.domain.log.SensorType;
import com.ssafy.fcc.handler.CamWebSocketHandler;
import com.ssafy.fcc.repository.FacilityRepository;
import com.ssafy.fcc.service.ApartService;
import com.ssafy.fcc.service.FacilityService;
import com.ssafy.fcc.service.SystemService;
import com.ssafy.fcc.service.UndergroundRoadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Base64;

public class MqttTopic extends AWSIotTopic {


    public MqttTopic(String topic) {
        super(String.valueOf(topic));
    }

    @Override
    public void onMessage(AWSIotMessage message) {

        System.out.println(message.getTopic());
        System.out.println(message.getStringPayload());
    }
}
