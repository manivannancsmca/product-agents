package com.product_agent.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ProductAgentService {

    private final ChatClient chatClient;

    public ProductAgentService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String processUserPrompt(String userPrompt) {
        return chatClient.prompt()
                .user(userPrompt)
                .call()
                .content();
    }
}