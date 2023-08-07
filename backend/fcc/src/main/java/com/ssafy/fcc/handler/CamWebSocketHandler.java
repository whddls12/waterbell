package com.ssafy.fcc.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.fcc.MQTT.MqttPublisher;
import com.ssafy.fcc.MQTT.MqttSubscriber;
import com.ssafy.fcc.repository.FacilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CamWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions =  new HashMap<>();
    // clientId : facilityId
    private final Map<String, String> clients = new HashMap<>();
    private final Map<String, MqttSubscriber> subscribers =  new HashMap<>();
    //facilityId : 개수
    private final Map<String, Integer> facilityNum = new HashMap<>();
    MqttSubscriber subscriber;
    MqttPublisher publisher;

    ObjectMapper objectMapper;
    private final FacilityRepository facilityRepository;

    // 연결을 맺고 나서 실행되는 메소드
    private String generateClientId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트 식별자 생성
        String clientId = generateClientId();

        // 세션에 식별자 저장
        session.getAttributes().put("camClient", clientId);
        sessions.put(clientId, session);

        //
        objectMapper = new ObjectMapper();
        Message msg = new Message("camClient", clientId);
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
//        session.sendMessage(new TextMessage(clientId));

        // 클라이언트 연결 후
        // 쿼리에서 값 가져옴.
        String query = session.getUri().getQuery();
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUriString("?" + query).build().getQueryParams();
        String facilityId = queryParams.getFirst("facilityId");
        String camNum = queryParams.getFirst("camNum");

        String topic = facilityId + "/cam" + camNum + "/picture";
        session.getAttributes().put("facilityId", topic);

        // 연결된 건물 id
        clients.put(clientId, topic);

        // 연결된 건물 개수
        if(facilityNum.containsKey(topic)){
            facilityNum.put(topic, facilityNum.get(topic) + 1);
        }else{
            facilityNum.put(topic, 1);
        }
        // subscriber, publisher 생성
        String hubIp = facilityRepository.findById(Integer.parseInt(facilityId)).getHubIp();

        // subscriber 등록
        subscriber.init(hubIp,topic);

        // topic 생성
        subscriber.subscribe(topic);
        subscribers.put(clientId, subscriber);
        publisher.send(topic, "ON");
    }


    // 클라이언트에게 프레임 전달
    public void sendVideoImg(int facilityId, String temp_img) throws IOException {
        for(String id : clients.values()){
            if(Integer.parseInt(id) == facilityId){
                WebSocketSession session = sessions.get(id);
                objectMapper = new ObjectMapper();
                Message msg = new Message("temp_img", temp_img);
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        // 클라이언트에서 받은 메시지 처리

    }

    // 연결이 종료되면 실행됨
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status){
        // cam mqtt 종료
        // 기존 토픽의 subscribe 요청 제한
        // 세션 제거

        String facilityId = (String) session.getAttributes().get("facilityId");
        String clientId = clients.get(facilityId);
        String topic = facilityId + "/picture";

        facilityNum.put(facilityId, facilityNum.get(facilityId)-1);

        if(facilityNum.get(facilityId)==0){
            publisher.send(topic, "OFF");
            facilityNum.remove(facilityId);
        }

        publisher.close();
        subscribers.remove(clientId);
        sessions.remove(clientId);
        clients.remove(clientId);
    }

}
