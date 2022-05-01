<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
* {
    margin: 0;
    padding: 0;
}

html {
    font-size: 10px;
}

ul, li {
    list-style: none;
}

a {
    text-decoration: none;
    color: inherit;
}

.board_wrap {
    width: 1000px;
    margin: 100px auto;
}

.board_title {
    margin-bottom: 30px;
}

.board_title strong {
    font-size: 3rem;
}

.board_title p {
    margin-top: 5px;
    font-size: 1.4rem;
}

.bt_wrap {
    margin-top: 30px;
    text-align: center;
    font-size: 0;
}

.bt_wrap a {
    display: inline-block;
    min-width: 80px;
    margin-left: 10px;
    padding: 10px;
    border: 1px solid #000;
    border-radius: 2px;
    font-size: 1.4rem;
}

.bt_wrap a:first-child {
    margin-left: 0;
}

.bt_wrap a.on {
    background: #000;
    color: #fff;
}

.board_list {
    width: 100%;
    border-top: 2px solid #000;
}

.board_list > div {
    border-bottom: 1px solid #ddd;
    font-size: 0;
}

.board_list > div.top {
    border-bottom: 1px solid #999;
}

.board_list > div:last-child {
    border-bottom: 1px solid #000;
}

.board_list > div > div {
    display: inline-block;
    padding: 15px 0;
    text-align: center;
    font-size: 1.4rem;
}

.board_list > div.top > div {
    font-weight: 600;
}

.board_list .num {
    width: 10%;
}

.board_list .title {
    width: 60%;
    text-align: left;
}

.board_list .top .title {
    text-align: center;
}

.board_list .writer {
    width: 10%;
}

.board_list .date {
    width: 10%;
}

.board_list .count {
    width: 10%;
}

.board_page {
    margin-top: 30px;
    text-align: center;
    font-size: 0;
}

.board_page a {
    display: inline-block;
    width: 32px;
    height: 32px;
    box-sizing: border-box;
    vertical-align: middle;
    border: 1px solid #ddd;
    border-left: 0;
    line-height: 100%;
}

.board_page a.bt {
    padding-top: 10px;
    font-size: 1.2rem;
    letter-spacing: -1px;
}

.board_page a.num {
    padding-top: 9px;
    font-size: 1.4rem;
}

.board_page a.num.on {
    border-color: #000;
    background: #000;
    color: #fff;
}

.board_page a:first-child {
    border-left: 1px solid #ddd;
}

.board_view {
    width: 100%;
    border-top: 2px solid #000;
}

.board_view .title {
    padding: 20px 15px;
    border-bottom: 1px dashed #ddd;
    font-size: 2rem;
}

.board_view .info {
    padding: 15px;
    border-bottom: 1px solid #999;
    font-size: 0;
}

.board_view .info dl {
    position: relative;
    display: inline-block;
    padding: 0 20px;
}

.board_view .info dl:first-child {
    padding-left: 0;
}

.board_view .info dl::before {
    content: "";
    position: absolute;
    top: 1px;
    left: 0;
    display: block;
    width: 1px;
    height: 13px;
    background: #ddd;
}

.board_view .info dl:first-child::before {
    display: none;
}

.board_view .info dl dt,
.board_view .info dl dd {
    display: inline-block;
    font-size: 1.4rem;
}

.board_view .info dl dt {

}

.board_view .info dl dd {
    margin-left: 10px;
    color: #777;
}

.board_view .cont {
    padding: 15px;
    border-bottom: 1px solid #000;
    line-height: 160%;
    font-size: 1.4rem;
}

.board_write {
    border-top: 2px solid #000;
}

.board_write .title,
.board_write .info {
    padding: 15px;
}

.board_write .info {
    border-top: 1px dashed #ddd;
    border-bottom: 1px solid #000;
    font-size: 0;
}

.board_write .title dl {
    font-size: 0;
}

.board_write .info dl {
    display: inline-block;
    width: 50%;
    vertical-align: middle;
}

.board_write .title dt,
.board_write .title dd,
.board_write .info dt,
.board_write .info dd {
    display: inline-block;
    vertical-align: middle;
    font-size: 1.4rem;
}

.board_write .title dt,
.board_write .info dt {
    width: 100px;
}

.board_write .title dd {
    width: calc(100% - 100px);
}

.board_write .title input[type="text"],
.board_write .info input[type="text"],
.board_write .info input[type="password"] {
    padding: 10px;
    box-sizing: border-box;
}

.board_write .title input[type="text"] {
    width: 80%;
}

