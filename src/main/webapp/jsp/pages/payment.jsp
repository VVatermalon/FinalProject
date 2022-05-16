<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 03.03.2022
  Time: 15:11
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
    <title>ORDER PAYMENT</title>
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
        <h3 class="text-center p-3"><fmt:message key="payment.name"/></h3>
        <div><fmt:message key="payment.price"/>: ${sessionScope.cart_total_price}$</div>
        <br/>
        <a href="${absolutePath}/controller?command=pay_order" role="button" class="btn btn-info"><fmt:message key="payment.pay"/></a>
        <c:if test="${!empty error_pay_order}">
            <div class="invalid-feedback-backend" style="color: red">
                <fmt:message key="${error_pay_order}"/>
            </div>
        </c:if>
    </div>
    <div class="text-center">
        <ctg:custom-footer/>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>
