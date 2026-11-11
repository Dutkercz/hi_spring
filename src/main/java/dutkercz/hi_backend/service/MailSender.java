package dutkercz.hi_backend.service;


import dutkercz.hi_backend.model.User;
import dutkercz.hi_backend.model.enums.EmailType;

public interface MailSender {

    void sendMail(User user, String otp, EmailType emailType);

}
