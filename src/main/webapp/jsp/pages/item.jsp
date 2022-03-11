<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 30.01.2022
  Time: 19:24
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
<html>
<head>
    <title><fmt:message key="title.shop"/> </title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script type="text/javascript">
        function disableBack() {
            window.history.forward();
        }
        setTimeout("disableBack()", 0);
        window.onunload = function() {
            null
        };
    </script>
    <script>
        $(document).ready(function() {
            $('.counter_btn_minus').click(function () {
                var $input = $(this).parent().find('input');
                let count = parseInt($input.val()) - 1;
                count = count < 1 ? 1 : count;
                $input.val(count);
                $input.change();
                return false;
            });
            $('.counter_btn_plus').click(function () {
                var $input = $(this).parent().find('input');
                let count = parseInt($input.val()) + 1;
                count = count > 50 ? 50 : count;
                $input.val(count);
                $input.change();
                return false;
            });
        });
    </script>
    <style>
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
        .catalog-item:nth-child(n+5) {
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
</head>
<body>
<div class="page">
    <header>
        <%@include file="header/headerCommon.jsp"%>
    </header>
    <div class="back">
        <a href="${absolutePath}/controller?command=find_all_items"><-<fmt:message key="back.shop"/></a>
    </div>
    <div class="box box_padding catalog-wrapp catalog-body">
        <div class="row">
            <div class="col">
                <img src="${absolutePath}/images/${item.imagePath}" alt="${item.name}" class="product_img">
            </div>
            <div class="col">
                <div class="product">
                    <div class="product_header">
                        <div class="product_title">${item.name}</div>
                    </div>
                    <div class="product_price"><strong id="price"> <fmt:message key="menu.product_money"/> ${item.price}</strong> </div>
                    <c:if test="${user.role ne 'ADMIN'}">
                        <form action="${absolutePath}/controller" method="post">
                            <input type="hidden" name="command" value="add_item_to_cart">
                            <input type="hidden" name="item_id" value="${item.id}">
                            <c:if test="${item.sizes.size() > 1}">
                                <label><fmt:message key="item.size"/></label>
                                <div class="row">
                                    <c:forEach items="${item.sizes}" var="size">
                                        <div class="col">
                                            <c:choose>
                                                <c:when test="${size.amountInStock > 0}">
                                                    <c:choose>
                                                        <c:when test="${!empty param.size_id && size.id eq param.size_id}">
                                                            <input type="radio" name="size_id" id="size${size.sizeName}" value="${size.id}" checked>
                                                        </c:when>
                                                        <c:when test="${empty param.size_id}">
                                                            <input type="radio" name="size_id" id="size${size.sizeName}" value="${size.id}" checked>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="radio" name="size_id" id="size${size.sizeName}" value="${size.id}">
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:when>
                                                <c:otherwise>
                                                    <input type="radio" name="size_id" id="size${size.sizeName}" value="${size.id}" disabled>
                                                </c:otherwise>
                                            </c:choose>
                                            <label for="size${size.sizeName}">${size.sizeName}</label>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:if>
                            <div class="counter">
                                <div class="counter_btn counter_btn_minus btn-secondary">-</div>
                                <input type="text" class="counter_number" id="product_number" name="amount" max="50" readonly value="${empty param.amount ? 1 : param.amount}">
                                <div class="counter_btn counter_btn_plus btn-secondary">+</div>
                            </div>
                            <div class="text-start">
                                <c:choose>
                                    <c:when test="${not empty customer}">
                                        <button type="submit" class="btn btn_type_light"><fmt:message key="item.to_cart"/></button>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${absolutePath}/jsp/pages/signIn.jsp" role="button" class="btn btn-info"><fmt:message key="item.to_cart"/></a>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <c:if test="${!empty param.error_cannot_add_item_to_cart}">
                                <div class="invalid-feedback-backend" style="color: red">
                                    <fmt:message key="${param.error_cannot_add_item_to_cart}"/>
                                </div>
                            </c:if>
                        </form>
                    </c:if>
                    <%--                            <c:if test="${user.role eq 'ADMIN'}">--%>
                    <%--                                <form name="UploadPhoto" method="post" action="${absolutePath}/controller" enctype="multipart/form-data">--%>
                    <%--                                    <input type="hidden" name="command" value="upload_product_photo">--%>
                    <%--                                    <input type="hidden" name="product_name" value="${menu.nameFood}">--%>
                    <%--                                    </br>--%>
                    <%--                                    <div class="form-group" class="mb-3">--%>
                    <%--                                        <label class="form-label"><fmt:message key="menu.picture"/></label>--%>
                    <%--                                        <input type="file" name="picture_path" class="form-control form-control-sm">--%>
                    <%--                                    </div>--%>
                    <%--                                    <button type="submit" class="btn btn-primary btn-sm"><fmt:message key="menu.insert_menu"/></button>--%>
                    <%--                                </form>--%>
                    <%--                            </c:if>--%>
                    <div class="item_description text-start" style="white-space: pre-line">${item.description}</div>
                </div>
            </div>
        </div>
    </div>
    <div class="text-center">
        <ctg:custom-footer/>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>
