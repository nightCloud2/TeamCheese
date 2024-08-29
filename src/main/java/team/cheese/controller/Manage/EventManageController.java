package team.cheese.controller.Manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import team.cheese.domain.event.EventDto;
import team.cheese.entity.PageHandler;
import team.cheese.service.Manage.ManageService;
import team.cheese.service.event.EventService;

import java.io.IOException;

@Controller
@RequestMapping(value = "/Manage/event")
public class EventManageController {
    @Autowired
    private EventService eventService;

    @GetMapping("")
    public String eventManageList(Model model, Integer page, String cd){
        page= page==null?1:page;
        cd = cd==null?"":cd;

        model.addAttribute("eventarr", eventService.getPageList(page, cd, 10));
        model.addAttribute("ph", new PageHandler(eventService.count(cd), page));
        return "/Manage/manageEvent";
    }
    @GetMapping("write")
    public String write() {
        return "/Manage/manageEventWrite";
    }
    @PostMapping("write")
    public String write(EventDto dto) throws IOException {
        eventService.eventRegister(dto);
        return "redirect:/Manage/event";
    }
}
