<%--
  Created by IntelliJ IDEA.
  User: huynq
  Date: 1/28/21
  Time: 4:28 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:useBean id="now" class="java.util.Date"/>
<div class="container-fluid">
    <div class="row">
        <div class="boarder-all" id="dailycounterConvertContent">
            <div class="form-group">
                <div class="col-md-2 col-xs-2"></div>
                <div class="col-md-2 col-xs-2">
                    <label class="control-label">Từ ngày</label>
                </div>
                <div class="col-md-4 col-xs-4">
                    <input type="text" class="report-time form-control" readonly
                           id="fromStatisticDate" name="fromStatisticDate" value=""
                           placeholder="<spring:message code="edoc.search.from.date"/>">
                </div>
                <div class="col-md-4 col-xs-4"></div>
            </div>
            <br>
            <div class="form-group">
                <div class="col-md-2 col-xs-2"></div>
                <div class="col-md-2 col-xs-2">
                    <label class="control-label">
                        Đến ngày
                    </label>
                </div>
                <div class="col-md-4 col-xs-4">
                    <input type="text" class="report-time form-control"
                           readonly id="toStatisticDate" name="toStatisticDate" value=""
                           placeholder="<spring:message code="edoc.search.to.date"/>">
                </div>
                <div class="col-md-4 col-xs-4"></div>
            </div><br>
            <br>
            <div class="form-group">
                <div class="col-md-4 col-xs-4"></div>
                <div class="col-md-8 col-xs-8">
                    <button class="btn btn-info btn-report-group" id="btnRunStatisticDetail">
                        <i class="fa fa-calculator fa-fw"></i>
                        <spring:message code="edoc.report.button"/>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="overlay-statistic">
    <div class="cv-spinner">
        <span class="loading-spinner"></span>
        <span>&nbsp;Đang xử lý</span>
    </div>
</div>
