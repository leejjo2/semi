<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>spring</title>
<jsp:include page="/WEB-INF/views/layout/staticHeader.jsp"/>
<style type="text/css">
.boardname { /* "소통 공간" 부분 */
	text-align: center;
	font-size: 16px;
	margin-bottom: 15px;
	padding: 10px;
}

.table-form td {
	padding: 7px 0;
}
.table-form p {
	line-height: 200%;
}
.table-form tr:first-child {
	border-top: 2px solid #212529; 
}
.table-form tr > td:first-child {
	width: 110px; text-align: center; background: #f8f8f8;
}
.table-form tr > td:nth-child(2) {
	padding-left: 10px;
}

.table-form tr:nth-child(4) > td {
	background: #fff;
}
.table-form input[type=text], .table-form input[type=file], .table-form textarea { /* 제목, 파일 */
	width: 96%;
}

.table-form input[type=file], .table-form textarea { /* 파일만? */
	border: none; 
}

.content {
	min-height: 300px;
}

</style>

<script type="text/javascript">
function sendBoard() {
    const f = document.boardForm;
	let str;
	
    str = f.title.value.trim();
    if(!str) {
        alert("제목을 입력하세요.");
        f.title.focus();
        return;
    }

    str = f.content.value.trim();
    if(!str) {
        alert("내용을 입력하세요.");
        f.content.focus();
        return;
    }

    f.action = "${pageContext.request.contextPath}/freeBoard/${mode}_ok.do";
    f.submit();
}

</script>
</head>
<body>

<header>
	<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</header>
	
<main>
	<div class="body-container" style="width: 900px; text-align: center; margin: auto;">
		<div class="boardname">
			<h3><i class="fas fa-clipboard-list"></i> 소통 공간 </h3>
		</div>
        
		<form name="boardForm" method="post" >
			<table class="table table-border table-form">
			
				<tr> 
					<td>작성자</td>
					<td align="left"> 
						<p>&nbsp;&nbsp;&nbsp;${sessionScope.member.userId}</p>
					</td>
				</tr>
				
				<tr> 
					<td>제&nbsp;&nbsp;&nbsp;&nbsp;목</td>
					<td> 
						<input type="text" name="title" maxlength="100" placeholder="제목을 입력하세요"
							class="form-control" value="${dto.title}">
					</td>
				</tr>
				
				<tr> 
					<td colspan="2"> 
						<textarea name="content" class="content" placeholder="내용을 입력하세요">${dto.content}</textarea>
					</td>
				</tr>
				
			</table>
				
			<table class="table">
				<tr> 
					<td align="center">
						<button type="button" class="btn" onclick="sendBoard();">${mode=="update"?"수정완료":"등록하기"}</button>
						<button type="reset" class="btn">다시입력</button>
						<button type="button" class="btn" onclick="location.href='${pageContext.request.contextPath}/freeBoard/list.do';">${mode=="update"?"수정취소":"등록취소"}</button>
						<c:if test="${mode=='update'}">
							<input type="hidden" name="num" value="${dto.num}">
							<input type="hidden" name="page" value="${page}">
						</c:if>
					</td>
				</tr>
			</table>
	
		</form>

        
	</div>
</main>

<footer>
    <jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>

<jsp:include page="/WEB-INF/views/layout/staticFooter.jsp"/>
</body>
</html>