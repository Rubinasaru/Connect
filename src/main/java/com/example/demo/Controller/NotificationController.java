package com.example.demo.Controller;

import com.example.demo.DTO.request.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendNotification(NotificationDTO notification) {
        String destination = "/topic/notifications/" + notification.getRecipientId();
        messagingTemplate.convertAndSend(destination, notification);
    }
}
