package com.ssafy.fcc.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {

    // WebSocket 세션들을 관리하기 위한 HashMap
    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트와의 연결이 수립되면 세션을 추가
        // 이건 시큐리티 쓰게되면
//      String name = (String) session.getPrincipal().getName();
        String name = "관리자" + (sessions.size()+1);
        session.getAttributes().put("userId", name);
        sessions.put(name, session);
        System.out.println(sessions);
        System.out.println(name);
    }

    // 서버에서 클라이언트 전원에게 알림을 보내는 메소드
    public void sendNotificationToClient(String notificationMessage) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            session.sendMessage(new TextMessage(notificationMessage));
        }
    }

    // 서버에서 클라이언트 특정 사용자에게 알림을 보내는 메소드
    public void sendNotificationToSpecificUser(String userId, String notificationMessage) throws IOException {
        WebSocketSession session = sessions.get(userId);
        if (session != null) {
            session.sendMessage(new TextMessage(notificationMessage));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // 클라이언트로부터 받은 메시지를 처리하는 로직 (필요한 경우 구현)
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 클라이언트와의 연결이 종료되면 세션을 제거
        String userId = (String) session.getAttributes().get("userId");
        sessions.remove(userId);
    }
}
