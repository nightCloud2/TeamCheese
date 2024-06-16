<%--
  Created by IntelliJ IDEA.
  User: MY
  Date: 2024-06-10
  Time: 오전 11:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="kr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>관리자 페이지</title>
    <link rel="stylesheet" href="/css/ManageMain.css">
    <script src="https://d3js.org/d3.v7.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
</head>
<body>
<header class="header">
    <ul>
        <li>유저 관리</li>
        <li>이벤트 관리</li>
        <li>통계</li>
        <li>1대 1 문의 관리</li>
        <li>FAQ관리</li>
    </ul>
</header>
<div class="row">
    <div class="column">
        <div class="card primary">
            <h4>오늘의 가입자 수</h4>
            <p id="today-signups">0</p>
        </div>
    </div>
    <div class="column">
        <div class="card danger">
            <h4>오늘 탈퇴자 수</h4>
            <p id="today-cancellations">0</p>
        </div>
    </div>
</div>
<div class="row">
    <div class="column">
        <div class="card">
            <h4>답변 필요 1대 1 문의 글 목록</h4>
            <ul class="list-group" id="inquiry-list"></ul>
        </div>
    </div>
    <div class="column">
        <div class="card">
            <h4>곧 종료 되는 이벤트</h4>
            <ul class="list-group" id="event-list"></ul>
        </div>
    </div>
</div>
<div class="row">
    <div class="column">
        <div class="card">
            <h4>카테고리 별 조회 수</h4>
            <div id="category-chart"><div class="tooltip" id="tooltip"></div></div>
        </div>
    </div>
</div>
</div>

<script src="/js/ManageMain.js"></script>
</body>
</html>
