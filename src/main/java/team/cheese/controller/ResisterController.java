package team.cheese.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.cheese.dao.AdministrativeDao;
import team.cheese.domain.AddrCdDto;
import team.cheese.domain.AdministrativeDto;
import team.cheese.domain.UserDto;
import team.cheese.service.AddrCdService;
import team.cheese.service.AdminService;
import team.cheese.service.MailService;
import team.cheese.service.UserService;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class ResisterController {
    @Autowired
    MailService mailService;
    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @Autowired
    AddrCdService addrCdService;

    @Autowired
    AdministrativeDao administrativeDao;

    // *** 회원가입 화면(resisterForm.jsp) 이동 ***
    @GetMapping("/resisterForm")
    public String resisterForm(Model m) throws Exception {

        List<AdministrativeDto> largeCategory = administrativeDao.selectLargeCategory();
        m.addAttribute("largeCategory", largeCategory);

        return "resisterForm";
    }

    @PostMapping("/createAccount")
    @Validated
    public String createAccount(@ModelAttribute @Valid UserDto inputUserDto, BindingResult result, String tradingPlace_A_small) throws Exception {
        if(result.hasErrors()) {
            return "resisterForm";
        }

        UserDto userDto = new UserDto(inputUserDto.getId(),
                inputUserDto.getPw(),
                inputUserDto.getName(),
                inputUserDto.getNick(),
                inputUserDto.getBirth(),
                inputUserDto.getGender(),
                inputUserDto.getPhone_num(),
                "",
                inputUserDto.getForeigner(),
                inputUserDto.getEmail(),
                'O',
                inputUserDto.getAddr_det(),
                new Timestamp(System.currentTimeMillis()),
                "",
                new Timestamp(System.currentTimeMillis()),
                ""
        );
        AdministrativeDto administrativeDto = (AdministrativeDto) administrativeDao.selectAddrCdByAddrCd(tradingPlace_A_small);
        AddrCdDto addrCdDto = new AddrCdDto();

        addrCdDto.setUr_id(userDto.getId());
        addrCdDto.setAddr_cd(administrativeDto.getAddr_cd());
        addrCdDto.setAddr_name(administrativeDto.getAddr_name());
        addrCdDto.setState('Y');
        addrCdDto.setFirst_date(new Timestamp(System.currentTimeMillis()));
        addrCdDto.setFirst_id("admin");
        addrCdDto.setLast_date(new Timestamp(System.currentTimeMillis()));
        addrCdDto.setLast_id("admin");
        userService.insertNewUser(userDto, addrCdDto);
        return "loginForm";
    }

    @GetMapping(value = "/checkIdDuplication", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String checkIdDuplication(@RequestParam("id") String id) {
        List<String> adminIdList = adminService.getAllAdminsId();
        List<String> AllUsersAdminsId = userService.getAllUsersAdminsId(adminIdList);

        if(id.trim().length() != id.length()) {
            return "아이디에 띄어쓰기를 넣을 수 없습니다.";
        }
        else if(hasMiddleSpace(id)) {
            return "아이디에 띄어쓰기를 넣을 수 없습니다.";
        }
        else if(isNumeric(id)) {
            return "아이디에는 반드시 영어와 숫자가 섞여있어야 합니다.";
        }
        else if(isAlphabetic(id)) {
            return "아이디에는 반드시 영어와 숫자가 섞여있어야 합니다.";
        }
        else if(containsSpecialCharacter(id)) {
            return "아이디에 한글 또는 특수문자는 불가능합니다.";
        }
        else {
            for(int i=0; i<AllUsersAdminsId.size(); i++) {
                if(id.equals(AllUsersAdminsId.get(i))) {
                    return "이미 존재하는 아이디입니다.";
                }
            }
        }

        return "사용 가능한 아이디입니다.";
    }

    String randomMailKey = "";

    // 1.1. 이메일로 본인 인증 절차 진행
    // 1.1.1. 입력받은 이메일 주소로 인증번호 전송
    // 1.1.1.2. 메일 전송 실패 시 다시 시도
    // 1.1.1.3. 메일 전송 성공 시 인증번호 입력란 출력
    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity<String> sendVerificationEmail(String email) {
        System.out.println("이메일 인증");
        try {
            if (userService.emailCheck(email)) {
                return new ResponseEntity<>("해당 이메일은 이미 사용 중입니다.", HttpStatus.BAD_REQUEST);
            } else {
                randomMailKey = mailService.sendMailToNonMember(email); // 이메일 주소로 인증번호 전송
                return new ResponseEntity<>("인증번호 전송 성공", HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("인증번호 전송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/verifyEmail")
    public ResponseEntity<String> verifyEmailAuthCode(String verificationCode) {
        try {
            if (verificationCode.equals(randomMailKey)) {
                return new ResponseEntity<>("인증번호가 일치합니다.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("잘못된 인증번호를 입력하셨습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("인증 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/mediumCategory", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<AdministrativeDto> mediumCategory(@RequestParam("tradingPlace_A_large") String largeCategory) throws Exception {
        return administrativeDao.selectMediumCategory(largeCategory);
    }

    @GetMapping(value = "/smallCategory", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<AdministrativeDto> smallCategory(@RequestParam("tradingPlace_A_medium") String mediumCategory) throws Exception {
        return administrativeDao.selectSmallCategory(mediumCategory);
    }

    public boolean hasMiddleSpace(String str) {
        return str.matches(".+\\s+.+");
    }

    public boolean isNumeric(String str) {
        return str.matches("^[0-9]+$");
    }

    public boolean isAlphabetic(String str) {
        return str.matches("^[A-Za-z]+$");
    }

    public boolean containsSpecialCharacter(String str) {
        return str.matches(".*[^A-Za-z0-9].*");
    }

}