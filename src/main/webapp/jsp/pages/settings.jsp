<%--
  Created by IntelliJ IDEA.
  User: Veta0
  Date: 10.05.2022
  Time: 16:01
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
<fmt:message key="profile.change_setting" var="change_setting"/>
<fmt:message key="action.close" var="close"/>
<c:set var="defaultFullAddress" scope="page" value="${customer.defaultAddress.isPresent() eq 'true' ? customer.defaultAddress.get() : null}"/>
<c:set var="defaultCountry" scope="page" value="${defaultFullAddress != null ? defaultFullAddress.country.toString() : null}"/>
<c:set var="defaultCity" scope="page" value="${defaultFullAddress != null ? defaultFullAddress.city : null}"/>
<c:set var="defaultAddress" scope="page" value="${defaultFullAddress != null ? defaultFullAddress.address : null}"/>
<c:set var="defaultApartment" scope="page" value="${defaultFullAddress != null ? (defaultFullAddress.apartment.isPresent() ? defaultFullAddress.apartment.get() : null) : null}"/>
<c:set var="defaultPostalCode" scope="page" value="${defaultFullAddress != null ? defaultFullAddress.postalCode : null}"/>
<html>
<head>
    <title><fmt:message key="title.settings"/></title>
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
    <style>
        html,body{
            width:100%;
            height:100%;
        }
        .table, td{
            padding: 0.75rem;
        }
        .link, .link:visited {
            text-decoration: none;
            color: #525252;
        }
        .link:hover {
            color: #525252;
            text-decoration: underline;
        }
        .firstColumn {
            color: gray;
        }
    </style>
