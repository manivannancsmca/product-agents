package com.product_agent.dto;

import jakarta.validation.constraints.NotBlank;

public record AgentRequest(
        @NotBlank(message = "Prompt string cannot be empty")
        String prompt
) {}
