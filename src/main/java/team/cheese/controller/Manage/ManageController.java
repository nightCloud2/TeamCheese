package team.cheese.controller.Manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.cheese.domain.Manage.saleCategorysumdto;
import team.cheese.domain.QnaDto;
import team.cheese.domain.event.EventDto;
import team.cheese.entity.PageHandler;
import team.cheese.service.Manage.ManageService;
import team.cheese.service.event.EventService;

import java.util.List;

@Controller()
@RequestMapping(value = "/Manage")
public class ManageController {
    @Autowired
    private ManageService manageService;


    @GetMapping("")
    public String main(Model model) {
        model.addAttribute(manageService.getSoonEndList());
        return "/Manage/MainPage";
    }

    @GetMapping("/getAnswerList")
    @ResponseBody
    public List<QnaDto> getAnserList(){
        return manageService.getAnserList();
    }

    @GetMapping("/getEventList")
    @ResponseBody
    public List<EventDto> getEventList(){
        return manageService.getSoonEndList();
    }

    @GetMapping("/getCharImfo")
    @ResponseBody
    public List<saleCategorysumdto> getchartImfo(){
        return manageService.getCartImpo();
    }

    @GetMapping("/todayRegisterUser")
    @ResponseBody
    public int gettodayRegisterUser(){
        return manageService.gettodayRegisterUsercnt();
    }

    @GetMapping("/todayQuitUser")
    @ResponseBody
    public int gettodayQuitUser(){
        return manageService.gettodayQuitUsercnt();
    }


}
