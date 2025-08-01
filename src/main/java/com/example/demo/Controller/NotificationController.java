package com.example.demo.Controller;

import com.example.demo.DTO.request.NotificationDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name="Notification Controller",description="Notifications related information")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public void sendNotification(NotificationDTO notification) {
        String destination = "/topic/notifications/" + notification.getRecipientId();
        messagingTemplate.convertAndSend(destination, notification);
    }
}
