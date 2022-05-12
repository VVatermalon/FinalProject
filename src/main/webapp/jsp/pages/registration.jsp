<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 25.01.2022
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customs" %>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<c:choose>
    <c:when test="${not empty language}"><fmt:setLocale value="${language}" scope="session"/></c:when>
    <c:when test="${empty language}"><fmt:setLocale value="${language = 'ru_RU'}" scope="session"/></c:when>
</c:choose>
<fmt:setBundle basename="language.language"/>
<fmt:message key="header.language" var="lang"/>
<fmt:message key="header.sign_in" var="sign_in"/>
<fmt:message key="form.label.email" var="user_email"/>
<fmt:message key="form.label.first_name" var="user_first_name"/>
<fmt:message key="form.label.last_name" var="user_last_name"/>
<fmt:message key="form.label.password" var="user_pass"/>
<fmt:message key="form.label.phone" var="user_phone"/>
<fmt:message key="registration.name" var="reg_name"/>
<fmt:message key="input.placeholder.email" var="e_email"/>
<fmt:message key="input.placeholder.first_name" var="e_first_name"/>
<fmt:message key="input.placeholder.last_name" var="e_last_name"/>
<fmt:message key="input.placeholder.password" var="e_password"/>
<fmt:message key="input.placeholder.phone" var="e_phone"/>

<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script type="text/javascript">
        window.history.forward();
        function noBack() {
            window.history.forward();
        }
    </script>
    <title><fmt:message key="title.registration"/></title>
</head>
<body>
<nav class="navbar navbar-light bg-light navbar-expand-lg" style="height: 100px">
    <div class="container-fluid" style="height: 100px">
        <div class="col"></div>
        <div class="col d-flex justify-content-center">
            <a class="navbar-brand m-1" href="${absolutePath}/jsp/pages/start.jsp">
                <img src="${absolutePath}/images/logo.png" alt="main" height="90">
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
                <li class="nav-item"><a class="nav-link" href="${absolutePath}/jsp/pages/signIn.jsp">${sign_in}</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="back">
    <a href="${absolutePath}/jsp/pages/start.jsp"><-<fmt:message key="back.home"/></a>
</div>
<div class="page">
    <div class="container justify-content-center col-12 col-sm-6 mt-3">
        <h3 class="text-center p-3">${reg_name}</h3>
        <form role="form" action="${absolutePath}/controller" method="post" class="needs-validation" novalidate>
            <input type="hidden" name="command" value="registration"/>
            <div class="row gy-3">
                <div class="form-group">
                    <label class="form-label">${user_first_name}</label>
                    <input type="text" name="user_name" value="${data_map.user_name}" class="form-control" placeholder="${e_first_name}" required pattern="^[A-Za-zА-Яа-я]{3,50}$">
                    <div id="firstNameHelp" class="form-text"><fmt:message key="registration.correct_first_name"/></div>
                    <c:if test="${!empty invalid_name}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_name}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="registration.invalid_first_name"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">${user_last_name}</label>
                    <input type="text" name="user_surname" value="${data_map.user_surname}" class="form-control" placeholder="${e_last_name}" required pattern="^[A-Za-zА-Яа-я]{3,50}$">
                    <div id="surnameHelp" class="form-text"><fmt:message key="registration.correct_surname"/></div>
                    <c:if test="${!empty invalid_surname}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_surname}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="registration.invalid_surname"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">${user_email}</label>
                    <input type="email" name="user_email" value="${data_map.user_email}" class="form-control" placeholder="${e_email}" required pattern="^[A-Za-z0-9\._]{1,25}@[a-z]{2,7}\.[a-z]{2,4}$">
                    <div id="emailHelp" class="form-text"><fmt:message key="registration.correct_email"/></div>
                    <c:if test="${!empty invalid_email}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_email}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="registration.invalid_email"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">${user_phone}</label>
                    <input type="tel" name="phone_number" value="${data_map.phone_number}" class="form-control" placeholder="${e_phone}" required pattern="+375(29|25|44|33)\d{7}">
                    <div id="phoneHelp" class="form-text"><fmt:message key="registration.correct_phone_number"/></div>
                    <c:if test="${!empty invalid_phone_number}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_phone_number}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="registration.invalid_phone_number"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label">${user_pass}</label>
                    <input type="password" name="user_password" value="${data_map.user_password}" class="form-control" placeholder="${e_password}" required pattern="^[A-Za-zА-Яа-я0-9\._*]{5,40}$">
                    <div id="passHelp" class="form-text"><fmt:message key="registration.correct_password"/></div>
                    <c:if test="${!empty invalid_password}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_password}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="registration.invalid_password"/>
                    </div>
                </div>
                <div class="text-center mb-3">
                    <button type="submit" class="btn btn-success"><fmt:message key="registration.submit"/></button>
                </div>
            </div>
        </form>
    </div>
    <div class="text-center">
        <ctg:custom-footer/>
    </div>
</div>
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
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>