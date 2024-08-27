<%--
  Created by IntelliJ IDEA.
  User: MY
  Date: 24. 7. 22.
  Time: 오후 3:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>이벤트 관리</title>
</head>
<body>
<header class="header">
    <ul>
        <li>유저 관리</li>
        <li><a href="/Manage/eventManageList">이벤트 관리</a></li>
        <li>통계</li>
        <li>1대 1 문의 관리</li>
        <li>FAQ</li>
    </ul>
</header>

<div class="nav">
    <a class="Allarr" href='/Manage/event?cd='>전체 이벤트</a>
    <a class="Beforearr" href='/Manage/event?cd=B'>시작전 이벤트</a>
    <a class="activearr" href='/Manage/event?cd=P'>활성 이벤트</a>
    <a class="finisharr" href='/Manage/event?cd=F'>종료 이벤트</a>
    <a class="cancelarr" href='/Manage/event?cd=C'>취소 이벤트</a>
    <a href="/Manage/write">작성</a>
    <div id="search">
        <form id="searchform" name="searchform" method="post">
            <fieldset>
                <label>검색 조건</label>
                <select id="user_select" name="cd" label="회원 분류">
                    <option value="title">제목</option>
                    <option value="contents">내용</option>
                    <option value="ad_id">작성자</option>
                </select>
                <input type= "text" id="inputcontents" name="contents" />
                <button id="submit">검색</button>
            </fieldset>
        </form>
    </div>
</div>
<c:forEach items="${eventarr}" var="event">
    <div class="imgbox">
        <a href="/Manage/read?evt_no=${event.evt_no}">
            <img src='${event.img_full_rt}' alt="">
        </a>
        <p class="title">${event.title}</p>
        <p class="date"><fmt:formatDate value="${event.s_date}" pattern="yyyy-MM-dd" />
            ~<fmt:formatDate value="${event.e_date}" pattern="yyyy-MM-dd" />
        </p>
    </div>
</c:forEach>
<c:if test="${ph.prevPage}">
    <a href="Manage/event?page=1&cd=${cd}"><<</a>
    <a href="Manage/event?page=${ph.offset-1}&cd=${cd}"><</a>
</c:if>
<c:forEach var="page" begin="${ph.beginPage}" end="${ph.endPage}">
    <a href="Manage/event?page=${page}&cd=${cd}">${page}</a>
</c:forEach>
<!-- Add more page links here -->
<c:if test="${ph.nextPage}">
    <a href="/Manage/event?page=${ph.getEndPage()+1}&cd=${cd}">></a>
    <a href="/Manage/event?page=${ph.getMaxPageNum()}&cd=${cd}">>></a>
</c:if>
</body>
</html>
