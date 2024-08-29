package team.cheese.service.Manage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.cheese.dao.Manage.ManageDao;
import team.cheese.domain.Manage.saleCategorysumdto;
import team.cheese.domain.QnaDto;
import team.cheese.domain.event.EventDto;

import java.util.List;

@Service
public class ManageService {
    public static int ANSWERLISTLIMIT = 3;
    @Autowired
    private ManageDao manageDao;

    public List<EventDto> getSoonEndList(){
        return manageDao.selectSoonEndEvent();
    }
    public List<saleCategorysumdto> getCartImpo(){
        return manageDao.countSaleCategory();
    }
    public int gettodayRegisterUsercnt(){
        return manageDao.counttodayregister();
    }
    public List<QnaDto> getAnserList(){
        return manageDao.selectNeedAnswerList(ANSWERLISTLIMIT);
    }
    public int gettodayQuitUsercnt(){
        return manageDao.counttodayQuit();
    }
}