</head>
<body>
<div class="page">
    <header>
        <%@include file="header/headerCommon.jsp"%>
    </header>
    <div class="modal fade" id="modalFirstName" tabindex="-1" aria-labelledby="modalFirstNameLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalFirstNameLabel"><fmt:message key="profile.change_setting.name"/></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Закрыть"></button>
                </div>
                <div class="modal-body m-4">
                    <form role="form" action="${absolutePath}/controller" method="post" class="needs-validation" novalidate>
                        <input type="hidden" name="command" value="change_setting"/>
                        <input type="hidden" name="setting" value="first_name"/>
                        <label class="form-label my-2"><fmt:message key="profile.new_name"/></label>
                        <input type="text" name="user_name" class="form-control my-2" required pattern="^[A-Za-zА-Яа-я]{3,50}$">
                        <div id="firstNameHelp" class="form-text my-2 mx-2"><fmt:message key="registration.correct_first_name"/></div>
                        <c:if test="${!empty invalid_name}">
                            <div class="invalid-feedback-backend" style="color: red">
                                <fmt:message key="${invalid_name}"/>
                            </div>
                        </c:if>
                        <div class="invalid-feedback">
                            <fmt:message key="registration.invalid_first_name"/>
                        </div>
                        <button type="submit" class="btn btn-success mt-4">${change_setting}</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="modalLastName" tabindex="-1" aria-labelledby="modalLastNameLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalLastNameLabel"><fmt:message key="profile.change_setting.surname"/></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="${close}"></button>
                </div>
                <div class="modal-body m-4">
                    <form role="form" action="${absolutePath}/controller" method="post" class="needs-validation" novalidate>
                        <input type="hidden" name="command" value="change_setting"/>
                        <input type="hidden" name="setting" value="last_name"/>
                        <label class="form-label my-2"><fmt:message key="profile.new_surname"/></label>
                        <input type="text" name="user_surname" class="form-control my-2" required pattern="^[A-Za-zА-Яа-я]{3,50}$">
                        <div id="surnameHelp" class="form-text my-2 mx-2"><fmt:message key="registration.correct_surname"/></div>
                        <c:if test="${!empty invalid_surname}">
                            <div class="invalid-feedback-backend" style="color: red">
                                <fmt:message key="${invalid_surname}"/>
                            </div>
                        </c:if>
                        <div class="invalid-feedback">
                            <fmt:message key="registration.invalid_surname"/>
                        </div>
                        <button type="submit" class="btn btn-success mt-4">${change_setting}</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="modalPhone" tabindex="-1" aria-labelledby="modalPhoneLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalPhoneLabel"><fmt:message key="profile.change_setting.phone"/></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="${close}"></button>
                </div>
                <div class="modal-body m-4">
                    <form role="form" action="${absolutePath}/controller" method="post" class="needs-validation" novalidate>
                        <input type="hidden" name="command" value="change_setting"/>
                        <input type="hidden" name="setting" value="phone_number"/>
                        <label class="form-label my-2"><fmt:message key="profile.new_phone"/></label>
                        <input type="tel" name="phone_number" class="form-control my-2" required pattern="+375(29|25|44|33)\d{7}">
                        <div id="phoneHelp" class="form-text my-2 mx-2"><fmt:message key="registration.correct_phone_number"/></div>
                        <c:if test="${!empty invalid_phone_number}">
                            <div class="invalid-feedback-backend" style="color: red">
                                <fmt:message key="${invalid_phone_number}"/>
                            </div>
                        </c:if>
                        <div class="invalid-feedback">
                            <fmt:message key="registration.invalid_phone_number"/>
                        </div>
                        <button type="submit" class="btn btn-success mt-4">${change_setting}</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="modalPassword" tabindex="-1" aria-labelledby="modalPasswordLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalPasswordLabel"><fmt:message key="profile.change_setting.password"/></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="${close}"></button>
                </div>
                <div class="modal-body m-4">
                    <form role="form" action="${absolutePath}/controller" method="post" class="needs-validation" novalidate>
                        <input type="hidden" name="command" value="change_setting"/>
                        <input type="hidden" name="setting" value="password"/>
                        <div class="form-group">
                            <label class="form-label my-2"><fmt:message key="profile.old_password"/></label>
                            <input type="password" name="user_old_password" class="form-control my-2" required pattern="^[A-Za-zА-Яа-я0-9\._*]{5,40}$">
                            <c:if test="${!empty invalid_old_password}">
                                <div class="invalid-feedback-backend" style="color: red">
                                    <fmt:message key="${invalid_old_password}"/>
                                </div>
                            </c:if>
                            <div class="invalid-feedback">
                                <fmt:message key="registration.invalid_password"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-label my-2"><fmt:message key="profile.new_password"/></label>
                            <input type="password" name="user_new_password" class="form-control my-2" required pattern="^[A-Za-zА-Яа-я0-9\._*]{5,40}$">
                            <div id="passHelp" class="form-text my-2 mx-2"><fmt:message key="registration.correct_password"/></div>
                            <c:if test="${!empty invalid_password}">
                                <div class="invalid-feedback-backend" style="color: red">
                                    <fmt:message key="${invalid_password}"/>
                                </div>
                            </c:if>
                            <div class="invalid-feedback">
                                <fmt:message key="registration.invalid_password"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="form-label my-2"><fmt:message key="profile.repeat_new_password"/></label>
                            <input type="password" name="user_repeat_password" class="form-control my-2" required pattern="^[A-Za-zА-Яа-я0-9\._*]{5,40}$">
                            <c:if test="${!empty invalid_repeat_password}">
                                <div class="invalid-feedback-backend" style="color: red">
                                    <fmt:message key="${invalid_repeat_password}"/>
                                </div>
                            </c:if>
                            <div class="invalid-feedback">
                                <fmt:message key="registration.invalid_password"/>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-success mt-4">${change_setting}</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="modalAddress" tabindex="-1" aria-labelledby="modalAddressLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="modalAddressLabel"><fmt:message key="profile.change_setting.address"/></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="${close}"></button>
                </div>
                <div class="modal-body m-4">
                    <form role="form" action="${absolutePath}/controller" method="post" class="needs-validation" novalidate>
                        <input type="hidden" name="command" value="add_shipping_address"/>
                        <input type="hidden" name="update_address" value="update"/>
                        <div class="form-group">
                            <label class="form-label my-2"><fmt:message key="order.label.country"/></label>
                            <br/>
                            <select class="form-select" name="country" required>
                                <c:forEach items="${applicationScope.country_list}" var="country">
                                    <c:choose>
                                        <c:when test="${address_data_map.country eq country}">
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
                            <label class="form-label my-2"><fmt:message key="order.label.city"/></label>
                            <input type="text" name="city" class="form-control" value="${address_data_map.city}" required pattern="^[A-Za-zА-Яа-я\-]{2,20}$">
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
                            <label class="form-label my-2"><fmt:message key="order.label.address"/></label>
                            <input type="text" name="address" class="form-control" value="${address_data_map.address}" required pattern="^[\wА-Яа-я\h\.,/]{5,50}$">
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
                            <label class="form-label my-2"><fmt:message key="order.label.apartment"/></label>
                            <input type="text" name="apartment" class="form-control value="${address_data_map.apartment}" pattern="^(\d{1,5}|\d{1,4}[A-Za-zА-Яа-я])$">
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
                            <label class="form-label my-2"><fmt:message key="order.label.postal_code"/></label>
                            <input type="text" name="postal_code" class="form-control form-control-sm" value="${address_data_map.postal_code}" required pattern="^[\dA-Za-z]{3,10}$">
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
                        <button type="submit" class="btn btn-success mt-4">${change_setting}</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <div style="width: 80%; padding-left: 20%">
        <table class="table mt-3">
        <tbody>
            <tr>
                <td colspan="1" class="p-3 firstColumn">
                    <fmt:message key="form.label.first_name"/>
                </td>
                <td colspan="2" class="p-3">
                    ${customer.name}
                </td>
                <td class="p-3">
                    <a class="link" data-bs-toggle="modal" href="#modalFirstName" aria-expanded="false" aria-controls="modalFirstName">
                        ${change_setting}
                    </a>
                </td>
            </tr>
            <tr>
                <td colspan="1" class="p-3 firstColumn">
                    <fmt:message key="form.label.last_name"/>
                </td>
                <td colspan="2" class="p-3">
                    ${customer.surname}
                </td>
                <td class="p-3">
                    <a class="link" data-bs-toggle="modal" href="#modalLastName" aria-expanded="false" aria-controls="modalLastName">
                        ${change_setting}
                    </a>
                </td>
            </tr>
            <tr>
                <td colspan="1" class="p-3 firstColumn">
                    <fmt:message key="form.label.email"/>
                </td>
                <td colspan="3" class="p-3">
                    ${customer.email}
                </td>
            </tr>
            <tr>
                <td colspan="1" class="p-3 firstColumn">
                    <fmt:message key="form.label.phone"/>
                </td>
                <td colspan="2" class="p-3">
                    ${customer.phoneNumber}
                </td>
                <td class="p-3">
                    <a class="link" data-bs-toggle="modal" href="#modalPhone" aria-expanded="false" aria-controls="modalPhone">
                        ${change_setting}
                    </a>
                </td>
            </tr>
            <tr>
                <td colspan="3" class="p-3 firstColumn">
                    <fmt:message key="form.label.password"/>
                </td>
                <td class="p-3">
                    <a class="link" data-bs-toggle="modal" href="#modalPassword" aria-expanded="false" aria-controls="modalPassword">
                        ${change_setting}
                    </a>
                </td>
            </tr>
            <tr>
                <td colspan="1" class="p-3 firstColumn">
                    <fmt:message key="order.shipping_address"/>
                </td>
                <td colspan="2" class="p-3">
                    <c:choose>
                        <c:when test="${defaultFullAddress == null}">
                        </c:when>
                        <c:otherwise>
                            <fmt:message key="country.${defaultCountry}"/> ${defaultCity}, ${defaultAddress}, ${defaultApartment} <fmt:message key="order.label.postal_code"/>: ${defaultPostalCode}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td class="p-3">
                    <a class="link" data-bs-toggle="modal" href="#modalAddress" aria-expanded="false" aria-controls="modalAddress">
                        ${change_setting}
                    </a>
                </td>
            </tr>
        </tbody>
    </table>
    </div>
    <div class="text-center py-3">
        <ctg:custom-footer/>
    </div>
