package org.gs1us.sgg.mail;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class MockMailSender implements MailSender
{

    @Override
    public void send(SimpleMailMessage message) throws MailException
    {
        System.out.format("*** MAIL\n  * From: %s\n  * To: %s\n  * Subject: %s\n%s\n*** END MAIL\n",
                          message.getFrom(),
                          message.getTo()[0],
                          message.getSubject(),
                          message.getText());

    }

    @Override
    public void send(SimpleMailMessage... messages) throws MailException
    {
        for (SimpleMailMessage message : messages)
            send(message);

    }

}
