<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 26.07.2022
  Time: 16:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ctg" uri="customs" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<c:choose>
    <c:when test="${not empty language}"> <fmt:setLocale value="${language}" scope="session"/></c:when>
    <c:when test="${empty language}"> <fmt:setLocale value="${language = 'ru_RU'}" scope="session"/></c:when>
</c:choose>
<fmt:setBundle basename="language.language"/>
<fmt:message key="action.page_navigation" var="page_navigation"/>
<fmt:message key="action.previous" var="previous"/>
<fmt:message key="action.next" var="next"/>
<fmt:message key="currency" var="currency"/>
<html>
<head>
    <title><fmt:message key="title.sizes"/></title>
    <link rel="icon" href="${absolutePath}/images/icon.png" type="image/png">
    <script type="text/javascript" src="${absolutePath}/js/disableBack.js"></script>
    <style>
        html, body, .page {
            min-height: 100%;
            width: 100%;
        }
        .cart_image img {
            display: block;
            max-width: 240px;
            max-height: 240px;
        }
        .cart_image {
            align-items: center;
        }
        table {
            border-collapse: collapse;
        }
        .link, .link:visited {
            text-decoration: none;
            color: #525252;
        }
        .link:hover {
            color: #525252;
            text-decoration: underline;
        }
        .verticalLine {
            border-left: thin solid lightgrey;
        }
        .btn-confirm {
            width: 1.6em;
            height: 1.25em;
            padding: .25em;
            background: url("data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='7 8 11 8' fill='green'><path d='M10.5,14.7928932 L17.1464466,8.14644661 C17.3417088,7.95118446 17.6582912,7.95118446 17.8535534,8.14644661 C18.0488155,8.34170876 18.0488155,8.65829124 17.8535534,8.85355339 L10.8535534,15.8535534 C10.6582912,16.0488155 10.3417088,16.0488155 10.1464466,15.8535534 L7.14644661,12.8535534 C6.95118446,12.6582912 6.95118446,12.3417088 7.14644661,12.1464466 C7.34170876,11.9511845 7.65829124,11.9511845 7.85355339,12.1464466 L10.5,14.7928932 Z'/></svg>") center/1.6em auto no-repeat;
            opacity: .5;
        }
        .btn-confirm:hover {
            opacity: .75;
        }
        .btn-confirm:focus {
            opacity: 1;
            box-shadow: blue;
        }
        .btn-confirm:disabled {
            opacity: .25;
        }
        .btn-dismiss {
            background: url("data:image/svg+xml,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16' fill='red'><path d='M.293.293a1 1 0 011.414 0L8 6.586 14.293.293a1 1 0 111.414 1.414L9.414 8l6.293 6.293a1 1 0 01-1.414 1.414L8 9.414l-6.293 6.293a1 1 0 01-1.414-1.414L6.586 8 .293 1.707a1 1 0 010-1.414z'/></svg>") center/1em auto no-repeat !important;
        }
    </style>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
<div class="page">
    <header class="sticky-top">
        <%@include file="header/headerCommon.jsp"%>
    </header>
    <div class="container justify-content-center col-12 col-sm-6 mt-3">
        <h3 class="text-center p-3"><fmt:message key="size.add"/></h3>
        <form name="add_new_size" method="post" action="${absolutePath}/controller" class="needs-validation" novalidate>
            <input type="hidden" name="command" value="create_size">
            <div class="form-group mb-3">
                <label class="form-label"><fmt:message key="size.name"/></label>
                <input type="text" name="size_name" value="${param.size_name}" class="form-control" required pattern="[A-Z\d]{1,5}">
                <c:if test="${!empty invalid_size_name}">
                    <div class="invalid-feedback-backend" style="color: red">
                        <fmt:message key="${invalid_size_name}"/>
                    </div>
                </c:if>
                <div class="invalid-feedback">
                    <fmt:message key="size.invalid_name"/>
                </div>
            </div>
            <div class="text-center mb-3">
                <button type="submit" class="btn btn-primary"><fmt:message key="action.insert"/></button>
            </div>
        </form>
        <h3 class="text-center p-3"><fmt:message key="size.update"/></h3>
        <form name="change_size_name" method="post" action="${absolutePath}/controller" novalidate>
            <input type="hidden" name="command" value="change_size">
            <label class="form-label"><fmt:message key="size.select"/></label>
            <select class="form-select" aria-label="Default select example" name="size_id">
                <c:forEach var="size" items="${applicationScope.size_list}">
                    <option value="${size.id}">${size.sizeName}</option>
                </c:forEach>
            </select>
            <c:if test="${! empty invalid_size}">
                <div class="invalid-feedback-backend" style="color: red">
                    <fmt:message key="${invalid_size}"/>
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="size.invalid_size"/>
            </div>
            </br>
            <div class="form-group mb-3">
                <label class="form-label"><fmt:message key="size.new_name"/></label>
                <input type="text" name="new_size_name" value="${param.new_size_name}" class="form-control" required pattern="[A-Z\\d]{1,5}">
                <c:if test="${! empty invalid_new_size_name}">
                    <div class="invalid-feedback-backend" style="color: red">
                        <fmt:message key="${invalid_new_size_name}"/>
                    </div>
                </c:if>
                <div class="invalid-feedback">
                    <fmt:message key="size.invalid_name"/>
                </div>
                <c:if test="${! empty not_unique_new_size_name}">
                    <div class="invalid-feedback-backend" style="color: red">
                        <fmt:message key="${not_unique_new_size_name}"/>
                    </div>
                </c:if>
            </div>
            <div class="text-center mb-3">
                <button type="submit" class="btn btn-primary"><fmt:message key="action.update"/></button>
            </div>
        </form>
        <h3 class="text-center p-3"><fmt:message key="size.delete"/></h3>
        <form name="delete_size" method="post" action="${absolutePath}/controller" novalidate>
            <input type="hidden" name="command" value="delete_size">
            <label class="form-label"><fmt:message key="size.select"/></label>
            <select class="form-select" aria-label="Default select example" name="size_id">
                <c:forEach var="size" items="${applicationScope.size_list}">
                    <option value="${size.id}">${size.sizeName}</option>
                </c:forEach>
            </select>
            <c:if test="${!empty invalid_delete_size}">
                <div class="invalid-feedback-backend" style="color: red">
                    <fmt:message key="${invalid_delete_size}"/>
                </div>
            </c:if>
            <div class="invalid-feedback">
                <fmt:message key="size.invalid_size"/>
            </div>
            </br>
            <div class="text-center mb-3">
                <button type="submit" class="btn btn-primary"><fmt:message key="action.delete"/></button>
            </div>
        </form>
    </div>
    <div class="text-center py-3">
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