</div>
<script>
    var invalidName = '${invalid_name}';
    if(invalidName.length !== 0) {
        var firstNameModal = new bootstrap.Modal(document.getElementById('modalFirstName'), {
            keyboard: false
        });
        firstNameModal.show();
    }

    var invalidSurname = '${invalid_surname}';
    if(invalidSurname.length !== 0) {
        var surnameModal = new bootstrap.Modal(document.getElementById('modalLastName'), {
            keyboard: false
        });
        surnameModal.show();
    }

    var invalidPhone = '${invalid_phone_number}';
    if(invalidPhone.length !== 0) {
        var phoneModal = new bootstrap.Modal(document.getElementById('modalPhone'), {
            keyboard: false
        });
        phoneModal.show();
    }

    var invalidRepeatPassword = '${invalid_repeat_password}';
    var invalidOldPassword = '${invalid_old_password}';
    var invalidPassword = '${invalid_password}';
    if(invalidRepeatPassword.length !== 0 || invalidOldPassword.length !== 0 || invalidPassword.length !== 0){
        var passwordModal = new bootstrap.Modal(document.getElementById('modalPassword'), {
            keyboard: false
        });
        passwordModal.show();
    }

    var invalidCountry = '${invalid_country}';
    var invalidCity = '${invalid_city}';
    var invalidAddress = '${invalid_address}';
    var invalidApartment = '${invalid_apartment}';
    var invalidPostalCode = '${invalid_postal_code}';
    if(invalidCountry.length !== 0 || invalidCity.length !== 0 || invalidAddress.length !== 0 || invalidApartment.length !== 0 ||
        invalidPostalCode.length !== 0) {
        var addressModal = new bootstrap.Modal(document.getElementById('modalAddress'), {
            keyboard: false
        });
        addressModal.show();
    }

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

