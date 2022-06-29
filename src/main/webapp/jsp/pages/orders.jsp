<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 27.05.2022
  Time: 15:35
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
    <title><fmt:message key="title.orders"/></title>
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
    <div class="row justify-content-between me-0">
        <nav class="navbar navbar-expand-lg col-3 flex-column position-sticky">
            <div class="container-fluid p-3">
                <div class="container-fluid bg-light">
                    <form name="sortByPrice" action="${absolutePath}/controller" novalidate>
                        <input type="hidden" name="command" value="upload_orders">
                        <div class="nav-item my-3">
                            <strong>Фильтр</strong>
                        </div>
                        <div class="nav-item mb-2">
                            <label class="form-label">Статус</label>
                            <select class="form-select" name="order_status" required>
                                <option value="order_status_any" selected><fmt:message key="order.status.any"/></option>
                                <c:forEach items="${applicationScope.order_status_list}" var="status">
                                    <c:choose>
                                        <c:when test="${!empty param.order_status && param.order_status eq status}">
                                            <option value="${status}" selected><fmt:message key="order.status.${status}"/></option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${status}"><fmt:message key="order.status.${status}"/></option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="nav-item mb-2">
                            <label class="form-label">Дата</label>
                            <input class="form-control" id="date" type="date" name="order_date" value="${param.order_date}" required>
                        </div>
                        <button class="btn btn-outline-success" type="submit">Найти</button>
                    </form>
                </div>
            </div>
        </nav>
        <div class="col p-3 ms-3 me-5 bg-default">
            <c:choose>
                <c:when test="${orders.isEmpty() eq 'true'}">
                    <div class="product_title"><fmt:message key="orders.no_orders"/></div>
                </c:when>
                <c:otherwise>
                <table class="table table-hover align-middle">
                    <tbody>
                        <c:forEach items="${orders}" var="order">
                        <c:if test="${order.status eq 'NEED_CONFIRMATION'}">
                            <c:set var="row_color" value="table-success"/>
                        </c:if>
                        <c:if test="${order.status eq 'CONFIRMED'}">
                            <c:set var="row_color" value="table-secondary"/>
                        </c:if>
                        <c:if test="${order.status eq 'CANCELLED'}">
                            <c:set var="row_color" value="table-danger" scope="page"/>
                        </c:if>
                        <tr class="${row_color}">
                            <td colspan="2" class="p-3 firstColumn" onclick="openModal('modal'+${order.id})">
                                <strong class="link"><fmt:message key="order"/> №${order.id}</strong>
                            </td>
                            <td colspan="2" class="p-3" onclick="openModal('modal'+${order.id})">
                                <fmt:message key="order.on"/> ${order.dateOrdered}
                            </td>
                            <td colspan="1" class="p-3" onclick="openModal('modal'+${order.id})">
                                <fmt:message key="order.price"/> ${currency}${order.totalPrice}
                            </td>
                            <td colspan="4" class="p-3 text-end" onclick="openModal('modal'+${order.id})">
                                <fmt:message key="order.status.${order.status.toString()}"/>
                            </td>
                            <td colspan="1" class="p-3 text-end">
                                <c:if test="${order.status eq 'NEED_CONFIRMATION'}">
                                    <a type="button" class="btn-confirm" title="Подтвердить" data-bs-toggle="tooltip" data-bs-placement="bottom" href="${absolutePath}/controller?command=confirm_order&order_id=${order.id}"></a>
                                </c:if>
                            </td>
                            <td colspan="1" class="p-3">
                                <c:if test="${order.status eq 'CONFIRMED' or order.status eq 'NEED_CONFIRMATION'}">
                                    <div class="align-middle">
                                        <a type="button" class="btn-close btn-dismiss" title="Отменить" data-bs-toggle="tooltip" data-bs-placement="bottom" href="${absolutePath}/controller?command=cancel_order&order_id=${order.id}"></a>
                                    </div>
                                </c:if>
                            </td>
                        </tr>

                        <div class="modal fade" id="modal${order.id}" tabindex="-1" aria-labelledby="modal${order.id}Label" aria-hidden="true">
                            <div class="modal-dialog modal-xl modal-dialog-centered modal-dialog-scrollable">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="modal${order.id}Label">Информация о заказе №${order.id}</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
                                    </div>
                                    <div class="modal-body p-4">
                                        <div class="row justify-content-between">
                                            <div class="col">
                                                <c:forEach items="${order.components}" var="component">
                                                    <div class="row m-3 align-items-start">
                                                        <div class="col-md-6 text-center">
                                                            <a href="${absolutePath}/controller?command=open_item_page&item_id=${component.item.id}" class="cart_image">
                                                                <img src="${absolutePath}/images/${component.item.imagePath}" alt="${component.item.name}">
                                                            </a>
                                                        </div>
                                                        <div class="col-md-3 text-center">
                                                            <a href="${absolutePath}/controller?command=open_item_page&item_id=${component.item.id}" class="link">
                                                                <strong class="product_title row">${component.item.name}</strong>
                                                            </a>
                                                            <p class="row">
                                                                <c:if test="${!empty component.itemSize && component.itemSize.id != 1}">
                                                                    <fmt:message key="item.size"/>: ${component.itemSize.sizeName}
                                                                </c:if>
                                                            </p>
                                                        </div>
                                                        <div class="col-md-3 text-center">
                                                            <strong class="link"><fmt:message key="order.amount"/></strong>
                                                            <p>${component.amount}</p>
                                                        </div>
                                                        <hr/>
                                                    </div>
                                                </c:forEach>
                                            </div>
                                            <div class="col-5 verticalLine">
                                                <div class="p-3">
                                                    <strong>Информация о покупателе</strong>
                                                    <div class="row mb-3">
                                                            ${order.customer.name} ${order.customer.surname}
                                                    </div>
                                                    <div class="row justify-content-between mb-3">
                                                        <div class="col">
                                                            Адрес электронной почты
                                                        </div>
                                                        <div class="col text-end">${order.customer.email}</div>
                                                    </div>
                                                    <div class="row justify-content-between mb-3">
                                                        <div class="col">
                                                            Номер телефона
                                                        </div>
                                                        <div class="col text-end">${order.customer.phoneNumber}</div>
                                                    </div>
                                                    <div class="row justify-content-between mb-3">
                                                        <div class="col">
                                                            Адрес доставки
                                                        </div>
                                                        <div class="col text-end">${order.address.country} ${order.address.city}</div>
                                                    </div>
                                                    <div class="row justify-content-between mb-3">
                                                        <div class="col">
                                                            Стоимость заказа
                                                        </div>
                                                        <div class="col text-end">${order.totalPrice}</div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="me-3">
                                            <fmt:message key="order.status.${order.status.toString()}"/>
                                        </div>
                                        <c:if test="${order.status eq 'CANCELLED'}">
                                            <button class="btn btn-success" disabled>Подтвердить</button>
                                            <button class="btn btn-danger" disabled>Отменить</button>
                                        </c:if>
                                        <c:if test="${order.status eq 'CONFIRMED'}">
                                            <button class="btn btn-success" disabled>Подтвердить</button>
                                            <a type="button" class="btn btn-danger" href="${absolutePath}/controller?command=cancel_order&order_id=${order.id}">Отменить</a>
                                        </c:if>
                                        <c:if test="${order.status eq 'NEED_CONFIRMATION'}">
                                            <a type="button" class="btn btn-success" href="${absolutePath}/controller?command=confirm_order&order_id=${order.id}">Подтвердить</a>
                                            <a type="button" class="btn btn-danger" href="${absolutePath}/controller?command=cancel_order&order_id=${order.id}">Отменить</a>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="container">
                    <nav aria-label="${page_navigation}">
                        <ul class="pagination pagination-lg justify-content-center">
                            <form id="paginationPrevious" action="${absolutePath}/controller">
                                <input type="hidden" name="command" value="upload_orders">
                                <c:if test="${not empty param.order_status}">
                                    <input type="hidden" name="order_status" value="${param.order_status}">
                                </c:if>
                                <c:if test="${not empty param.order_date}">
                                    <input type="hidden" name="order_date" value="${param.order_date}">
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
                                <input type="hidden" name="command" value="upload_orders">
                                <c:if test="${not empty param.order_status}">
                                    <input type="hidden" name="order_status" value="${param.order_status}">
                                </c:if>
                                <c:if test="${not empty param.order_date}">
                                    <input type="hidden" name="order_date" value="${param.order_date}">
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
    </div>
    <div class="text-center fixed-bottom py-3">
        <ctg:custom-footer/>
    </div>
</div>
<script>
    function openModal(name) {
        let modal = new bootstrap.Modal(document.getElementById(name));
        modal.show();
    }
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl, {
            delay: { "show": 1000, "hide": 100 }
        })
    })
</script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>