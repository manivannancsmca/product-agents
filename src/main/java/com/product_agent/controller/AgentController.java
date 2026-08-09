package com.product_agent.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.product_agent.dto.AgentRequest;
import com.product_agent.dto.AgentResponse;
import com.product_agent.service.ProductAgentService;

@RestController
@RequestMapping("/api/v1/agent")
public class AgentController {

    private final ProductAgentService agentService;

    public AgentController(ProductAgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping("/chat")
    public ResponseEntity<AgentResponse> handleAgentQuery(@Valid @RequestBody AgentRequest request) {
        String answer = agentService.processUserPrompt(request.prompt());
        return ResponseEntity.ok(new AgentResponse(answer));
    }
}
