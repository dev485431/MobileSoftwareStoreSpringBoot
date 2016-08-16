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

        <div class="row text-align-center">
            <h2>${programDetails.name}</h2>
        </div>
        <br/>
        <div class="row">

            <img src="${programDetails.img512Url}" class="img-thumbnail img512">

            ${programDetails.description}

        </div>
        <br/>

        <div class="row text-align-center">Average user rating: <strong>${programDetails.averageRating}</strong></div>
        <c:if test="${not empty pageContext.request.userPrincipal}">
            <br/>
            <div class="row text-align-center">Rate this app:</div>
            <div id="rating-msg" class="row text-align-center"></div>
            <div data-program-id="${programDetails.id}" id="rating" class="row center-block"></div>
        </c:if>
        <br/>

        <div class="row text-align-center">
            <p></p>
            <img src="${serviceServerPath}/resources/images/download.gif"
                 alt=""/> Downloads: ${programDetails.downloads}
            </span>
            <span>
                    <img src="${serviceServerPath}/resources/images/category.gif"
                         alt=""/> Category: ${programDetails.categoryName}
                </span>
            <span>
                    <img src="${serviceServerPath}/resources/images/category.gif"
                         alt=""/> Time uploaded: ${programDetails.timeUploaded}
                </span>
            </p>
        </div>

        <div class="row text-align-center">
            <h3><a href="/download/${programDetails.id}">Download</a></h3>
        </div>
        <br/>

        <div class="row text-align-center">
            <a href="/">Back to main page</a>
        </div>
        <br/>

    </div>

</div>

<%@include file="/WEB-INF/common/footer.jsp" %>
