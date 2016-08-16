<%@include file="/WEB-INF/common/taglib.jsp" %>
<%@include file="/WEB-INF/common/header.jsp" %>

<div class="container" align="center">
    <h1>Our application encountered an error</h1><br/>
    Please contact the administrator<br/><br/>
    <a href="/">Go back to the main page</a>

</div>

<!--
Information for technical support:
Failed URL: ${url}
Exception: ${exception.message}
Cause: ${exception.cause}

Stack trace:
<c:forEach items="${exception.stackTrace}" var="printStackTrace">${printStackTrace}
</c:forEach>
-->

<%@include file="/WEB-INF/common/footer.jsp" %>
