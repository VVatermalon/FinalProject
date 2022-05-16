<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 27.01.2022
  Time: 22:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<c:choose>
    <c:when test="${not empty language}"> <fmt:setLocale value="${language}" scope="session"/></c:when>
    <c:when test="${empty language}"> <fmt:setLocale value="${language = 'ru_RU'}" scope="session"/></c:when>
</c:choose>
<fmt:setBundle basename="language.language"/>
<fmt:message key="alt.catalog" var="alt_catalog"/>
<fmt:message key="header.music" var="music"/>
<fmt:message key="header.merch" var="merch"/>
<fmt:message key="header.lisa" var="Lisa"/>
<fmt:message key="header.rose" var="Rose"/>
<fmt:message key="header.the_show" var="TheShow"/>
<fmt:message key="header.the_album" var="TheAlbum"/>
<fmt:message key="header.figures" var="figures"/>
<fmt:message key="header.language" var="lang"/>
<fmt:message key="header.basket" var="basket"/>
<fmt:message key="header.profile" var="profile"/>
<fmt:message key="header.sign_in" var="sign_in"/>
<fmt:message key="header.sign_out" var="sign_out"/>
<html>
<head>
    <script>
        function disableBack() {
            window.history.forward();
        }
        setTimeout("disableBack()", 0);
        window.onunload = function() {
            null
        };
    </script>

    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/CSS/styles.css">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark" style="height: 100px">
    <div class="container-fluid" style="height: 100px">
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavDarkDropdown" aria-controls="navbarNavDarkDropdown" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNavDarkDropdown">
            <ul class="navbar-nav">
                <li class="nav-item"><a class="nav-link" href="${absolutePath}/controller?command=find_by_category_items&category_id=1">${music}</a></li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="navbarDarkDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                        ${merch}</a>
                    <ul class="dropdown-menu">
                        <c:forEach items="${sessionScope.category_list}" var="category">
                            <li><a class="dropdown-item" href="${absolutePath}/controller?command=find_by_category_items&category_id=${category.id}"><fmt:message key="${category.categoryName}"/></a></li>
                        </c:forEach>
                    </ul>
                </li>
                <c:choose>
                    <c:when test="${language eq 'ru_RU'}">
                        <li class="nav-item">
                            <a class="nav-link" href="${absolutePath}/controller?command=change_language&language=en_US">${lang}</a>
                        </li>
                    </c:when>
                    <c:when test="${language eq 'en_US'}">
                        <li class="nav-item">
                            <a class="nav-link" href="${absolutePath}/controller?command=change_language&language=ru_RU">${lang}</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item">
                            <a class="nav-link" href="${absolutePath}/controller?command=change_language&language=ru_RU">${lang}</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
        <div>
            <a class="navbar-brand navbar-center" href="${absolutePath}/controller?command=find_all_items">
                <img src="${absolutePath}/images/logo.png" alt="${alt_catalog}" height="90">
            </a>
        </div>
        <div>
            <ul class="nav navbar-nav navbar-right">
                <c:choose>
                    <c:when test="${user.role eq 'ADMIN'}"><%@include file="component/admin_header.jspf" %></c:when>
                    <c:otherwise><%@include file="component/client_header.jspf" %></c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>
