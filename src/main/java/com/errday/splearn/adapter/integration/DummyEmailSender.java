package com.errday.splearn.adapter.integration;

import com.errday.splearn.application.member.required.EmailSender;
import com.errday.splearn.domain.shared.Email;
import org.springframework.stereotype.Component;

@Component
public class DummyEmailSender implements EmailSender {

    @Override
    public void send(Email email, String subject, String body) {
        System.out.println("DummyEmailSender send email: " + email);
    }
}
