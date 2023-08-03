package com.ssafy.fcc.handler;

import com.ssafy.fcc.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class MyWebSocketHandler extends TextWebSocketHandler {

    private final MemberRepository memberRepository;

    // WebSocket 세션들을 관리하기 위한 HashMap
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트와의 연결이 수립되면 처리할 로직
        int memberId = (int) session.getAttributes().get("memberId");
        String id = memberRepository.findById(memberId).getLoginId();
        session.getAttributes().put("userId", id);
        sessions.put(id, session);
        System.out.println(sessions);
    }

    // 서버에서 클라이언트 전원에게 알림을 보내는 메소드
    public void sendNotificationToClient(String notificationMessage) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            session.sendMessage(new TextMessage(notificationMessage));
        }
    }

    // 서버에서 클라이언트 특정 사용자에게 알림을 보내는 메소드
    public void sendNotificationToSpecificUser(String loginId, String notificationMessage) throws IOException {
        WebSocketSession session = sessions.get(loginId);
        if (session != null) {
            session.sendMessage(new TextMessage(notificationMessage));
        }
    }

    @Override
    // 클라이언트로부터 받은 메시지를 처리하는 로직
    protected void handleTextMessage(WebSocketSession session, TextMessage userId) {
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // 클라이언트와의 연결이 종료되면 세션을 제거
        String id = (String) session.getAttributes().get("userId");
        sessions.remove(id);
    }
}
