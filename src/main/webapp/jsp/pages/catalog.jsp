<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 28.01.2022
  Time: 19:49
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
<fmt:message key="alt.new_item" var="alt_new_item"/>
<html>
<head>
    <title><fmt:message key="title.shop"/> </title>
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
        html, body, .page {
            min-height: 100%;
        }
        .disabled {
            pointer-events: none;
        }
        .catalog-body {
            position: relative;
            color: #525252;
            text-align: center;
            font-family: Geometria, sans-serif;
            font-size: 14px;
            font-weight: 400;
            line-height: 1;
            padding-top: 20px;
            -webkit-font-smoothing: antialiased;
            background-color: whitesmoke;
        }
        .catalog-wrapp {
            overflow: hidden;
        }
        .box_padding {
            padding-left: 58px;
            padding-right: 58px;
        }
        .box {
            box-sizing: border-box;
            margin: 0 auto;
            max-width: 1440px;
            min-width: 260px;
        }
        a, abbr, acronym, address, applet, article, aside, audio, b, big, blockquote, body, canvas, caption, center, cite, code, dd, del, details, dfn, div, dl, dt, em, embed, fieldset, figcaption, figure, footer, form, h1, h2, h3, h4, h5, h6, header, hgroup, html, i, iframe, img, ins, kbd, label, legend, li, mark, menu, nav, object, ol, output, p, pre, q, ruby, s, samp, section, small, span, strike, strong, sub, summary, sup, table, tbody, td, tfoot, th, thead, time, tr, tt, u, ul, var, video {
            margin: 0;
            padding: 0;
            border: 0;
            font: inherit;
            vertical-align: baseline;
        }
        div {
            display: block;
        }
        .catalog {
            display: flex;
            flex-wrap: wrap;
            margin: 0 -30px 45px;
        }
        .catalog-item:nth-child(n+4) {
            margin-top: 20px;
        }
        .catalog-item {
            box-sizing: border-box;
            width: 30%;
            padding-left: 10px;
            padding-right: 10px;
        }
        .product {
            box-sizing: border-box;
            padding: 21px 20px;
            background-color: #fff;
        }
        .product__link {
            display: block;
            text-decoration: none;
            color: #525252;
        }
        .product_header {
            height: 54px;
            overflow: hidden;
        }
        .product_title {
            font-size: 16px;
            line-height: 18px;
            display: inline;
            font-weight: 700;
            color: #525252;
            text-decoration: underline;
            text-transform: uppercase;
        }
        .product_figure {
            position: relative;
            margin-top: 13px;
            text-align: center;
            background-color: #fff;
            overflow: hidden;
        }
        #add_item {
            margin-bottom: 26px;
            padding: 100px;
        }
        #add_item:hover {
            padding: 105px;
        }
        .product_figure:after {
            content: "";
            position: absolute;
            top: 0;
            right: 0;
            left: 0;
            bottom: 0;
        }
        .product_img {
            display: inline-block !important;
            max-width: 100%;
            max-height: 100%;
            position: relative;
            transition: transform .3s ease;
        }
        img {
            vertical-align: top;
        }
        .product_figure:after {
            content: "";
            position: absolute;
            top: 0;
            right: 0;
            left: 0;
            bottom: 0;
        }
        .product_info {
            font-size: 12px;
            line-height: 14px;
            color: #525252;
            font-weight: 800;
            text-transform: uppercase;
            margin-top: 9px;
        }
        .product_consist {
            height: 56px;
            overflow: hidden;
            font-size: 13px;
            line-height: 14px;
            color: #525252;
            margin-top: 13px;
        }
        .product__cost {
            display: flex;
            justify-content: space-between;
            margin-top: 29px;
        }
        .product_price:only-child {
            margin-left: auto;
            margin-right: auto;
        }
        .product_price {
            font-size: 20px;
            color: #525252;
            position: relative;
            min-height: 26px;
            font-weight: 500;
        }
        .product_actions {
            display: -ms-flexbox;
            display: flex;
            -ms-flex-pack: justify;
            justify-content: space-between;
            margin-top: 20px;
        }
        .product_actions .counter {
            flex-shrink: 0;
            width: 50%;
        }
        .product_actions .counter {
            flex-shrink: 0;
            width: 50%;
        }
        .counter, .counter_btn {
            display: -ms-flexbox;
            display: flex;
            -ms-flex-align: center;
            align-items: center;
            position: relative;
        }
        .counter {
            height: 34px;
            overflow: hidden;
            width: 100px;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
            box-sizing: border-box;
        }
        .counter_btn {
            -ms-flex-pack: center;
            justify-content: center;
            width: 32%;
            height: 100%;
            cursor: pointer;
            color: #525252;
            font-size: 24px;
        }
        .counter_number {
            display: inline-block;
            vertical-align: top;
            outline: none;
            padding: 1px 0 0;
            margin: 0;
            box-sizing: border-box;
            width: 36%;
            height: 34px;
            border: none;
            text-align: center;
            font-weight: 500;
            font-family: Geometria, sans-serif;
            font-size: 24px;
            line-height: 32px;
            background-color: transparent;
            box-shadow: none;
            cursor: pointer;
            color: #525252;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }
        input[type="text" i] {
            padding: 1px 2px;
        }
        input {
            -webkit-writing-mode: horizontal-tb !important;
            text-rendering: auto;
            letter-spacing: normal;
            word-spacing: normal;
            text-transform: none;
            text-indent: 0;
            text-shadow: none;
            display: inline-block;
            text-align: start;
            appearance: auto;
            -webkit-rtl-ordering: logical;
            cursor: text;
            margin: 0;
            font: 400 13.3333px Arial;
            padding: 1px 2px;
            border-width: 2px;
            border-style: inset;
            border-image: initial;
        }
        .product .btn {
            padding-left: 14px;
            padding-right: 14px;
        }
        .btn_type_light {
            border: 1px solid #d8d8d8;
            border-radius: 1px;
            padding: 8px 40px 6px;
            font-size: 14px;
            line-height: 18px;
            background-color: #fff;
            color: #525252;
            text-transform: uppercase;
        }
        .btn, .btn:active, .btn:focus {
            margin: 0;
        }
        .btn {
            display: inline-block;
            vertical-align: middle;
            position: relative;
            top: 0;
            border: none;
            outline: 0;
            font-weight: 400;
            line-height: 1;
            cursor: pointer;
            word-wrap: break-word;
            text-decoration: none;
            box-sizing: border-box;
            text-align: center;
            background-color: transparent;
            font-family: Geometria, sans-serif;
        }
        button {
            appearance: auto;
            -webkit-writing-mode: horizontal-tb !important;
            text-rendering: auto;
            letter-spacing: normal;
            word-spacing: normal;
            text-transform: none;
            text-indent: 0;
            text-shadow: none;
            display: inline-block;
            text-align: center;
            align-items: flex-start;
            cursor: default;
            box-sizing: border-box;
            margin: 0;
            font: 400 13.3333px Arial;
            padding: 1px 6px;
            border-width: 2px;
            border-style: outset;
            border-image: initial;
        }
        h1 {
            position: relative;
            text-align: center;
        }
        #message9 {
            position: absolute;
            left: 35%;
            bottom: 8%;
            width: 30%;
            text-align: center;
            z-index:99999999;
            padding: 5px;
            font-weight: bold;
        }
        .valid_message {
            color: darkgreen;
        }
        .invalid_message {
            color: red;
        }
        .pages {
            position: relative;
            left: 40%;
        }
    </style>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
