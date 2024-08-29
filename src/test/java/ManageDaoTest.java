import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import team.cheese.dao.Manage.ManageDao;
import team.cheese.domain.Manage.saleCategorysumdto;
import team.cheese.domain.QnaDto;
import team.cheese.domain.event.EventDto;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class ManageDaoTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ManageDao manageDao;

    @Test
    public void selectEndEventTest() {
        Assert.assertNotNull(manageDao, "ManageDao는 null이 아니어야 합니다.");

        List<EventDto> dtolist = manageDao.selectSoonEndEvent();
        Assert.assertNotNull(dtolist, "반환된 리스트는 null이 아니어야 합니다.");
        Assert.assertFalse(dtolist.isEmpty(), "반환된 리스트는 비어 있지 않아야 합니다.");

        Date tomorrow = convertToDateViaInstant(LocalDate.now().plusDays(1));

        for (EventDto dto : dtolist) {
            Assert.assertEquals(dto.getE_date(), tomorrow, "이벤트 날짜는 내일이어야 합니다.");
        }
    }
    @Test
    public void selectCountsalecategory(){
        List<saleCategorysumdto>dtolist = manageDao.countSaleCategory();
        System.out.println(dtolist);
        Assert.assertTrue(dtolist.get(0).getName().equals("여성의류"));
    }
    @Test
    public  void Countregisterusertest(){
        System.out.println(manageDao.counttodayregister());
        Assert.assertTrue(manageDao.counttodayregister()!=-1);
    }
    @Test
    public  void selectQnaneedAnswerList(){
        List<QnaDto> dtolist = manageDao.selectNeedAnswerList(3);
        Assert.assertTrue(dtolist.size() == 3);
    }

    private Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