.board_write .cont {
    border-bottom: 1px solid #000;
}

.board_write .cont textarea {
    display: block;
    width: 100%;
    height: 300px;
    padding: 15px;
    box-sizing: border-box;
    border: 0;
    resize: vertical;
}

</style>

</head>
<body>

<header>
    <jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>
	
<main>
<div class="board_wrap">
        <div class="board_title">
            <strong>정보게시판</strong>
            <p>코딩에 관한 정보를 공유하는 게시판입니다.</p>
        </div>
        <div class="board_list_wrap">
            <div class="board_list">
                <div class="top">
                    <div class="num">번호</div>
                    <div class="title">제목</div>
                    <div class="writer">글쓴이</div>
                    <div class="date">작성일</div>
                    <div class="count">조회</div>
                </div>
                <div>
                    <div class="num">5</div>
                    <div class="title"><a href="view.html">글 제목이 들어갑니다.</a></div>
                    <div class="writer">김자바</div>
                    <div class="date">2022.4.24</div>
                    <div class="count">0</div>
                </div>
                <div>
                    <div class="num">4</div>
                    <div class="title"><a href="view.html">글 제목이 들어갑니다.</a></div>
                    <div class="writer">김자바</div>
                    <div class="date">2022.4.24</div>
                    <div class="count">0</div>
                </div>
                <div>
                    <div class="num">3</div>
                    <div class="title"><a href="view.html">글 제목이 들어갑니다.</a></div>
                    <div class="writer">김자바</div>
                    <div class="date">2022.4.24</div>
                    <div class="count">0</div>
                </div>
                <div>
                    <div class="num">2</div>
                    <div class="title"><a href="view.html">글 제목이 들어갑니다.</a></div>
                    <div class="writer">김자바</div>
                    <div class="date">2022.4.24</div>
                    <div class="count">0</div>
                </div>
                <div>
                    <div class="num">1</div>
                    <div class="title"><a href="view.html">글 제목이 들어갑니다.</a></div>
                    <div class="writer">김자바</div>
                    <div class="date">2022.4.24</div>
                    <div class="count">0</div>
                </div>
            </div>
            <div class="board_page">
                <a href="#" class="bt first">≪</a>
                <a href="#" class="bt prev">＜</a>
                <a href="#" class="num on">1</a>
                <a href="#" class="num">2</a>
                <a href="#" class="num">3</a>
                <a href="#" class="num">4</a>
                <a href="#" class="num">5</a>
                <a href="#" class="bt next">＞</a>
                <a href="#" class="bt last">≫</a>
            </div>
            <div class="bt_wrap">
                <a href="write.html" class="on">등록</a>
                <!--<a href="#">수정</a>-->
            </div>
        </div>
    </div>
<!--  
    <div class="body-container">
        <div class="row" style="padding-top: 15px; padding-bottom: 50px;">
        
        	<h3 style="text-align: center; padding: 25px; font-size: 30px;"> 정보게시판 </h3>
        
            <table class="table table-striped" style="text-align: center; border: 1px solid #dddddd">
            	<thead>
            		<tr>
            			<th style="background-color: #eeeeee; text-align: center;">번호</th>
            			<th style="background-color: #eeeeee; text-align: center;">제목</th>
            			<th style="background-color: #eeeeee; text-align: center;">작성자</th>
            			<th style="background-color: #eeeeee; text-align: center;">작성일</th>
            		</tr>
            	</thead>
            	<tbody>
            		<tr>
            			<td>1</td>
            			<td>안녕하세요</td>
            			<td>홍길동</td>
            			<td>2018-02-13</td>
            		</tr>
            		<tr>
            			<td>2</td>
            			<td>안녕하세요</td>
            			<td>홍길동</td>
            			<td>2018-02-13</td>
            		</tr>
            		<tr>
            			<td>3</td>
            			<td>안녕하세요</td>
            			<td>홍길동</td>
            			<td>2018-02-13</td>
            		</tr>
            		<tr>
            			<td>4</td>
            			<td>안녕하세요</td>
            			<td>홍길동</td>
            			<td>2018-02-13</td>
            		</tr>
            		<tr>
            			<td>5</td>
            			<td>안녕하세요</td>
            			<td>홍길동</td>
            			<td>2018-02-13</td>
            		</tr>
            		<tr>
            			<td>6</td>
            			<td>안녕하세요</td>
            			<td>홍길동</td>
            			<td>2018-02-13</td>
            		</tr>
            		
            	</tbody>
            
            </table>
        </div>
    </div>
-->
</main>
<footer>
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/></body>
</html>