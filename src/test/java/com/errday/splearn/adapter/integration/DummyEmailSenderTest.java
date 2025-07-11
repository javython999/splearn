package com.errday.splearn.adapter.integration;

import com.errday.splearn.domain.shared.Email;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.StdIo;
import org.junitpioneer.jupiter.StdOut;

import static org.assertj.core.api.Assertions.assertThat;

class DummyEmailSenderTest {

    @Test
    @StdIo
    void dummyEmailSenderTest(StdOut out) {
        DummyEmailSender dummyEmailSender = new DummyEmailSender();

        dummyEmailSender.send(new Email("test@test.com"), "subject", "body");

        assertThat(out.capturedLines()[0]).isEqualTo("DummyEmailSender send email: Email[address=test@test.com]");
    }

}