package team.cheese.controller.sale;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team.cheese.dao.*;
import team.cheese.domain.AddrCdDto;
import team.cheese.domain.AdministrativeDto;
import team.cheese.domain.SaleCategoryDto;
import team.cheese.domain.SaleDto;
import team.cheese.service.sale.SaleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping(value = "/sale")
public class SaleController {
    @Autowired
    SaleDao saleDao;
    @Autowired
    SaleCategoryDao saleCategoryDao;
    @Autowired
    AdministrativeDao administrativeDao;
    @Autowired
    TagDao tagDao;
    @Autowired
    AddrCdDao addrCdDao;

    @Autowired
    SaleService saleService;

    @Autowired
    TestSession testSession;

    // 전체 게시글을 보는 경우
    @RequestMapping("/list")
    public String getList(Model model, HttpSession session) throws Exception {
        System.out.println("/list들어옴");
        // 1. 세션에서 정보를 불러옴
        //  1.1. 로그인 한 경우
        //    1.1.1. 유저의 로그인 정보 첫번째 주소를 불러옴
        //  1.2. 로그인 하지 않은 경우
        //    1.2.1. addr_cd값을 null로 전달

        // TestSession 클래스를 사용하여 세션을 설정
        String ur_id = "asdf";
        session = testSession.setSession(ur_id, session);

        ur_id = (String) session.getAttribute("userId");
        System.out.println("ur_id : " + ur_id);

        if(ur_id == null) {
            System.out.println("ur_id가 Null인 경우");
            model.addAttribute("addrCdList", administrativeDao.selectAll());
            List<SaleDto> saleList = saleService.getList();
            System.out.println(saleList.size());
            model.addAttribute("saleList", saleList);
        } else {
            System.out.println("ur_id가 NOTNull인 경우");
            // 세션에서 ID 값을 가지고 옴
            List<AddrCdDto> addrCdList = (List<AddrCdDto>) session.getAttribute("userAddrCdDtoList");
            System.out.println("addrCdList 확인 : " + addrCdList);
            String addr_cd = addrCdList.get(0).getAddr_cd();
            System.out.println("addr_cd" + addr_cd);
            List<SaleDto> saleList = saleService.getUserAddrCdList(addr_cd);
            System.out.println(saleList.size());

            // 사용자의 기본 주소 첫번째
            model.addAttribute("addrCdList", addrCdList);
            // 불러온 게시글 리스트를 모델에 담음
            model.addAttribute("saleList", saleList);
        }
        return "/saleList";
    }

    // 게시글 리스트 중 하나를 클릭한 경우
    @RequestMapping("/read")
    public String read(Long no, Model model, HttpSession session) throws Exception {
        SaleDto saleDto = saleService.read(no);
        System.out.println(saleDto);

        model.addAttribute("Sale", saleDto); // model로 값 전달

        return "/login/saleBoard";
    }

    // 글쓰기 버튼 누른 경우
    @GetMapping("/write")
    public String write(@RequestParam String addr_cd, Model model, HttpSession session) throws Exception {
        System.out.println("GET write");
        // 로그인 한 경우

        String ur_id = (String) session.getAttribute("userId");

        List<AddrCdDto> addrCdDtoList = (List<AddrCdDto>) session.getAttribute("userAddrCdDtoList");
        String addr_name = "";
        for(AddrCdDto addrCdDto : addrCdDtoList) {
            if (addr_cd.equals(addrCdDto.getAddr_cd())) {
                addr_name = addrCdDto.getAddr_name();
            }
        }

        if(ur_id != null) {
            SaleDto saleDto = new SaleDto(addr_cd, addr_name);
            model.addAttribute("Sale", saleDto);
            model.addAttribute("saleCategory1", saleCategoryDao.selectCategory1());
            return "/login/saleWrite";
        } else {
            // 로그인 안한 경우
            return "home";
        }
    }

    // 수정하기 버튼을 눌렀을 때 글을 받아서 jsp로 전달
    @PostMapping("/modify")
    public String modify(@RequestParam Long no, Model model, HttpSession session, HttpServletRequest request) throws Exception {

        Map map = saleService.modify(no);
        SaleDto saleDto = (SaleDto) map.get("saleDto");
        String tagContents = (String) map.get("tagContents");

        model.addAttribute("Sale", saleDto);
        model.addAttribute("Tag", tagContents);
        model.addAttribute("saleCategory1", saleCategoryDao.selectCategory1());

        return "/login/saleWrite";
    }

