package team.cheese.dao.Manage;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.cheese.domain.Manage.saleCategorysumdto;
import team.cheese.domain.event.EventDto;

import java.util.List;
import java.util.Map;

@Repository
public class ManageDao {
    @Autowired
    SqlSession sqlSession;

    private String namespace = "team.cheese.ManageMapper.";

    public List<EventDto> selectSoonEndEvent(){
        return sqlSession.selectList(namespace+"selectSoonEndEvent");
    }
    public List<saleCategorysumdto> countSaleCategory(){
        return sqlSession.selectList(namespace+"countsalecategory");
    }
    public int counttodayregister(){
        return sqlSession.selectOne(namespace+"counttodayRegister");
    }
}
