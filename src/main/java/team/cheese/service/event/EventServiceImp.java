package team.cheese.service.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.cheese.dao.event.EventDaoImp;
import team.cheese.domain.event.EventDto;
import team.cheese.service.ImgService;
import team.cheese.service.ImgServiceImpl;

import java.io.IOException;
import java.util.*;

@Service
public class EventServiceImp implements EventService {
    @Autowired
    EventDaoImp dao;

    @Autowired
    ImgService imgService;

    @Override
    public ArrayList<EventDto> getAlllist(){
        ArrayList<EventDto> dtolist = new ArrayList<>(dao.selectAll());
        return dtolist;
    }
    @Override
    public int count(String nowcd){
        return dao.count(nowcd);
    }
    @Override
    public int searchCount(String cd, String contents){
        Map map = new HashMap();
        map.put("searchContent", contents);
        map.put("searchCd", cd);
        return dao.searchCount(map);
    }
    @Override
    public  ArrayList<EventDto> getPageList(int startnum, String cd, int maxcontents){
        ArrayList<EventDto> dtolist = new ArrayList<>(dao.selectPage(startnum, cd, maxcontents));
        return dtolist;
    }
    @Override
    public List<EventDto> getSearchList(String cd, String contents, int startnum){
        Map map = new HashMap();
        map.put("searchCd", cd);
        map.put("searchContent", contents);
        map.put("startnum", startnum);
        List<EventDto> arr = dao.selectSearch(map);
        return arr;
    }
    @Override
    public int eventRegister(EventDto dto) throws IOException {
        String S_Cd = isWithinRange(dto.getS_date(),dto.getE_date());
        dto.setActive_s_cd(S_Cd);

        int result=dao.insert(dto);
//        String fullrt = imgService.reg_img_one(imgname);
//        dto.setImg_full_rt(fullrt);

        return result;
    }
    @Override
    public EventDto getContent(Long evt_no){
        return dao.selectContent(evt_no);
    }

    @Override
    public int changeState(EventDto dto){

        return dao.updatestate(dto);
    }
    @Override
    public int modifyEvent(EventDto dto){
        String S_Cd = isWithinRange(dto.getS_date(),dto.getE_date());
        dto.setActive_s_cd(S_Cd);
        return dao.updateContent(dto);
    }

    public String isWithinRange(Date startDate ,Date endDate){
        Date nowDate = new Date();
        if(nowDate.after(startDate)&& nowDate.before(endDate))
            return "P";
        if(nowDate.before(startDate))
            return "B";
        else
            return "F";
    }
}