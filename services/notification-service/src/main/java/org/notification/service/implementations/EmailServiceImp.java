package org.notification.service.implementations;


import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.notification.dto.OrderLineDto;
import org.notification.service.interfaces.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import  org.notification.constant.EmailTemplate;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImp implements EmailService{

    @Autowired
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    @Override
    public void sendPaymentEmail(String destinationEmail, String customerName, BigDecimal amount, Integer orderId){

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());


            final String templateName = EmailTemplate.PAYMENT_CONFIRMATION.getTemplate();

            Map<String, Object> variables = new HashMap<>();
            variables.put("customerName", customerName);
            variables.put("amount", amount);
            variables.put("orderId", orderId);

            Context context = new Context();
            context.setVariables(variables);
            messageHelper.setSubject(EmailTemplate.PAYMENT_CONFIRMATION.getSubject());

                String htmlTemplate = templateEngine.process(templateName, context);
                messageHelper.setText(htmlTemplate, true);

                messageHelper.setTo(destinationEmail);
                mailSender.send(mimeMessage);
                log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }

    }


    @Async
    @Override
    public void sendOrderEmail(String destinationEmail, String customerName, BigDecimal amount, Integer orderId, List<OrderLineDto> products) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, UTF_8.name());


            final String templateName = EmailTemplate.ORDER_CONFIRMATION.getTemplate();

            Map<String, Object> variables = new HashMap<>();
            variables.put("customerName", customerName);
            variables.put("totalAmount", amount);
            variables.put("orderId", orderId);
            variables.put("products", products);

            Context context = new Context();
            context.setVariables(variables);
            messageHelper.setSubject(EmailTemplate.ORDER_CONFIRMATION.getSubject());

                String htmlTemplate = templateEngine.process(templateName, context);
                messageHelper.setText(htmlTemplate, true);

                messageHelper.setTo(destinationEmail);
                mailSender.send(mimeMessage);
                log.info(String.format("INFO - Email successfully sent to %s with template %s ", destinationEmail, templateName));
        } catch (MessagingException e) {
            log.warn("WARNING - Cannot send Email to {} ", destinationEmail);
        }

    }


}