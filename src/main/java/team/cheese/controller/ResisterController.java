package team.cheese.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import team.cheese.service.UserService;
import team.cheese.entity.UserVaildation;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

@Controller
public class ResisterController {

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
    public String createAccount(@ModelAttribute @Valid UserDto userDto, BindingResult result, String addr_cd, String addr_nm) throws NoSuchAlgorithmException {
        if(result.hasErrors())
            return "resisterForm";
        AddrCdDto addrCdDto = new AddrCdDto();
        addrCdDto.setUr_id(userDto.getId());
        addrCdDto.setAddr_cd(addr_cd);
        addrCdDto.setAddr_name(addr_nm);
        userService.insertNewUser(userDto, addrCdDto);
        return "loginForm";
    }

    @GetMapping(value = "/checkIdDuplication", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String checkIdDuplication(@RequestParam("id") String id) {
        List<String> adminIdList = adminService.getAllAdminsId();
        List<String> AllUsersAdminsId = userService.getAllUsersAdminsId(adminIdList);
        String answer=UserVaildation.vailed(id);
        for(int i=0; i<AllUsersAdminsId.size(); i++) {
            if(id.equals(AllUsersAdminsId.get(i))) {
                answer = "이미 존재하는 아이디입니다.";
            }
        }
        return answer;
    }

    @GetMapping(value = "/mediumCategory", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public List<AdministrativeDto> mediumCategory(@RequestParam("tradingPlace_A_large") String largeCategory) throws Exception {
        return administrativeDao.selectMediumCategory(largeCategory);
    }
}