package dutkercz.hi_backend.service;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.Recipient;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;
import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.model.enums.EmailType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
//Usa o serviço de envio do Mailer Send https://www.mailersend.com/
public class MailerSendProvider implements MailSender {

    @Value(value = "${mailsender.api-key}")
    private String API_KEY;

    @Value(value = "${mailsender.domain}")
    private String DOMAIN;

    @Value(value = "${mailsender.recovery-url}")
    private String RECOVERY_BASE_URL;

    @Override
    public void sendMail(User user, String otpCode, EmailType emailType) {
        var firstName = user.getFullName().split(" ")[0];
        MailerSend mailerSend = new MailerSend();
        mailerSend.setToken(API_KEY);

        Email email = new Email();
        //para quem sera enviado o email
        Recipient recipient = new Recipient(firstName, user.getEmail().toLowerCase(Locale.ROOT).trim());

        //contem nome e email do destinatário
        email.addRecipient(recipient);
        try {
            switch (emailType) {
                case RECOVERY_PW: {
                    generateRecoveryMail(firstName, email, recipient, otpCode);
                    mailerSend.emails().send(email);
                    return;
                }
                case NOTIFICATION: {
                    return;
                }
                default:
                    return;
            }


        } catch (MailerSendException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }
    }

    private void generateRecoveryMail(String firstName, Email email, Recipient recipient, String otpCode) {
        //quem envia
        // id do template personalizado
        // linguagem, o provedor tem translate automatico
        // auxiliar para ter a propriedade first_name nos moldes do template
        // variavel / valor
        //auxiliar para ter a propiedade recovery_link nos moldes do template
        email.setFrom("Hotel Manager", "recovery-pw@" + DOMAIN);

        email.setTemplateId("z3m5jgrryzogdpyo");

        email.addPersonalization(recipient,"first_name",firstName);
        String recoveryUrl = RECOVERY_BASE_URL + otpCode;
        log.info("{}", recoveryUrl);
        email.addPersonalization(recipient, "reset_link", recoveryUrl);

        email.setSubject("Recuperação de senha");;
    }

}
