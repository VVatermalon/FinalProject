<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 12.07.2022
  Time: 18:39
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
<fmt:message key="alt.main" var="alt_main"/>
<fmt:setBundle basename="language.language"/>
<fmt:message key="header.language" var="lang"/>
<fmt:message key="currency" var="currency"/>
<fmt:message key="item.name" var="item_name_placeholder"/>
<fmt:message key="item.price" var="item_price_placeholder"/>
<fmt:message key="item.description" var="item_description_placeholder"/>
<html>
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script type="text/javascript" src="${absolutePath}/js/disableBack.js"></script>
    <title>
        <c:if test="${!empty item}">
            <fmt:message key="title.update_item"/>
        </c:if>
        <c:if test="${empty item}">
            <fmt:message key="title.create_item"/>
        </c:if>
    </title>
    <style>
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
        .categories {
            display: flex;
            flex-wrap: wrap;
            margin: 0 -30px 45px;
        }
        .categories-item:nth-child(n+5) {
            margin-top: 20px;
        }
        .categories-item {
            box-sizing: border-box;
            width: 25%;
            padding-left: 10px;
            padding-right: 10px;
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
        .product {
            box-sizing: border-box;
            padding: 21px 20px;
            background-color: #fff;
        }
        .product_header {
            height: 54px;
            overflow: hidden;
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
        .product_actions .counter {
            flex-shrink: 0;
            width: 50%;
        }
        .product_actions .counter {
            flex-shrink: 0;
            width: 50%;
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
    </style>
</head>
<body>
    <header class="sticky-top">
        <%@include file="header/headerCommon.jsp"%>
    </header>
    <div class="page">
        <h3 class="text-center p-3"></h3>
        <form role="form" action="${absolutePath}/controller" method="post" class="needs-validation" enctype="multipart/form-data" novalidate>
            <input type="hidden" name="command" value="update_item"/>
            <c:if test="${!empty item}">
                <input type="hidden" name="item_id" value="${item.id}"/>
            </c:if>
            <input type="hidden" id="sizes-id" name="sizes_id"/>
            <input type="hidden" id="categories-id" name="categories_id"/>
            <div class="box box_padding catalog-wrapp catalog-body">
                <div class="row">
                    <div class="col">
                        <c:if test="${empty item}">
                            <img src="${absolutePath}/images/defaultItem.png" alt="default item" class="product_img">
                        </c:if>
                        <c:choose>
                            <c:when test="${item.imagePath eq 'defaultItem.png'}">
                                <img src="${absolutePath}/images/${item.imagePath}" alt="${item.name}" class="product_img">
                            </c:when>
                            <c:otherwise>
                                <img src="${absolutePath}/uploadImage?image_path=${item.imagePath}" alt="${item.name}" class="product_img">
                            </c:otherwise>
                        </c:choose>
                        <input type="file" name="image_path" class="form-control form-control-sm"/>
                        <c:if test="${!empty invalid_image}">
                            <div class="invalid-feedback-backend" style="color: red">
                                <fmt:message key="${invalid_image}"/>
                            </div>
                        </c:if>
                    </div>
                    <div class="col">
                        <div class="product">
                            <div class="product_header">
                                <div class="form-group">
                                    <input type="text" name="item_name" value="${!empty item_data_map ? item_data_map.item_name : item.name}" class="form-control product-title" placeholder="${item_name_placeholder}" required pattern="^[\s\S]{1,40}$">
                                    <c:if test="${!empty invalid_name}">
                                        <div class="invalid-feedback-backend" style="color: red">
                                            <fmt:message key="${invalid_name}"/>
                                        </div>
                                    </c:if>
                                    <div class="invalid-feedback">
                                        <fmt:message key="item.invalid_name"/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group mb-3">
                                ${currency}<input type="text" name="item_price" value="${!empty item_data_map ? item_data_map.item_price : item.price}" class="form-control" placeholder="${item_price_placeholder}" required pattern="^([1-9]\d{0,5}|[1-9]\d{0,5}\.\d{1,2}|0.([1-9]\d?|\d[1-9]))$">
                                <c:if test="${!empty invalid_price}">
                                    <div class="invalid-feedback-backend" style="color: red">
                                        <fmt:message key="${invalid_price}"/>
                                    </div>
                                </c:if>
                                <div class="invalid-feedback">
                                    <fmt:message key="item.invalid_price"/>
                                </div>
                            </div>
                            <label><fmt:message key="item.size"/></label>
                            <c:forEach items="${applicationScope.size_list}" var="size">
                                <div class="row my-3">
                                    <div class="col">
                                        <c:set scope="page" var="hasItemThisSize" value="${false}"/>
                                        <c:choose>
                                            <c:when test="${item.sizes.stream().filter(itemSize->itemSize.id == size.id).count() > 0}">
                                                <c:set scope="page" var="hasItemThisSize" value="${true}"/>
                                                <input type="checkbox" name="size_id" class="size-checkbox" id="size${size.id}" value="${size.id}" checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" name="size_id" class="size-checkbox" id="size${size.id}" value="${size.id}">
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${size.id eq 1}">
                                                <label for="size${size.sizeName}"><fmt:message key="size.no_size"/></label>
                                            </c:when>
                                            <c:otherwise>
                                                <label for="size${size.sizeName}">${size.sizeName}</label>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div class="col">
                                        <c:set scope="page" value="${item.sizes.stream().filter(itemSize->itemSize.id == size.id).findFirst()}" var="itemSizeCurrent"/>
                                        <c:if test="${hasItemThisSize}">
                                            <input type="number" name="item_size_amount_in_stock${size.id}" id="amountsize${size.id}" class="form-control form-control-sm" value="${itemSizeCurrent.get().amountInStock}" placeholder="Количество товара" min="0" max="100_000_000" required/>
                                        </c:if>
                                        <c:if test="${!hasItemThisSize}">
                                            <input type="number" name="item_size_amount_in_stock${size.id}" id="amountsize${size.id}" class="form-control form-control-sm" value="0" placeholder="Количество товара" min="0" max="100_000_000" disabled/>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                            <c:if test="${!empty invalid_sizes}">
                                <div class="invalid-feedback-backend" style="color: red">
                                    <fmt:message key="${invalid_sizes}"/>
                                </div>
                            </c:if>
                            <c:if test="${!empty invalid_amounts}">
                                <div class="invalid-feedback-backend" style="color: red">
                                    <fmt:message key="${invalid_amounts}"/>
                                </div>
                            </c:if>
                            <label><fmt:message key="item.category"/></label>
                            <div class="categories">
                            <c:forEach items="${applicationScope.category_list}" var="category">
                                    <div class="categories-item">
                                        <c:choose>
                                            <c:when test="${item.categories.stream().filter(itemCategory->itemCategory.id == category.id).count() > 0}">
                                                <input type="checkbox" name="category_id" class="category-checkbox" id="category${category.categoryName}" value="${category.id}" checked>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" name="category_id" class="category-checkbox" id="category${category.categoryName}" value="${category.id}">
                                            </c:otherwise>
                                        </c:choose>
                                        <label for="category${category.categoryName}"><fmt:message key="${category.categoryName}"/></label>
                                    </div>
                            </c:forEach>
                            </div>
                            <c:if test="${!empty invalid_categories}">
                                <div class="invalid-feedback-backend" style="color: red">
                                    <fmt:message key="${invalid_categories}"/>
                                </div>
                            </c:if>
                            <div class="item_description text-start" style="white-space: pre-line">
                                <div class="form-group">
                                    <textarea name="item_description" class="form-control" placeholder="${item_description_placeholder}" required maxlength="1000">${!empty item_data_map ? item_data_map.item_description : item.description}</textarea>
                                    <c:if test="${!empty invalid_description}">
                                        <div class="invalid-feedback-backend" style="color: red">
                                            <fmt:message key="${invalid_description}"/>
                                        </div>
                                    </c:if>
                                    <div class="invalid-feedback">
                                        <fmt:message key="item.invalid_description"/>
                                    </div>
                                </div>
                            </div>
                            <button type="submit" class="btn btn_type_light"><fmt:message key="item.save"/></button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
        <div class="text-center py-3">
            <ctg:custom-footer/>
        </div>
    </div>
    <script>
        (function () {
            'use strict'
            var checkboxes = document.querySelectorAll('.size-checkbox')
            Array.prototype.slice.call(checkboxes)
                .forEach(function (checkbox) {
                    checkbox.addEventListener('change', function (event) {
                        let sizeAmountInputId = 'amount' + this.id
                        let input = document.getElementById(sizeAmountInputId)
                        if (this.checked) {
                            input.disabled = false;
                            input.required = true;
                        } else {
                            input.disabled = true;
                            input.required = false;
                        }
                    }, false)
                })
        })()
    </script>
    <script>
        (function () {
            'use strict'
            var forms = document.querySelectorAll('.needs-validation')
            Array.prototype.slice.call(forms)
                .forEach(function (form) {
                    form.addEventListener('submit', function (event) {
                        let sizes = [];
                        let sizeCheckboxes = document.querySelectorAll('input[type="checkbox"][name="size_id"]:checked')
                        Array.prototype.slice.call(sizeCheckboxes).forEach(checkbox => {
                            sizes.push(checkbox.value);
                        });
                        document.getElementById('sizes-id').value = JSON.stringify(sizes);
                        let categories = [];
                        let categoryCheckboxes = document.querySelectorAll('input[type="checkbox"][name="category_id"]:checked')
                        Array.prototype.slice.call(categoryCheckboxes).forEach(checkbox => {
                            categories.push(checkbox.value);
                        });
                        document.getElementById('categories-id').value = JSON.stringify(categories);
                        if (!form.checkValidity()) {
                            event.preventDefault()
                            event.stopPropagation()
                        }
                        form.classList.add('was-validated');
                    }, false)
                })
        })()
    </script>
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>
