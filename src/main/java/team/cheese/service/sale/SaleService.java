package team.cheese.service.sale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.cheese.dao.AdministrativeDao;
import team.cheese.dao.SaleCategoryDao;
import team.cheese.dao.SaleDao;
import team.cheese.dao.TagDao;
import team.cheese.domain.SaleDto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {
    @Autowired
    SaleDao saleDao;
    @Autowired
    SaleCategoryDao saleCategoryDao;
    @Autowired
    AdministrativeDao administrativeDao;

    @Autowired
    TagDao tagDao;

    // 전체 게시글 수 count
    public int getCount() throws Exception {
        // 총 작성된 글을 count해주어야 됨
        return 0;
    }

    // 판매자가 자신의 게시글을 삭제할 때
    public int remove(BigInteger no, String writer) throws Exception {
        // 현재 상태를 'N'으로 변경해주는거 여기서 처리
        return 0;
    }

    // 판매자가 판매 게시글을 작성할 때
    public int write(Map<String, Object> map) throws Exception {
        // insert 해주는 거 여기서 처리

        // 1. 필수로 들어와야 되는 값 체크
        /*  addr_cd(행정동코드), addr_name(주소명),
        *   seller_id(판매자id), seller_nick(판매자명),
        *   sal_i_cd(판매카테고리), sal_name(판매카테고리명),
        *   group_no(이미지그룹번호), img_full_rt(이미지루트),
        *   pro_s_cd(사용감), tx_s_cd(거래방법),
        *   trade_s_cd_1(거래방식), title(제목), contents(내용),
        *   price(가격), bid_cd(가격제시/나눔신청여부)
        */
        //     1.1. 값이 들어와 있지 않으면 rollback

        // 2. sale 테이블에 insert

        // 3. sale_history 테이블에 insert
        //     3.1. 실패하면 rollback

        // 4. tag테이블에 tag정보 저장
        // 5. saleTag테이블에 교차정보 저장

        SaleDto saleDto = (SaleDto) map.get("saleDto");
        System.out.println("service write: " + saleDto);
        List<String> tagList = (List<String>) map.get("tagList");
        for (String contents : tagList) {
            System.out.println("service tag contents : " + contents);
//            2.2.2.3.2. tagDao에 해당하는 contents값이 존재하는지 확인

//            - 값이 존재하는 경우
//            - tagDao의 no값 반환
//            - 값이 존재하지 않는 경우
//                    - tagDao의 insert문 수행

        }

        System.out.println("insert 성공 : " + saleDao.insert(saleDto));
        return saleDao.insert(saleDto);
    }

    // 전체 게시글 list를 가지고 올 때
    public List<SaleDto> getList(String addr_cd) throws Exception {
        List<SaleDto> saleList = saleDao.selectAll(addr_cd);
        System.out.println(saleList.size());

        return saleList;
    }

    // 판매글 하나에 들어가서 게시글을 읽을 때
    public SaleDto read(Long no) throws Exception {
        // 판매글 번호를 넘겨 받아서 Dao에서 select로 처리
        SaleDto saleDto = saleDao.select(no);

        return saleDto;
    }

    // 판매 게시글을 수정할 때
    public int modify(SaleDto saleDto) throws Exception {
        // 판매글 내용을 받아서 수정하도록 처리
        return saleDao.update(saleDto);
    }
}
