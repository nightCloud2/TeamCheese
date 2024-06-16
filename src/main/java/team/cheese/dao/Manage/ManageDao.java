package team.cheese.dao.Manage;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import team.cheese.domain.event.EventDto;

import java.util.List;

@Repository
public class ManageDao {
    @Autowired
    SqlSession sqlSession;

    private String namespace = "team.cheese.ManageMapper.";

    public List<EventDto> selectSoonEndEvent(){
        return sqlSession.selectList(namespace+"selectSoonEndEvent");
    }
}