    // 서비스로 분리
    // 글쓰기 완료하고 글을 등록하는 경우
    @PostMapping("/write")
    @ResponseBody
    public ResponseEntity<String> write(@Valid @RequestBody Map<String, Object> map, Model model, HttpSession session, HttpServletRequest request) throws Exception {
        System.out.println("POST write");
        // service 호출
        // 서비스단 작성 필요함
//        System.out.println(formData);

        // 1. 사용자가 글작성
        // 동시에 작성버튼 누르면?
        // 작성 실패하면?
        // 필수로 써야되는거 안썼으면? -> view에서 여기로 전송못하게하기

        String seller_id = (String) session.getAttribute("userId");
        String seller_nick = (String) session.getAttribute("userNick");

        // ObjectMapper : JSON 형태를 JAVA 객체로 변환
        System.out.println(map.get("sale"));
        System.out.println(map.get("tag"));
        ObjectMapper objectMapper = new ObjectMapper();
        SaleDto saleDto = objectMapper.convertValue(map.get("sale"), SaleDto.class);


        saleDto.setAddrSeller(seller_id, seller_nick);
        System.out.println("값 들어왔는지 확인 : " + saleDto);

        Map<String, Object> tagMap = (Map<String, Object>) map.get("tag");
        List<String> tagContents = (List<String>) tagMap.get("contents");
        System.out.println("tag값 확인 : " + tagMap.size());

        // 각 해시태그를 반복하여 TagDto 객체 생성 및 tagList에 추가
        List<String> tagList = new ArrayList<>();
        for (String content : tagContents) {
            // '#' 기호를 제거하여 태그 내용만 추출
            String tagContent = content.substring(1);
            // TagDto 객체 생성
            // 생성된 TagDto를 tagList에 추가
            tagList.add(tagContent);
        }

        Map mapDto = new HashMap();
        mapDto.put("saleDto", saleDto);
        mapDto.put("tagList", tagList);

        // Service를 통해 글 등록 처리
        Long sal_no = saleService.write(mapDto);

        System.out.println("글 번호 : " + sal_no);
        String page = "/sale/read?no=" + sal_no;

        // 등록 후에는 다시 글 목록 페이지로 리다이렉트
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

// 글 수정을 완료하고 글을 등록하는 경우
@PostMapping("/write")
@ResponseBody
public ResponseEntity<String> update(@Valid @RequestBody Map<String, Object> map, Model model, HttpSession session, HttpServletRequest request) throws Exception {
    System.out.println("POST update");

    String seller_id = (String) session.getAttribute("userId");
    String seller_nick = (String) session.getAttribute("userNick");

    // ObjectMapper : JSON 형태를 JAVA 객체로 변환
    System.out.println(map.get("sale"));
    System.out.println(map.get("tag"));
    ObjectMapper objectMapper = new ObjectMapper();
    SaleDto saleDto = objectMapper.convertValue(map.get("sale"), SaleDto.class);


    saleDto.setAddrSeller(seller_id, seller_nick);
    System.out.println("값 들어왔는지 확인 : " + saleDto);

    Map<String, Object> tagMap = (Map<String, Object>) map.get("tag");
    List<String> tagContents = (List<String>) tagMap.get("contents");
    System.out.println("tag값 확인 : " + tagMap.size());

    // 각 해시태그를 반복하여 TagDto 객체 생성 및 tagList에 추가
    List<String> tagList = new ArrayList<>();
    for (String content : tagContents) {
        // '#' 기호를 제거하여 태그 내용만 추출
        String tagContent = content.substring(1);
        // TagDto 객체 생성
        // 생성된 TagDto를 tagList에 추가
        tagList.add(tagContent);
    }

    Map mapDto = new HashMap();
    mapDto.put("saleDto", saleDto);
    mapDto.put("tagList", tagList);

    // Service를 통해 글 등록 처리
    Long sal_no = saleService.update(mapDto);

    System.out.println("글 번호 : " + sal_no);
    String page = "/sale/read?no=" + sal_no;

    // 등록 후에는 다시 글 목록 페이지로 리다이렉트
    return new ResponseEntity<>(page, HttpStatus.OK);
}

    @RequestMapping("/remove")
    public ResponseEntity<String> remove(@RequestParam Long no, Model model, HttpSession session) throws Exception {
        String seller_id = (String) session.getAttribute("userId");
        saleService.remove(no, seller_id);

        // 등록 후에는 다시 글 목록 페이지로 리다이렉트
        String page = "/sale/list";
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

//  ajax 요청을 처리해주는 URL등
    @PostMapping("/saleCategory2")
    @ResponseBody
    public ResponseEntity<List<SaleCategoryDto>> getSaleCategory2(@RequestParam String category1, Model model) throws Exception {
        try{
            return new ResponseEntity<>(saleCategoryDao.selectCategory2(category1), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ajax 판매 카테고리 처리(중분류)
    @RequestMapping("/saleCategory3")
    @ResponseBody
    public  ResponseEntity<List<SaleCategoryDto>> getSaleCategory3(@RequestParam String category2, Model model) throws Exception {
        try{
            return new ResponseEntity<>(saleCategoryDao.selectCategory3(category2), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ajax 판매 카테고리 처리(소분류)
    @RequestMapping("/searchLetter")
    @ResponseBody
    public ResponseEntity<List<AdministrativeDto>> getAdministrative(@RequestParam String searchLetter, Model model) throws Exception {
        // 검색어를 이용하여 판매글을 검색
        try{
            return new ResponseEntity<>(administrativeDao.searchLetter(searchLetter), HttpStatus.OK);
        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}