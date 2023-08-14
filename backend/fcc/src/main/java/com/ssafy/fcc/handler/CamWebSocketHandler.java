package com.ssafy.fcc.handler;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.fcc.dto.MqttTopic;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CamWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions =  new HashMap<>();
    // clientId : facilityId
    private final Map<String, String> clients = new HashMap<>();
    //facilityId : 개수
    private final Map<String, Integer> facilityNum = new HashMap<>();

    ObjectMapper objectMapper;
    // 연결을 맺고 나서 실행되는 메소드
    private String generateClientId() {
        return UUID.randomUUID().toString();
    }

    AWSIotMqttClient mqttClient;
    Base64.Encoder encode = Base64.getEncoder();
    byte[] encodeByte;
    @Autowired
    public CamWebSocketHandler(AWSIotMqttClient mqttClient){
        this.mqttClient = mqttClient;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트 식별자 생성
        String clientId = generateClientId();
        // 클라이언트 연결 후
        // 쿼리에서 값 가져옴.
        String query = session.getUri().getQuery();
        MultiValueMap<String, String> queryParams = UriComponentsBuilder.fromUriString("?" + query).build().getQueryParams();
        String facilityId = queryParams.getFirst("facilityId");
        String camNum = queryParams.getFirst("camNum");

        // 세션에 식별자 저장
        session.getAttributes().put("camClient"+camNum, clientId);
        sessions.put(clientId, session);

        //
        JSONObject msg = new JSONObject();
        msg.put("camClient"+camNum, clientId);
        objectMapper = new ObjectMapper();
        session.sendMessage(new TextMessage(msg.toString()));


        String topic = facilityId + "/CAM" + camNum ;
        session.getAttributes().put("facilityId", topic);

        // 연결된 건물 id
        clients.put(topic, clientId);

        // 연결된 건물 개수
        if(facilityNum.containsKey(topic)){
            facilityNum.put(topic, facilityNum.get(topic) + 1);
        }else{
            facilityNum.put(topic, 1);
        }
        // subscriber, publisher 생성
        String subtopic = "Arduino/"+topic;
        System.out.println("받아온 토픽" + topic);
        AWSIotMqttClient client = mqttClient;
        client.subscribe(new MqttTopic(subtopic){
            @Override
            public void onMessage(AWSIotMessage message) {
                String topic = message.getTopic();
                String[] topicList = topic.split("/");
                String payload = message.getStringPayload();
                System.out.println(payload);

                encodeByte = encode.encode(message.getPayload());
                System.out.println("인코딩 후 : " + new String(encodeByte));
                try {
                    sendVideoImg(Integer.parseInt(topicList[1]), topicList[2], new String(encodeByte));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("보냄");
            }
        });
        String pubtopic = "Server/"+topic;
        client.publish(pubtopic, "ON");
    }


    // 클라이언트에게 프레임 전달
    public void sendVideoImg(int facilityId, String camNum, String temp_img) throws IOException {
        System.out.println("사진 받은 시설"+facilityId);

        String str = facilityId+camNum;

        for(String id : clients.keySet()){
            if(id.equals(str)){
                System.out.println(clients.get(id));
                WebSocketSession session = sessions.get(clients.get(id));

                objectMapper = new ObjectMapper();
                JSONObject msg = new JSONObject();
                msg.put("temp_img", temp_img);
                session.sendMessage(new TextMessage(msg.toString()));

            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        // 클라이언트에서 받은 메시지 처리

    }

    // 연결이 종료되면 실행됨
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws AWSIotException {
        // cam mqtt 종료
        // 기존 토픽의 subscribe 요청 제한
        // 세션 제거

        String facilityId = (String) session.getAttributes().get("facilityId");
        String clientId = clients.get(facilityId);
        String topic = "Server/"+facilityId;
        facilityNum.put(facilityId, facilityNum.get(facilityId)-1);

        if(facilityNum.get(facilityId)==0){
            AWSIotMqttClient client = mqttClient;
            client.publish(topic, "OFF");
            facilityNum.remove(facilityId);
        }

        sessions.remove(clientId);
        clients.remove(clientId);
    }


}
