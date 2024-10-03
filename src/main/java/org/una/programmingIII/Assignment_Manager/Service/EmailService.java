package org.una.programmingIII.Assignment_Manager.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String toEmail, String subject, String body) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String htmlBody = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f2f2f2; padding: 20px; }"
                + "h1 { color: #000359; text-align: center; }"
                + "p { color: #666; font-size: 16px; }"
                + ".footer { font-size: 12px; color: #999; margin-top: 20px; text-align: center; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div style='max-width: 600px; margin: 0 auto; background-color: white; padding: 20px; border-radius: 10px; box-shadow: 0px 0px 15px rgba(0, 0, 0, 0.1);'>"
                + "<h1>¡Bienvenido a WikiPet!</h1>"
                + "<p>" + body + "</p>"
                + "<hr style='border: none; border-top: 1px solid #ddd;'>"
                + "<p class='footer'>Este correo fue generado automáticamente. Por favor, no responda a este mensaje.</p>"
                + "</div>"
                + "</body>"
                + "</html>";

        helper.setText(htmlBody, true);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setFrom("wikiPetsUNA@gmail.com");

        mailSender.send(mimeMessage);
        System.out.println("Correo enviado a " + toEmail);
    }
}