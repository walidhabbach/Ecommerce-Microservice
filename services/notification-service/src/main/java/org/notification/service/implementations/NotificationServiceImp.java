package org.notification.service.implementations;

import lombok.RequiredArgsConstructor;
import org.notification.dto.NotificationDto;
import org.notification.exception.wrapper.NotificationNotFoundException;
import org.notification.helper.NotificationMapper;
import org.notification.repository.NotificationRepository;
import org.notification.service.interfaces.NotificationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class NotificationServiceImp implements NotificationService{
    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationDto> findAllByCustomerId(String customerId){
        return this.notificationRepository.findAllByCustomerId(customerId)
                .stream()
                .map(NotificationMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDto findById(String id){
        return this.notificationRepository.findById(id)
                .map(NotificationMapper::map)
                .orElseThrow(() -> new NotificationNotFoundException(String.format("No notification found with the provided ID: %s", id)));
    }
}
