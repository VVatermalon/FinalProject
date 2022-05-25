<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 23.02.2022
  Time: 19:09
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
<fmt:message var="e_country" key="input.placeholder.country"/>
<fmt:message var="e_city" key="input.placeholder.city"/>
<fmt:message var="e_address" key="input.placeholder.address"/>
<fmt:message var="e_apartment" key="input.placeholder.apartment"/>
<fmt:message var="e_postal_code" key="input.placeholder.postal_code"/>
<c:set var="defaultFullAddress" scope="page" value="${customer.defaultAddress.isPresent() eq 'true' ? customer.defaultAddress.get() : null}"/>
<c:set var="defaultCountry" scope="page" value="${defaultFullAddress != null ? defaultFullAddress.country.toString() : null}"/>
<c:set var="defaultCity" scope="page" value="${defaultFullAddress != null ? defaultFullAddress.city : null}"/>
<c:set var="defaultAddress" scope="page" value="${defaultFullAddress != null ? defaultFullAddress.address : null}"/>
<c:set var="defaultApartment" scope="page" value="${defaultFullAddress != null ? (defaultFullAddress.apartment.isPresent() ? defaultFullAddress.apartment.get() : null) : null}"/>
<c:set var="defaultPostalCode" scope="page" value="${defaultFullAddress != null ? defaultFullAddress.postalCode : null}"/>
<html>
<head>
    <title><fmt:message key="title.ordering"/></title>
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
    <div class="container justify-content-center col-12 col-sm-6 mt-3">
        <h3 class="text-center p-3"><fmt:message key="order.shipping_address"/></h3>
        <form role="form" action="${absolutePath}/controller" method="post" class="needs-validation" novalidate>
            <input type="hidden" name="command" value="add_shipping_address"/>
            <div class="row gy-3">
                <div class="form-group">
                    <label class="form-label"><fmt:message key="order.label.country"/></label>
                    <br/>
                    <select class="form-select" name="country" required>
                        <c:forEach items="${applicationScope.country_list}" var="country">
                            <c:choose>
                                <c:when test="${!empty address_data_map ? address_data_map.country eq country : defaultCountry eq country}">
                                    <option value="${country}" selected><fmt:message key="country.${country}"/></option>
                                </c:when>
                                <c:otherwise>
                                    <option value="${country}"><fmt:message key="country.${country}"/></option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                    <c:if test="${!empty invalid_country}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_country}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="order.invalid_country"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label"><fmt:message key="order.label.city"/></label>
                    <input type="text" name="city" value="${!empty address_data_map ? address_data_map.city : defaultCity}" class="form-control" placeholder="${e_city}" required pattern="^[A-Za-zА-Яа-я\-]{2,20}$">
                    <div class="form-text"><fmt:message key="order.correct_city"/></div>
                    <c:if test="${!empty invalid_city}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_city}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="order.invalid_city"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label"><fmt:message key="order.label.address"/></label>
                    <input type="text" name="address" value="${!empty address_data_map ? address_data_map.address : defaultAddress}" class="form-control" placeholder="${e_address}" required pattern="^[\wА-Яа-я\h\.,/]{5,50}$">
                    <div class="form-text"><fmt:message key="order.correct_address"/></div>
                    <c:if test="${!empty invalid_address}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_address}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="order.invalid_address"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label"><fmt:message key="order.label.apartment"/></label>
                    <input type="text" name="apartment" value="${!empty address_data_map ? address_data_map.apartment : defaultApartment}" class="form-control" placeholder="${e_apartment}" pattern="^(\d{1,5}|\d{1,4}[A-Za-zА-Яа-я])$">
                    <div class="form-text"><fmt:message key="order.correct_apartment"/></div>
                    <c:if test="${!empty invalid_apartment}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_apartment}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="order.invalid_apartment"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="form-label"><fmt:message key="order.label.postal_code"/></label>
                    <input type="text" name="postal_code" value="${!empty address_data_map ? address_data_map.postal_code : defaultPostalCode}" class="form-control form-control-sm" placeholder="${e_postal_code}" required pattern="^[\dA-Za-z]{3,10}$">
                    <div class="form-text"><fmt:message key="order.correct_postal_code"/></div>
                    <c:if test="${!empty invalid_postal_code}">
                        <div class="invalid-feedback-backend" style="color: red">
                            <fmt:message key="${invalid_postal_code}"/>
                        </div>
                    </c:if>
                    <div class="invalid-feedback">
                        <fmt:message key="order.invalid_postal_code"/>
                    </div>
                </div>
                <div class="form-group">
                    <input type="checkbox" id="saveForLater" name="save_for_later">
                    <label class="form-label" for="saveForLater"><fmt:message key="order.label.save_for_later"/></label>
                </div>
                <div class="row gy-3 justify-content-evenly">
                    <div class="col text-center mb-3">
                        <a class="btn btn-info" role="button" href="${absolutePath}/controller?command=upload_cart"><fmt:message key="back.cart"/></a>
                    </div>
                    <div class="col text-center mb-3">
                        <button type="submit" class="btn btn-success"><fmt:message key="order.continue_to_payment"/></button>
                    </div>
                </div>
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