<%--    <link rel="stylesheet" href="${absolutePath}/CSS/styles.css">--%>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
</head>
<body>
<div class="page">
    <header class="sticky-top">
        <%@include file="header/headerCommon.jsp"%>
    </header>
    <div class="container">
        <div class="row">
            <div class="btn-group col-auto">
                <button class="btn btn-secondary dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                    <fmt:message key="action.sort"/></button>
                <ul class="dropdown-menu">
                    <li>
                        <form name="sortByPrice" action="${absolutePath}/controller">
                            <input type="hidden" name="command" value="find_all_items">
                            <input type="hidden" name="sort" value="PRICE">
                            <input type="hidden" name="sort_order" value="ASC">
                            <c:if test="${not empty param.category_id}">
                                <input type="hidden" name="category_id" value="${param.category_id}">
                            </c:if>
                            <button type="submit" class="dropdown-item"><fmt:message key="action.sort_by_price_asc"/></button>
                        </form>
                    </li>
                    <li>
                        <form name="sortByPrice" action="${absolutePath}/controller">
                            <input type="hidden" name="command" value="find_all_items">
                            <input type="hidden" name="sort" value="PRICE">
                            <input type="hidden" name="sort_order" value="DESC">
                            <c:if test="${not empty param.category_id}">
                                <input type="hidden" name="category_id" value="${param.category_id}">
                            </c:if>
                            <button type="submit" class="dropdown-item"><fmt:message key="action.sort_by_price_desc"/></button>
                        </form>
                    </li>
                    <li>
                        <form name="sortByName" action="${absolutePath}/controller">
                            <input type="hidden" name="command" value="find_all_items">
                            <input type="hidden" name="sort" value="ITEM_NAME">
                            <input type="hidden" name="sort_order" value="ASC">
                            <c:if test="${not empty param.category_id}">
                                <input type="hidden" name="category_id" value="${param.category_id}">
                            </c:if>
                            <button type="submit" class="dropdown-item"><fmt:message key="action.sort_by_name_asc"/></button>
                        </form>
                    </li>
                    <li>
                       <form name="sortByName" action="${absolutePath}/controller">
                            <input type="hidden" name="command" value="find_all_items">
                            <input type="hidden" name="sort" value="ITEM_NAME">
                            <input type="hidden" name="sort_order" value="DESC">
                            <c:if test="${not empty param.category_id}">
                                <input type="hidden" name="category_id" value="${param.category_id}">
                            </c:if>
                            <button type="submit" class="dropdown-item"><fmt:message key="action.sort_by_name_desc"/></button>
                        </form>
                    </li>
                    <li>
                        <form name="sortByPopularity" action="${absolutePath}/controller">
                            <input type="hidden" name="command" value="find_all_items">
                            <input type="hidden" name="sort" value="POPULARITY">
                            <input type="hidden" name="sort_order" value="DESC">
                            <c:if test="${not empty param.category_id}">
                                <input type="hidden" name="category_id" value="${param.category_id}">
                            </c:if>
                            <button type="submit" class="dropdown-item"><fmt:message key="action.sort_by_popularity_desc"/></button>
                        </form>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="box box_padding catalog-wrapp catalog-body">
        <c:choose>
            <c:when test="${item_list.isEmpty() eq 'true'}">
                <div class="product_title"><fmt:message key="catalog.no_items"/></div>
            </c:when>
            <c:otherwise>
                <div class="catalog justify-content-center">
                    <c:if test="${user.role eq 'ADMIN'}">
                        <div class="catalog-item">
                            <div class="product">
                                <div class="product_header">
                                    <a href="${absolutePath}/jsp/pages/updateItem.jsp">
                                        <div class="product_title"><fmt:message key="item.new"/></div>
                                    </a>
                                </div>
                                <a href="${absolutePath}/jsp/pages/updateItem.jsp">
                                    <div class="product_figure" id="add_item">
                                        <img src="${absolutePath}/images/createItem.png" alt="${alt_new_item}" class="product_img">
                                    </div>
                                </a>
                            </div>
                        </div>
                    </c:if>
                    <c:forEach items="${item_list}" var="item">
                        <div class="catalog-item">
                            <div class="product">
                                <div class="product_header">
                                    <a href="${absolutePath}/controller?command=open_item_page&item_id=${item.id}">
                                        <div class="product_title">${item.name}</div>
                                    </a>
                                </div>
                                <a href="${absolutePath}/controller?command=open_item_page&item_id=${item.id}">
                                    <div class="product_figure">
                                        <c:choose>
                                            <c:when test="${item.imagePath eq 'defaultItem.png'}">
                                                <img src="${absolutePath}/images/${item.imagePath}" alt="${item.name}" class="product_img">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${absolutePath}/uploadImage?image_path=${item.imagePath}" alt="${item.name}" class="product_img">
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </a>
                                <div class="">
                                    <div class="product_price"><strong id="price">${currency}${item.price}</strong> </div>
                                    <c:if test="${item.amountInStock == 0}">
                                        <div style="color: red"><fmt:message key="catalog.sold_out"/></div>
                                    </c:if>
                                    <c:if test="${user.role eq 'ADMIN'}">
                                        <div class="row">
                                            <div class="col">
                                                <a class="btn btn-info btn-sm" href="${absolutePath}/controller?command=open_update_item_page&item_id=${item.id}" role="button"><fmt:message key="action.update"/></a>
                                            </div>
                                            <div class="col">
                                                <form action="${absolutePath}/controller" method="post">
                                                    <input type="hidden" name="command" value="delete_item">
                                                    <input type="hidden" name="item_id" value="${item.id}">
                                                    <button type="submit" class="btn btn-danger btn-sm"><fmt:message key="action.delete"/></button>
                                                </form>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                <div class="container">
                    <nav aria-label="${page_navigation}">
                        <ul class="pagination pagination-lg justify-content-center">
                            <form id="paginationPrevious" action="${absolutePath}/controller">
                                <input type="hidden" name="command" value="find_all_items">
                                <c:if test="${not empty param.category_id}">
                                    <input type="hidden" name="category_id" value="${param.category_id}">
                                </c:if>
                                <c:if test="${not empty param.sort}">
                                    <input type="hidden" name="sort" value="${param.sort}">
                                </c:if>
                                <c:if test="${not empty param.sort_order}">
                                    <input type="hidden" name="sort_order" value="${param.sort_order}">
                                </c:if>
                                <input type="hidden" name="page" value="${requestScope.page-1}"/>
                                <c:choose>
                                    <c:when test="${requestScope.page > 1}">
                                        <li class="page-item">
                                            <a class="page-link" aria-label="${previous}"
                                               onclick="document.getElementById('paginationPrevious').submit(); return false;">
                                                <span aria-hidden="true">&laquo;</span>
                                            </a>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item disabled">
                                            <a class="page-link" href="#" aria-label="${previous}">
                                                <span aria-hidden="true">&laquo;</span>
                                            </a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                            <li class="page-item"><span class="page-link">${requestScope.page}</span></li>
                            <form id="paginationNext" action="${absolutePath}/controller">
                                <input type="hidden" name="command" value="find_all_items">
                                <c:if test="${not empty param.category_id}">
                                    <input type="hidden" name="category_id" value="${param.category_id}">
                                </c:if>
                                <c:if test="${not empty param.sort}">
                                    <input type="hidden" name="sort" value="${param.sort}">
                                </c:if>
                                <c:if test="${not empty param.sort_order}">
                                    <input type="hidden" name="sort_order" value="${param.sort_order}">
                                </c:if>
                                <input type="hidden" name="page" value="${requestScope.page+1}"/>
                                <c:choose>
                                    <c:when test="${!empty is_next_page}">
                                        <li class="page-item">
                                            <a class="page-link" aria-label="${next}"
                                               onclick="document.getElementById('paginationNext').submit(); return false;">
                                                <span aria-hidden="true">&raquo;</span>
                                            </a>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item disabled">
                                            <a class="page-link" href="#" aria-label="${next}">
                                                <span aria-hidden="true">&raquo;</span>
                                            </a>
                                        </li>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                        </ul>
                    </nav>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="text-center py-3">
        <ctg:custom-footer/>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<%--<script>--%>
<%--    let discount = '${menu.discount}'+100;--%>
<%--    document.getElementById('${menu.discount}').value = discount;--%>
<%--</script>--%>

</body>
</html>