package com.swp391.JewelryProduction.services.email;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendEmail(String toEmail, String subject, String body);
    void sendEmailWithHtml(String toEmail, String subject, String message) throws MessagingException;
}
