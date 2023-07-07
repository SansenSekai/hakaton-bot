package com.inovusbot.mytestbot.service;

import com.inovusbot.mytestbot.dto.UserState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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

    public void setAccessToken(String userId, String accessToken) {
        UserState userState = activeUsers.get(userId);
        userState.setGoogleAccessToken(accessToken);
    }
    public String getAccessToken(String userId) {
        UserState userState = activeUsers.get(userId);
        return userState.getGoogleAccessToken();
    }


    public String getContext(String userId) {
        UserState userState = activeUsers.get(userId);
        if(userState == null) {
            return "/";
        } else {
            return userState.getContext();
        }
    }

    public String getCode(String userId) {
        UserState userState = activeUsers.get(userId);
        if(userState == null) {
            return "";
        } else {
            return userState.getCode();
        }
    }

    public void setContext(String userId, String context) {
        UserState userState = activeUsers.get(userId);
        if (userState != null) {
            userState.setContext(context);
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
            userState.setAuthorized(false);
            userState.setContext("/");
            activeUsers.put(userId, userState);
            return false;
        }
        return userState.getAuthorized();
    }

    public void setCode(String userId, String code) {
        UserState userState = activeUsers.get(userId);
        userState.setCode(code);
        userState.setAuthorized(true);
    }

    public Integer getTextIndex(String userId) {
        UserState userState = activeUsers.get(userId);
        Integer textIndex = userState.getTextIndex();
        if(textIndex > 4) {
            Random random = new Random();
            return random.nextInt(4) + 1;
        }
        else {
            userState.setTextIndex(textIndex + 1);
            return textIndex;
        }
    }

    public void setEmail(String userId, String email) {
        UserState userState = activeUsers.get(userId);
        userState.setEmail(email);
    }

    public String getEmail(String userId) {
        UserState userState = activeUsers.get(userId);
        return userState.getEmail();
    }

    public void logout(String userId) {
        activeUsers.remove(userId);
    }
}
