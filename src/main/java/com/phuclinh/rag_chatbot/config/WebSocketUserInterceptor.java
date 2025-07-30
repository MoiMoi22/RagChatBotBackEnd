package com.phuclinh.rag_chatbot.config;

import com.phuclinh.rag_chatbot.security.CustomUserDetails;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class WebSocketUserInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompCommand command = accessor.getCommand();

        if (command == null) return message;

        switch (command) {
            case SUBSCRIBE, SEND, DISCONNECT -> {
                if (accessor.getUser() == null) {
                    Object saved = accessor.getSessionAttributes().get("user");
                    if (saved instanceof CustomUserDetails userDetails) {
                        Authentication auth = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        accessor.setUser(auth);
                        System.out.println("🔄 [ChannelInterceptor] Gán lại Principal: " + userDetails.getUsername());
                    }
                }

                if (command == StompCommand.SUBSCRIBE) {
                    System.out.println("📡 [SUBSCRIBE] từ: " + (accessor.getUser() != null ? accessor.getUser().getName() : "anonymous"));
                }
            }
        }

        return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
    }
}
