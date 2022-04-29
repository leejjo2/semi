<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>공지사항</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">

.body-container {
    margin: 0 auto 15px;
    width: 700px;
    min-height: 450px;
}

.body-title {
    color: #424951;
    padding-top: 25px;
    padding-bottom: 5px;
    margin: 0 0 25px 0;
}

.body-title h3 {
    font-size: 23px;
    min-width: 300px;
    font-family: "Malgun Gothic", "맑은 고딕", NanumGothic, 나눔고딕, 돋움, sans-serif;
    font-weight: bold;
    margin: 0 0 -5px 0;
    padding-bottom: 5px;
    display: inline-block;
}

.table-form textarea {
    display: block;
    width: 100%;
    height: 300px;
    padding: 15px;
    box-sizing: border-box;
    border: 0px;
}


.table-list thead > tr:first-child{
	background: #f8f8f8;
}
.table-list th, .table-list td {
	text-align: center;
}
.table-list .left {
	text-align: left; padding-left: 5px; 
}

.table-list .num {
	width: 60px; color: #787878;
}
.table-list .subject {
	color: #787878;
}
.table-list .name {
	width: 100px; color: #787878;
}
.table-list .date {
	width: 100px; color: #787878;
}
.table-list .hit {
	width: 70px; color: #787878;
}
.table-list .file {
	width: 50px; color: #787878;
}
.table-list .notice {
	display: inline-block; padding: 1px 3px; background: #ed4c00; color: #fff;
}

.btn {
    text-align: center;
    display: inline-block;
    min-width: 50px;
    padding: 5px;
    border: 1px solid #000;
    border-radius: 2px;
    font-size: inherit;
}

</style>
<script type="text/javascript">
<c:if test="${sessionScope.member.userId==dto.userId || sessionScope.member.userId=='admin'}">
function deleteNotice() {
	if(confirm('게시글을 삭제하시겠습니까?')){
		let query = "num=${dto.num}&${query}";
		let url = "${pageContext.request.contextPath}/notice/delete.do?"+query;
		location.href=url;
	}
}
</c:if>

</script>
</head>
<body>
<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>

<main>
	<div class="body-container" style="width: 700px;">
		
		<div class="body-title">
			<h3> 공지사항 </h3>
			<p>필독을 권유합니다.</p>
		</div>
        
		<table class="table table-border table-article">
			<thead>
				<tr>
					<td colspan="2" align="center">
						${dto.subject }
					</td>
				</tr>
			</thead>
			
			<tbody>
				<tr>
					<td width="50%">
						이름 : ${dto.userName }
					</td>
					<td align="right">
						${dto.reg_date } | 조회수 ${dto.hitCount }
					</td>
				</tr>
				
				<tr>
					<td colspan="2" valign="top" height="200">
						${dto.content }
					</td>
				</tr>
				
				<c:forEach var="vo" items="${listFile}">
					<tr>
						<td colspan="2">
							첨부파일
							<a href="${pageContext.request.contextPath}/notice/download.do?fileNum=${vo.numFile}">${vo.originalFilename }</a>
						</td>
					</tr>
				</c:forEach>
				<tr>
					<td colspan="2">
						이전글 : 
						<c:if test="${not empty preReadNotice }">
							<a href="${pageContext.request.contextPath }/notice/article.do?${query}&num=${preReadNotice.num}">${preReadNotice.subject }</a>
						</c:if>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						다음글 :
						<c:if test="${not empty nextReadNotice }">
							<a href="${pageContext.request.contextPath }/notice/article.do?${query}&num=${nextReadNotice.num}">${nextReadNotice.subject }</a>
						</c:if> 
					</td>
				</tr>
			</tbody>
		</table>
		
		<table class="table">
			<tr>
				<td width="50%">
				
					<c:choose>
						<c:when test="${sessionScope.member.userId=='admin' }">
							<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/notice/update.do?num=${dto.num}&page=${page}';" >수정</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn" disabled="disabled">수정</button>
						</c:otherwise>
					</c:choose>
					
					<c:choose>
						<c:when test="${sessionScope.member.userId=='admin' }">
							<button type="button" class="btn" onclick="deleteNotice();">삭제</button>
						</c:when>
						<c:otherwise>
							<button type="button" class="btn" disabled="disabled">삭제</button>
						</c:otherwise>
					</c:choose>
				</td>
				<td align="right">
					<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/notice/list.do?${query}';">리스트</button>
				</td>
			</tr>
		</table>
        
	</div>
</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>
<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>