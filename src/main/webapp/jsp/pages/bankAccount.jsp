<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 23.02.2022
  Time: 20:56
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
<fmt:message key="input.placeholder.money_amount" var="e_money"/>
<html>
<head>
    <title>BANK ACCOUNT</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script>
        function disableBack() {
            window.history.forward();
        }
        setTimeout("disableBack()", 0);
        window.onunload = function() {
            null
        };
    </script>
</head>
<body>
<div class="page">
    <header>
        <%@include file="header/headerCommon.jsp"%>
    </header>
    <div class="back">
        <a href="${absolutePath}/controller?command=upload_cart"><-<fmt:message key="back.shop"/></a>
    </div>
    <div class="container justify-content-center col-12 col-sm-6 mt-3">
        <h3 class="text-center p-3"><fmt:message key="bank_account.name"/></h3>
        <div><fmt:message key="bank_account.money_amount"/>: ${customer.bankAccount}$</div>
        <br/>
        <form role="form" action="${absolutePath}/controller" method="post" class="needs-validation" novalidate>
            <input type="hidden" name="command" value="add_money_to_account"/>
            <div class="form-group">
                <input type="text" name="money" class="form-control" placeholder="${e_money}" required pattern="^\d{1,3}$">
                <c:if test="${!empty error_add_money}">
                    <div class="invalid-feedback-backend" style="color: red">
                        <fmt:message key="${error_add_money}"/>
                    </div>
                </c:if>
                <div class="invalid-feedback">
                    <fmt:message key="bank_account.invalid_money"/>
                </div>
            </div>
            <br/>
            <div class="text-center mb-3">
                <button type="submit" class="btn btn-success"><fmt:message key="bank_account.add_money"/></button>
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

