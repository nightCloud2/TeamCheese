package team.cheese.controller.Manage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping(value = "/Manage")
public class ManageController {
    @GetMapping("")
    public String main() {
        return "Manage/MainPage";
    }

}
