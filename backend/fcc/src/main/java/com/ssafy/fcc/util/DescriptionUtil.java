package com.ssafy.fcc.util;

import com.ssafy.fcc.domain.alarm.Step;
import com.ssafy.fcc.domain.member.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DescriptionUtil {
    private static final Map<String, String> descriptions;

    static {
        descriptions = new HashMap<>();
        descriptions.put(Step.FIRST.name(), "1차 경고 알림");
        descriptions.put(Step.SECOND.name(), "2차 경고 알림");
        descriptions.put(Step.ACTIVATION.name(), "차수판 동작알림");
        descriptions.put(Step.DEACTIVATION.name(), "차수판 해제알림");
        descriptions.put(Role.APART_MANAGER.name(), "아파트 관리자");
        descriptions.put(Role.PUBLIC_MANAGER.name(), "지자체 관리자");
        descriptions.put(Role.APART_MEMBER.name(), "주민");
        descriptions.put(Role.SYSTEM.name(), "시스템");
        descriptions.put(Role.USER.name(), "시민");
    }

    public String getDescription(String string) {
        return descriptions.get(string);
    }
}
