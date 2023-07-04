package com.inovusbot.mytestbot.service;

import com.inovusbot.mytestbot.dto.UserState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserService {
    private Map<String, UserState> activeUsers = new HashMap<>();

    public void clear() {
        activeUsers.clear();
    }

    public void changeContext(String userId, String context) {
        if (activeUsers.containsKey(userId)) {
            UserState userState = activeUsers.get(userId);
            userState.setContext(context);
            activeUsers.put(userId, userState);
        }
    }

    public void addNewUser(UserState userState) {
        this.activeUsers.put(userState.getChatId(), userState);
    }

    public String getUsername(String userId) {
        UserState userState = activeUsers.get(userId);
        if(userState == null) {
            return "Кто ты?";
        } else {
            return userState.getUsername();
        }
    }

    public Integer getLastMessageId(String userId) {
        UserState userState = activeUsers.get(userId);
        if(userState == null) {
            return null;
        } else {
            return userState.getLastMessageId();
        }
    }

    public void setLastMessageId(String userId, Integer messageId) {
        UserState userState = activeUsers.get(userId);
        if (userState != null) {
            userState.setLastMessageId(messageId);
        }

    }

    public boolean isUserAuthorized(String userId, String userName) {
        UserState userState = activeUsers.get(userId);
        if(userState == null) {
            userState = new UserState();
            userState.setChatId(userId);
            userState.setUsername(userName);
            userState.setAuthorized(true);
            userState.setContext("/");
            activeUsers.put(userId, userState);
            return true;
        }
        return userState.getAuthorized();
    }

    public void setGoogleToken(String userId, String token) {
        UserState userState = activeUsers.get(userId);
        userState.setGoogleToken(token);
        userState.setAuthorized(true);
    }
}
