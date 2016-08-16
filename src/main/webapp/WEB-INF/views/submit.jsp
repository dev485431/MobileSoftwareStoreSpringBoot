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
            <div class="row">
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success">
                        <span class="bg-success">${successMessage}</span>
                    </div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger">
                        <span class="bg-danger">${errorMessage}</span>
                    </div>
                </c:if>
            </div>
            </br>

            <form:form modelAttribute="programForm" encType="multipart/form-data" role="form" method="POST"
                       cssClass="form">

                <div class="form-group">
                    <label for="name">Application name</label><br/>
                    <form:errors cssClass="bg-danger" path="name"/><br/>
                    <form:input path="name" type="text" value="${programForm.name}" cssClass="form-control"
                                placeholder="Application name"/>
                </div>
                <div class="form-group">
                    <label for="categoryId">Category</label><br/>
                    <form:errors cssClass="bg-danger" path="categoryId"/><br/>
                    <form:select path="categoryId">
                        <c:forEach var="category" items="${allCategories}">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </form:select>
                </div>
                <div class="form-group">
                    <label for="description">Description</label><br/>
                    <form:errors cssClass="bg-danger" path="description"/><br/>
                    <form:textarea path="description" value="${programForm.description}" cssClass="form-control"
                                   placeholder="Description"/>
                </div>
                <div class="form-group">
                    <label for="file">File
                        <small class="text-muted">* ** ***</small>
                    </label><br>
                    <form:errors cssClass="bg-danger" path="file"/>
                    <input type="file" name="file" id="file">
                </div>
                <button type="submit" class="btn btn-default">Submit</button>
            </form:form>
            <small class="text-muted">* Maximum program file size is <strong>${maxFileSizeKb} Kb</strong></small>
            </br>
            <small class="text-muted">** The program file must be a <strong>${uploadedFileExtension}</strong> file.It
                cannot be <strong>empty</strong>.</br>
                *** It must contain only the following files in the <u>main archive folder</u>:
                <strong>
                    <c:forEach var="requiredInnerFile" items="${requiredInnerFiles}">
                        ${requiredInnerFile}&nbsp;
                    </c:forEach>
                </strong>
            </small>
        </div>
    </div>
    <br/>

</div>
</div>

<%@include file="/WEB-INF/common/footer.jsp" %>













