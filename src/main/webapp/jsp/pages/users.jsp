<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 20.06.2022
  Time: 16:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ctg" uri="customs"%>
<c:set var="absolutePath">${pageContext.request.contextPath}</c:set>
<c:choose>
    <c:when test="${not empty language}"> <fmt:setLocale value="${language}" scope="session"/></c:when>
    <c:when test="${empty language}"> <fmt:setLocale value="${language = 'ru_RU'}" scope="session"/></c:when>
</c:choose>
<fmt:setBundle basename="language.language"/>
<fmt:message key="action.page_navigation" var="page_navigation"/>
<fmt:message key="action.previous" var="previous"/>
<fmt:message key="action.next" var="next"/>
<html>
<head>
    <title><fmt:message key="title.users"/></title>
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
    <div id="toastError" class="toast align-items-center text-white bg-danger position-fixed bottom-0 end-0 m-3" style="z-index: 11" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
            <div class="toast-body">
                <fmt:message key="${error_delete_admin}"/>
            </div>
            <button type="button" class="btn-close me-2 m-auto" data-bs-dismiss="toast" aria-label="Закрыть"></button>
        </div>
    </div>
    <div class="row justify-content-between px-5 me-0">
<%--        <nav class="navbar navbar-expand-lg col-3 flex-column position-sticky">--%>
<%--            <div class="container-fluid p-3">--%>
<%--                <div class="container-fluid bg-light">--%>
<%--                    <form name="sortByPrice" action="${absolutePath}/controller" novalidate>--%>
<%--                        <input type="hidden" name="command" value="upload_orders">--%>
<%--                        <div class="nav-item my-3">--%>
<%--                            <strong>Фильтр</strong>--%>
<%--                        </div>--%>
<%--                        <div class="nav-item mb-2">--%>
<%--                            <label class="form-label">Статус</label>--%>
<%--                            <select class="form-select" name="order_status" required>--%>
<%--                                <option value="order_status_any" selected><fmt:message key="order.status.any"/></option>--%>
<%--                                <c:forEach items="${applicationScope.order_status_list}" var="status">--%>
<%--                                    <c:choose>--%>
<%--                                        <c:when test="${!empty param.order_status && param.order_status eq status}">--%>
<%--                                            <option value="${status}" selected><fmt:message key="order.status.${status}"/></option>--%>
<%--                                        </c:when>--%>
<%--                                        <c:otherwise>--%>
<%--                                            <option value="${status}"><fmt:message key="order.status.${status}"/></option>--%>
<%--                                        </c:otherwise>--%>
<%--                                    </c:choose>--%>
<%--                                </c:forEach>--%>
<%--                            </select>--%>
<%--                        </div>--%>
<%--                        <div class="nav-item mb-2">--%>
<%--                            <label class="form-label">Дата</label>--%>
<%--                            <input class="form-control" id="date" type="date" name="order_date" value="${param.order_date}" required>--%>
<%--                        </div>--%>
<%--                        <button class="btn btn-outline-success" type="submit">Найти</button>--%>
<%--                    </form>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </nav>--%>
        <div class="p-5 bg-default">
            <c:choose>
                <c:when test="${user_list.isEmpty() eq 'true'}">
                    <div class="product_title"><fmt:message key="users.no_users"/></div>
                </c:when>
                <c:otherwise>
                    <table class="table table-hover align-middle">
                        <thead>
                        <tr>
                            <th class="p-3"><fmt:message key="form.label.first_name"/> </th>
                            <th class="p-3"><fmt:message key="form.label.last_name"/> </th>
                            <th class="p-3"><fmt:message key="form.label.email"/> </th>
                            <th class="p-3"><fmt:message key="users.user_status"/> </th>
                            <th class="p-3"><fmt:message key="users.user_action"/> </th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${user_list}" var="user">
                            <c:choose>
                                <c:when test="${user.status eq 'BLOCKED'}">
                                    <c:set var="row_color" value="table-danger"/>
                                </c:when>
                                <c:otherwise>
                                    <c:set var="row_color" value="table-default"/>
                                </c:otherwise>
                            </c:choose>
                            <tr class="${row_color}">
                                <td colspan="1" class="p-3">${user.name}</td>
                                <td colspan="1" class="p-3">${user.surname}</td>
                                <td colspan="1" class="p-3">${user.email}</td>
                                <td colspan="1" class="p-3 text-end">
                                    <fmt:message key="user.status.${user.status.toString()}"/>
                                </td>
                                <td>
                                    <div class="row">
                                        <c:choose>
                                            <c:when test="${user.role eq 'ADMIN'}">
                                                <div class="col">
                                                    <form action="${absolutePath}/controller" method="post">
                                                        <input type="hidden" name="command" value="delete_admin">
                                                        <input type="hidden" name="user_id" value="${user.id}">
                                                        <c:choose>
                                                            <c:when test="${user.id == sessionScope.user.id}">
                                                                <button type="submit" class="btn btn-danger" disabled><fmt:message key="action.delete"/></button>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <button type="submit" class="btn btn-danger"><fmt:message key="action.delete"/></button>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </form>
                                                </div>
                                            </c:when>
                                            <c:when test="${user.role eq 'CUSTOMER'}">
                                                <div class="col">
                                                    <form action="${absolutePath}/controller" method="post">
                                                        <input type="hidden" name="user_id" value="${user.id}">
                                                        <input type="hidden" name="command" value="change_user_status">
                                                        <c:choose>
                                                            <c:when test="${user.status eq 'BLOCKED'}">
                                                                <input type="hidden" name="user_status" value="ACTIVE">
                                                                <button type="submit" class="btn btn-dark"><fmt:message key="action.unblock"/></button>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <input type="hidden" name="user_status" value="BLOCKED">
                                                                <button type="submit" class="btn btn-dark"><fmt:message key="action.block"/></button>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </form>
                                                </div>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
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
    var success = '${success}';
    var error = '${error_delete_admin}';
    if(success.length !== 0) {
        var toastSuccess = bootstrap.Toast.getOrCreateInstance(document.getElementById('toastSuccess'));
        toastSuccess.show();
    }
    if(error.length !== 0) {
        var toastError = bootstrap.Toast.getOrCreateInstance(document.getElementById('toastError'));
        toastError.show();
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
</body>
</html>