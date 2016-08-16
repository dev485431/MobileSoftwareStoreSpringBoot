<%@include file="/WEB-INF/common/taglib.jsp" %>
<%@include file="/WEB-INF/common/header.jsp" %>

<div class="container">
    <!-- Most popular -->
    <div class="row">
        <h2>Most Popular</h2>
    </div>

    <div class="row">
        <div id="top-downloads"></div>
    </div>

    <div class="row">
        <!-- Categories -->
        <div class="col-xs-5 col-md-3">
            <div class="row">
                <h4><a href="/">Home</a></h4>

                <c:choose>
                    <c:when test="${empty pageContext.request.userPrincipal}">
                        <h4><a href="/login">Login</a></h4>
                    </c:when>
                    <c:otherwise>
                        <h4><a href="/logout">Logout</a></h4>
                    </c:otherwise>
                </c:choose>
                <sec:authorize access="hasRole('ROLE_ADMIN') or hasRole('ROLE_DEVELOPER')">
                    <h4><a href="/submit">Submit new program</a></h4>
                </sec:authorize>
            </div>

            <div class="row">
                <h2>Categories</h2>
                <c:forEach items="${allCategories}" var="category">
                    <a href="${serviceServerPath}/?categoryId=${category.id}">${category.name}</a><br>
                </c:forEach>
            </div>
            <br/><br/>
        </div>

        <!-- Content -->
        <div class="col-xs-12 col-sm-6 col-md-8">

            <c:if test="${param.error != null}">
                <div class="alert alert-danger">
                    <p>Invalid username and password.</p>
                </div>
            </c:if>
            <c:if test="${param.logout != null}">
                <div class="alert alert-success">
                    <p>You have been logged out successfully.</p>
                </div>
            </c:if>

            <div class="form-group cem">
                <c:url var="loginUrl" value="/login"/>
                <form action="${loginUrl}" method="post" class="form">

                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="username"><i class="glyphicon glyphicon-user"></i></label>
                        <input type="text" class="form-control" id="username" name="username"
                               placeholder="Enter Username">
                    </div>

                    <div class="input-group input-sm">
                        <label class="input-group-addon" for="password"><i class="glyphicon glyphicon-lock"></i></label>
                        <input type="password" class="form-control" id="password" name="password"
                               placeholder="Enter Password">
                    </div><br/>

                    <div class="input-group center-block">
                        <input type="submit"
                               class="btn btn-block btn-primary btn-default" value="Log in">
                    </div>
                </form>

            </div>

        </div>
    </div>
    <br/>

</div>
</div>

<%@include file="/WEB-INF/common/footer.jsp" %>













