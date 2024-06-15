package team.cheese.service;

//import com.neo.byez.dao.user.UserDaoImpl;
import team.cheese.domain.MailHandler;
import team.cheese.domain.TempKey;
//import com.neo.byez.domain.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final String subject = "치즈마켓 인증메일 입니다.";
    private final String title = "<h1>치즈마켓 메일인증</h1>";
    private final String content = "<br>치즈마켓 메일인증 서비스 입니다" + "<br>인증번호: ";
    private final String senderName = "TeamCheeseMate";
    private final String senderEmail = "ghkdwjdgk23@gmail.com";

//    @Autowired UserDaoImpl userDao;
    @Autowired
    JavaMailSender mailSender;

    // 인증번호 생성 메서드 makeRandomMailKey()
    public String makeRandomMailKey() {
        return new TempKey().getKey(6, false);
    }

    // 메일 전송 메서드 send()
    public void send(String recipient, String mail_key) throws Exception {
        MailHandler sendMail = new MailHandler(mailSender);
        sendMail.setSubject(subject);
        sendMail.setText(
                title + content + mail_key
        );
        sendMail.setFrom(senderEmail, senderName);
        sendMail.setTo(recipient);
        sendMail.send();
    }



    // 회원가입 시 사용할 메일 인증 메서드
    public String sendMailToNonMember(String recipient) {
        // 1. 6자리 랜덤키 생성
        String mail_key = makeRandomMailKey();

        // 2. 메일 전송
        // 3. 메일을 전송하는 메서드 send() 실행 중 예외 발생 시 Null 반환
        // 4. 메일 전송 성공 시 인증번호 반환 (인증번호 일치 여부 확인 위함)
        try {
            send(recipient, mail_key);
            return mail_key;
        } catch (Exception e) {
            return null;
        }
    }
}
