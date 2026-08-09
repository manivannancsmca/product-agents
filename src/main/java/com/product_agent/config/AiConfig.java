package com.product_agent.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.product_agent.tool.ProductTools;

@Configuration
public class AiConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, ProductTools productTools) {
        return builder
                .defaultSystem("""
                        You are an autonomous Senior Inventory & Product Management Assistant for an e-commerce platform.
                        You have direct tool access to query and modify product data in MySQL.
                        
                        Rules:
                        1. ALWAYS use the provided tools to fetch real data before answering inventory questions. Never guess product quantities or details.
                        2. When asked to perform multi-step actions (e.g., check low stock and update quantities), execute all required tools in sequence.
                        3. Provide clear, concise, and structured summaries of the actions taken and results retrieved.
                        """)
                .defaultTools(productTools)
                .build();
    }
}
