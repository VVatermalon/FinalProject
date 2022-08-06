<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 09.01.2022
  Time: 17:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customs" %>
<%@ taglib prefix="fmr" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<c:choose>
    <c:when test="${not empty language}"> <fmt:setLocale value="${language}" scope="session"/></c:when>
    <c:when test="${empty language}"> <fmt:setLocale value="${language = 'ru_RU'}" scope="session"/></c:when>
</c:choose>
<fmt:setBundle basename="language.language"/>
<fmt:message key="alt.main" var="alt_main"/>
<fmt:message key="header.language" var="lang"/>
<fmt:message key="registration.name" var="register"/>
<fmt:message key="input.placeholder.email" var="enter_email"/>
<fmt:message key="input.placeholder.password" var="enter_password"/>
<fmt:message key="sign_in.name" var="sign_in_name"/>
<fmt:message key="sign_in.submit" var="sign_in"/>
<fmt:message key="form.label.email" var="email" />
<fmt:message key="form.label.password" var="password" />

<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="/CSS/styles.css">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script type="text/javascript" src="${absolutePath}/js/disableBack.js"></script>
    <title><fmt:message key="title.sign_in"/></title>
</head>
<body>
<nav class="navbar navbar-light bg-light navbar-expand-lg" style="height: 100px">
    <div class="container-fluid" style="height: 100px">
        <div class="col"></div>
        <div class="col d-flex justify-content-center">
            <a class="navbar-brand m-1" href="${absolutePath}/jsp/pages/start.jsp">
                <img src="${absolutePath}/images/logo.png" alt="${alt_main}" height="90">
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
                <li class="nav-item"><a class="nav-link" href="${absolutePath}/jsp/pages/registration.jsp">${register}</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="back">
    <a href="${absolutePath}/jsp/pages/start.jsp"><-<fmt:message key="back.home"/></a>
</div>
<div class="page">
    <div class="container justify-content-center col-12 col-sm-6 mt-3">
        <h3 class="text-center p-3">${sign_in_name}</h3>
        <form name="LoginForm" method="post" action="${absolutePath}/controller" novalidate>
            <input type="hidden" name="command" value="sign_in"/>
            </br>
            <div class="form-group mb-3">
                <label class="form-label">${email}</label>
                <input type="text" name="user_email" class="form-control form-control-sm" placeholder="${enter_email}" pattern="^[A-Za-z0-9\._]{1,25}@[a-z]{2,7}\.[a-z]{2,4}$">
                <div class="invalid-feedback">
                    <fmt:message key="registration.invalid_email"/>
                </div>
            </div>
            </br>
            <div class="form-group mb-3">
                <label class="form-label">${password}</label>
                <input type="password" name="user_password" class="form-control form-control-sm" placeholder="${enter_password}" pattern="^[A-Za-zА-Яа-я0-9\._*]{5,40}$">
                <div class="invalid-feedback">
                    <fmt:message key="registration.invalid_password"/>
                </div>
            </div>
            <c:if test="${!empty error_incorrect_login_or_password}">
                <div class="invalid-feedback-backend" style="color: red">
                    <fmt:message key="${error_incorrect_login_or_password}"/>
                </div>
            </c:if>
            <c:if test="${! empty user_status_blocked}">
                <div class="invalid-feedback-backend" style="color: red">
                    <fmt:message key="${user_status_blocked}"/>
                </div>
            </c:if>
            <br/>
            <div class="text-center">
                <button type="submit" class="btn btn-primary">${sign_in}</button>
            </div>
        </form>
    </div>
    <div class="text-center py-3">
        <ctg:custom-footer/>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>

<script>
    (function () {
        'use strict'
        var forms = document.querySelectorAll('.needs-validation')
        Array.prototype.slice.call(forms)
            .forEach(function (form) {
                form.addEventListener('submit', function (event) {
                    if (!form.checkValidity()) {
                        event.preventDefault()
                        event.stopPropagation()
                    }
                    form.classList.add('was-validated')
                }, false)
            })
    })()
</script>
</body><hr/>
</html>
