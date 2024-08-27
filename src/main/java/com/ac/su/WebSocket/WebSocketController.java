package com.ac.su.WebSocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/changeStatus")
    @SendTo("/topic/status")
    public String sendStatusUpdate(String status) {
        return status;
    }
}
