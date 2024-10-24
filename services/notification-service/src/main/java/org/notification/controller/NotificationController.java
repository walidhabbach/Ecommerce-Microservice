package org.notification.controller;

import lombok.RequiredArgsConstructor;
import org.notification.dto.NotificationDto;
import org.notification.service.interfaces.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController{

  private final NotificationService notificationService;

  @GetMapping("/{customerId}")
  public ResponseEntity<List<NotificationDto>> findAllByCustomerId(@PathVariable("customerId") String customerId) {
    return ResponseEntity.ok(this.notificationService.findAllByCustomerId(customerId));
  }
}
