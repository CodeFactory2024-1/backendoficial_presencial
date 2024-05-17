package co.udea.airline.api.service;

import java.io.UnsupportedEncodingException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import co.udea.airline.api.model.jpa.model.Person;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailSenderService {

    private JavaMailSender mailSender;

    String fromAddress = "sitassingapurairlines@gmail.com";
    String senderName = "Sitas airline";
    String frontBaseUrl = "front.codefact.noc";

    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a verification email to the user with a link to verify their
     * registration.
     *
     * @param user    The user to whom the verification email will be sent.
     * @param siteURL The base URL of the site to generate the verification link.
     * @throws MessagingException           If there is an error while sending the
     *                                      email.
     * @throws UnsupportedEncodingException If there is an error with the encoding
     *                                      of the email.
     */
    public void sendVerificationEmail(Person user)
            throws MessagingException, UnsupportedEncodingException {

        String toAddress = user.getEmail();

        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Sitas.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String fullName = user.getFirstName() + " " + user.getLastName();

        content = content.replace("[[name]]", (fullName));

        String verifyURL = frontBaseUrl + "/verify?code=" + user.getRecoveryCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    /**
     * Sends a password recovery email to the user with a link to reset their
     * password.
     *
     * @param user The user to send the recovery code to.
     * @return A string indicating the result of sending the email: "Sending email
     *         success".
     * @throws MessagingException           If there is an error while sending the
     *                                      email.
     * @throws UnsupportedEncodingException If there is an error with the encoding
     *                                      of the email.
     */
    public String sendRecoveryPasswCode(Person user)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String subject = "Recover your password";
        String content = "Dear [[name]],<br>"
                + "This is the code to recover your password:<br>"
                + "<h3>[[CODE]]</h3>"
                + "Thank you,<br>"
                + "Sitas.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        String fullName = user.getFirstName() + " " + user.getLastName();
        content = content.replace("[[name]]", (fullName));

        String code = user.getRecoveryCode();

        content = content.replace("[[CODE]]", code);

        helper.setText(content, true);

        mailSender.send(message);
        return "Sending email success";
    }

    public void sendAccountLockedNotification(Person user) throws MessagingException {
        String subject = "Unlock your account";
        String content = "Dear %s,<br>"
                + "Please click the link below to unlock your account and change your password:<br>"
                + "<h3><a href=\"%s\" target=\"_self\">VERIFY</a></h3>"
                + "<br>";

        String fullName = user.getFirstName() + " " + user.getLastName();
        String redirectUrl = frontBaseUrl + "/unlock?email=%s&code=%s";
        redirectUrl = redirectUrl.formatted(user.getEmail(), user.getRecoveryCode());
        content = content.formatted(fullName, redirectUrl);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }

}