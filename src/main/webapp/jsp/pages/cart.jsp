<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 18.02.2022
  Time: 19:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="ctg" uri="customs" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<c:choose>
    <c:when test="${not empty language}"> <fmt:setLocale value="${language}" scope="session"/></c:when>
    <c:when test="${empty language}"> <fmt:setLocale value="${language = 'ru_RU'}" scope="session"/></c:when>
</c:choose>
<fmt:setBundle basename="language.language"/>
<fmt:message key="currency" var="currency"/>
<html>
<head>
    <title><fmt:message key="title.cart"/></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript" src="${absolutePath}/js/disableBack.js"></script>
    <style>
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
    </style>
</head>
<body>
    <div class="page">
    <header class="sticky-top">
        <%@include file="header/headerCommon.jsp"%>
    </header>
    <div class="back">
        <a href="${absolutePath}/controller?command=find_all_items"><-<fmt:message key="back.shop"/></a>
    </div>
    <c:if test="${uploaded_cart.isEmpty() eq 'true'}">
        <div class="text-start" style="white-space: pre-line"><fmt:message key="cart.empty"/></div>
    </c:if>
    <c:if test="${uploaded_cart.isEmpty() eq 'false'}">
        <table width="95%">
            <thead>
            <th colspan="3" class="text-left"><fmt:message key="cart.item"/></th>
            <th colspan="2"></th>
            <th class="text-center"><fmt:message key="cart.price"/></th>
            <th class="text-center"><fmt:message key="cart.quantity"/></th>
            <th class="text-center"><fmt:message key="cart.remove"/></th>
            <th class="text-center"><fmt:message key="cart.total"/></th>
            </thead>
            <tbody>
            <c:forEach items="${uploaded_cart}" var="component">
                <tr style="border-top: 1px solid grey">
                    <td colspan="3" class="text-center">
                        <a href="${absolutePath}/controller?command=open_item_page&item_id=${component.item.id}" class="cart_image">
                            <c:choose>
                                <c:when test="${component.item.imagePath eq 'defaultItem.png'}">
                                    <img src="${absolutePath}/images/${component.item.imagePath}" alt="${fn:escapeXml(component.item.name)}" class="product_img">
                                </c:when>
                                <c:otherwise>
                                    <img src="${absolutePath}/uploadImage?image_path=${component.item.imagePath}" alt="${fn:escapeXml(component.item.name)}" class="product_img">
                                </c:otherwise>
                            </c:choose>
                        </a>
                    </td>
                    <td colspan="2" class="text-center">
                        <a href="${absolutePath}/controller?command=open_item_page&item_id=${component.item.id}">
                            <strong class="product_title row">${fn:escapeXml(component.item.name)}</strong>
                        </a>
                        <p class="row">
                            <c:if test="${!empty component.itemSize && component.itemSize.id != 1}">
                                <fmt:message key="item.size"/>: ${component.itemSize.sizeName}
                            </c:if>
                        </p>
                    </td>
                    <td class="text-center">
                        <strong>
                                ${currency}${component.item.price}
                        </strong>
                    </td>
                    <td class="text-center">
                        <c:choose>
                            <c:when test="${component.amount == 1}">
                                <div>-</div>
                            </c:when>
                            <c:otherwise>
                                <a href="${absolutePath}/controller?command=add_item_to_cart&item_id=${component.item.id}&size_id=${component.itemSize.id}&amount=-1&change_from_cart=true">
                                    <div class="counter_btn counter_btn_minus btn-secondary">-</div>
                                </a>
                            </c:otherwise>
                        </c:choose>
                        <span>${component.amount}</span>
                        <a href="${absolutePath}/controller?command=add_item_to_cart&item_id=${component.item.id}&size_id=${component.itemSize.id}&amount=1">
                            <div class="counter_btn counter_btn_plus btn-secondary">+</div>
                        </a>
                    </td>
                    <td class="text-center">
                        <a href="${absolutePath}/controller?command=remove_item_from_cart&item_id=${component.item.id}&size_id=${component.itemSize.id}">&times;</a>
                    </td>
                    <td class="text-center">
                        <strong>
                                ${currency}${component.item.price.multiply(component.amount)}
                        </strong>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="container">
            <div class="row justify-content-end">
                <div class="col col-auto">
                    <strong><fmt:message key="cart.subtotal"/> ${currency}${sessionScope.cart_total_price}</strong>
                </div>
            </div>
            <div class="invalid-feedback-backend row mt-2 justify-content-end" style="color: red">
                <div class="col col-auto">
                    <c:if test="${!empty error_cart}">
                        <fmt:message key="${error_cart}"/>
                    </c:if>
                    <c:if test="${!empty error_add_item_to_cart}">
                        <fmt:message key="${error_add_item_to_cart}"/>
                    </c:if>
                </div>
            </div>
            <div class="row mt-2 justify-content-end">
                <div class="col col-auto">
                    <a href="${absolutePath}/jsp/pages/addShippingAddress.jsp" role="button" class="btn btn-info"><fmt:message key="cart.check_out"/></a>
                </div>
            </div>
        </div>
    </c:if>
    <div class="text-center py-3">
        <ctg:custom-footer/>
    </div>
</div>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>