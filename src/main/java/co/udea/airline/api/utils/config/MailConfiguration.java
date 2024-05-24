package co.udea.airline.api.utils.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Value("${airline-api.mail.enabled:true}")
    private boolean enabled;
    @Value("${airline-api.mail.host}")
    private String host;
    @Value("${airline-api.mail.port}")
    private int port;
    @Value("${airline-api.mail.username}")
    private String username;
    @Value("${airline-api.mail.password}")
    private String password;

    @Value("${airline-api.mail.transport.protocol}")
    private String protocol;
    @Value("${airline-api.mail.smtp.use-auth}")
    private String useAuth;
    @Value("${airline-api.mail.smtp.use-tls}")
    private String useTls;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        if (enabled) {

            mailSender.setHost(host);
            mailSender.setPort(port);

            mailSender.setUsername(username);
            mailSender.setPassword(password);

            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.transport.protocol", protocol);
            props.put("mail.smtp.auth", useAuth);
            props.put("mail.smtp.starttls.enable", useTls);
        }

        return mailSender;
    }
}