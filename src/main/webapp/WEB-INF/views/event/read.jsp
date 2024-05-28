<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="../fixed/header.jsp"%>
<link rel="stylesheet" type="text/css" href="/css/event_read.css">

<main>
    <div class="wrapper">
    <h3 class="eventTitle">제목 : ${dto.title}</h3>
    <p class="eventContents">내용 : ${dto.contents}</p>
    <img src="/img/display?fileName=${dto.img_full_rt}" alt="이벤트 이미지" class="read_eventimg">
    <h5>날짜</h5>
    <p class="date"><fmt:formatDate value="${dto.s_date}" pattern="yyyy-MM-dd" /> ~ <fmt:formatDate value="${dto.e_date}" pattern="yyyy-MM-dd" /></p>
    <h5>경품</h5>
    <p class="prize">${dto.prize}</p>
    </div>
</main>
<%@include file="../fixed/footer.jsp"%>

