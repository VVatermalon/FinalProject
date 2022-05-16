<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 09.01.2022
  Time: 18:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customs" %>

<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<c:choose>
    <c:when test="${not empty language}"> <fmt:setLocale value="${language}" scope="session"/></c:when>
    <c:when test="${empty language}"> <fmt:setLocale value="${language = 'ru_RU'}" scope="session"/></c:when>
</c:choose>
<fmt:setBundle basename="language.language"/>
<fmt:message key="alt.shop" var="alt_shop"/>
<fmt:message key="alt.the_album" var="alt_the_album"/>
<fmt:message key="header.language" var="lang"/>
<fmt:message key="header.sign_in" var="sign_in"/>
<fmt:message key="header.sign_out" var="sign_out"/>
<fmt:message key="start.continue_customer" var="continue_customer"/>
<fmt:message key="start.continue_guest" var="continue_guest"/>
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
    <title><fmt:message key="title.home"/></title>
    <style>
        body {
            background-color: black;
        }
        nav {
            background-color: black;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-dark navbar-expand-lg" style="height: 100px">
    <div class="container-fluid" style="height: 100px">
        <div class="col"></div>
        <div class="col d-flex justify-content-center">
            <a class="navbar-brand m-1" href="${absolutePath}/controller?command=find_all_items">
                <img src="${absolutePath}/images/logo.png" alt="${alt_shop}" height="90">
            </a>
        </div>
        <div class="col d-flex flex-row-reverse">
            <ul class="navbar-nav">
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
                <c:choose>
                    <c:when test="${!empty customer or user.role eq 'ADMIN'}">
                        <li class="nav-item"><a class="nav-link" href="${absolutePath}/controller?command=sign_out"> ${sign_out}</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="${absolutePath}/jsp/pages/signIn.jsp">${sign_in}</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
<div class="container-fluid text-center">
    <c:choose>
        <c:when test="${!empty customer or user.role eq 'ADMIN'}">
            <a class="btn btn-info" role="button" href="${absolutePath}/controller?command=find_all_items">${continue_customer}</a>
        </c:when>
        <c:otherwise>
            <a class="btn btn-info" role="button" href="${absolutePath}/controller?command=find_all_items">${continue_guest}</a>
        </c:otherwise>
    </c:choose>
</div>
<div class="text-center">
    <img src="${absolutePath}/images/main.jpg" alt="${alt_the_album}" height="1000"/>
</div>
<div class="text-center">
    <ctg:custom-footer/>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>
