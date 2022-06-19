<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 16.05.2022
  Time: 18:26
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
<fmt:message key="currency" var="currency"/>
<html>
<head>
    <title><fmt:message key="title.order_history"/></title>
    <link rel="icon" href="${absolutePath}/images/icon.png" type="image/png">
    <script>
        function disableBack() {
            window.history.forward();
        }
        setTimeout("disableBack()", 0);
        window.onunload = function() {
            null
        };
    </script>
    <style>
        html,body{
            width:100%;
            height:100%;
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
        .link, .link:visited, .link:focus {
            text-decoration: none;
            color: #525252;
        }
        .link:hover {
            color: #525252;
            text-decoration: underline;
        }
        .accordion-h, .accordion-h:visited, .accordion-h:focus, .accordion-h:hover, .accordion-h:active, .accordion-h:link {
            text-decoration: none;
            color: #525252 !important;
        }
    </style>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body class="bg-light">
<div class="page bg-light">
    <header class="sticky-top">
        <%@include file="header/headerCommon.jsp"%>
    </header>
    <div class="container px-5 bg-light">
        <h3 class="text-center px-3 pt-3"><fmt:message key="profile.history"/></h3>
       <div class="accordion accordion-flush mx-5 px-5" id="orderHistory">
        <c:forEach items="${orders}" var="order">
            <div class="accordion-item bg-light">
                <div class="accordion-header" id="heading${order.id}">
                    <a href="#" class="accordion-button collapsed bg-light accordion-h" type="button" data-bs-toggle="collapse" data-bs-target="#collapse${order.id}" aria-expanded="false" aria-controls="collapse${order.id}">
                        <div class="row align-items-center container-fluid">
                            <div class="col-md-2">
                                <strong class="link"><fmt:message key="order"/> №${order.id}</strong>
                            </div>
                            <div class="col-md-2 p-3">
                                <fmt:message key="order.on"/> ${order.dateOrdered}
                            </div>
                            <div class="col-md-2 p-3">
                                <fmt:message key="order.status.${order.status.toString()}"/>
                            </div>
                            <div class="col-md-2 p-3">
                                <fmt:message key="order.price"/> ${currency}${order.totalPrice}
                            </div>
                            <div class="col-md p-3">
                                <fmt:message key="order.sale"/> ${order.giftCard}
                            </div>
                            <div class="col-md-2 p-3">
                                <fmt:message key="order.total_price"/> ${currency}${order.totalPrice}
                            </div>
                        </div>
                    </a>
                </div>
                <div id="collapse${order.id}" class="accordion-collapse collapse" aria-labelledby="heading${order.id}" data-bs-parent="#orderHistory">
                    <div class="accordion-body">
                        <div class="mx-5">
                            <p><fmt:message key="order.shipping_address"/></p>
                            <c:set var="apartment" scope="page" value="${order.address.apartment.isPresent() ? order.address.apartment.get() : null}"/>
                            <p><fmt:message key="country.${order.address.country.toString()}"/> ${order.address.city}, ${order.address.address}, ${apartment} <fmt:message key="order.label.postal_code"/>: ${order.address.postalCode}</p>
                        </div>
                        <c:forEach items="${order.components}" var="component">
                            <div class="row m-3 align-items-start">
                                <div class="col-md-4 text-center">
                                    <a href="${absolutePath}/controller?command=open_item_page&item_id=${component.item.id}" class="cart_image">
                                        <img src="${absolutePath}/images/${component.item.imagePath}" alt="${component.item.name}">
                                    </a>
                                </div>
                                <div class="col-md-2 text-center">
                                    <a href="${absolutePath}/controller?command=open_item_page&item_id=${component.item.id}" class="link">
                                        <strong class="product_title row">${component.item.name}</strong>
                                    </a>
                                    <p class="row">
                                        <c:if test="${!empty component.itemSize && component.itemSize.id != 1}">
                                            <fmt:message key="item.size"/>: ${component.itemSize.sizeName}
                                        </c:if>
                                    </p>
                                </div>
                                <div class="col-md-2 text-center">
                                    <strong class="link"><fmt:message key="order.amount"/></strong>
                                    <p>${component.amount}</p>
                                </div>
                            </div>
                            <hr/>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
        </div>
    </div>
    <div class="text-center py-3">
        <ctg:custom-footer/>
    </div>
</div>
<script>
    function showAccordionItem(name) {
        document.getElementById(name).click();
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>