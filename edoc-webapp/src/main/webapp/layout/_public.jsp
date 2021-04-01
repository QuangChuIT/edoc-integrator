<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@page isELIgnored="true" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content=`"">
    <meta name="author" content="">
    <%--<link rel="shortcut icon" href="<c:url value="/images/favicon.ico"/>" type="image/x-icon">
    <link rel="icon" href="<c:url value="/images/favicon.ico"/>" type="image/x-icon">--%>
    <title><tiles:getAsString name="title"/></title>
    <link href="<c:url value="/asset/css/bootstrap.min.css"/>" rel="stylesheet">
    <!-- DataTables CSS -->
    <link href="<c:url value="/asset/css/dataTables/jquery.dataTables.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/asset/css/dataTables/dataTables.bootstrap.min.css"/>" rel="stylesheet">
    <!-- DataTables Responsive CSS -->
    <link href="<c:url value="/asset/css/dataTables/responsive.dataTables.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/asset/css/dataTables/responsive.bootstrap.min.css"/>" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;1,300&display=swap"
    rel="stylesheet">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <!-- Custom Fonts -->
    <link href="<c:url value="/asset/css/font-awesome.min.css"/>" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="<c:url value="/asset/css/jquery.datetimepicker.min.css"/>">
    <link href="<c:url value="/asset/css/select2.min.css"/>" rel="stylesheet"/>
    <!-- Custom CSS -->
    <link href="<c:url value="/asset/css/report.css"/>" rel="stylesheet">
    <link href="<c:url value="/asset/css/startmin.css"/>" rel="stylesheet">

    <!--TreeGrid CSS-->
    <link href="<c:url value="/asset/css/TreeGrid/ej.web.all.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/asset/css/TreeGrid/ej.responsive.css"/>" rel="stylesheet">
</head>
<body>
<div id="wrapper">
    <tiles:insertAttribute name="body"/>
</div>
<div id="overlay-public">
    <div class="cv-spinner">
        <span class="loading-spinner"></span>
        <span>&nbsp;Đang xử lý</span>
    </div>
</div>
<script id="edocPublicStatTmpl" type="text/x-jquery-tmpl">
    <div>
        <a href="<c:url value="/public/stat/detail"/>">
            <div id="publicContent">
                <p class="before">${String.format(app_message.edoc_before_content, getCurrentMonth() + 1)}</p>
                <p id="all" class="main">${addCommas(total + 0)}</p>
                <p class="after">${app_message.edoc_after_first_content}</p>
                <p class="after">${String.format(app_message.edoc_after_second_content,totalOrgan)}</p>
                <p class="sub">${String.format(app_message.edoc_after_last_content,dateTime)}</p>
            </div>
        </a>
    </div>
</script>
<script id="edocPublicTraceTmpl" type="text/x-jquery-tmpl">
    <form class="form-horizontal" action="javascript:void(0)">
        <div class="form-group">
            <div class="col-md-2 col-xs-3">
                <label class="control-label " for="subjectInfo">${app_message.edoc_table_header_subject}</label>
            </div>
            <div class="col-md-10 col-xs-9">
                <textarea class="form-control" readonly rows="2" id="subjectInfo">${subject}</textarea>
            </div>
        </div>
        <div class="form-group">
            <div class="col-md-2 col-xs-3">
                <label class="control-label " for="toOrganInfo">
                    ${app_message.edoc_organ_process}
                </label>
            </div>
            <div class="col-md-10 col-xs-9">
                <textarea class="form-control" readonly rows="3" id="toOrganInfo">${toOrganName}</textarea>
            </div>
        </div>
        <div class="form-group trace-area">
            <div class="col-md-12 col-sm-12">
                <span class="field-title">
                    ${app_message.edoc_trace}
                </span>
            </div>
            <div class="row">
                <div class="col-md-12 col-xs-12 traces">
                    {{if traces.length > 0}}
                        {{each traces}}
                            <div class="form-group">
                                <div class="col-md-12 col-xs-12">
                                    <span class="from-to">${fromOrgan.name}</span>
                                </div>
                                <div class="row">
                                    <div class="staff-comment col-md-8 col-xs-7" id="trace_${traceId}">
                                            <div class="staff-handler>
                                                <span><i class="fa fa-info-circle fa-fw"></i>${app_message.edoc_main_handler}</span> <span><b>${staffName}</b></span>
                                            </div>
                                            <%--<div class="staff-comment">
                                                <span>${app_message.edoc_handler_comment}</span> <span class="comment-trace">${comment}</span>
                                            </div>--%>
                                    </div>
                                    <div class="col-md-2 col-xs-3 text-center">
                                        ${getStatusOfTrace(statusCode)}
                                    </div>
                                    <div class="col-md-1 col-xs-2 text-center">
                                        <span>${convertToDate(timeStamp).formatTime()}</span>
                                    </div>
                                    <div class="col-md-1 col-xs-12 text-right" >
                                        <span>${convertToDate(timeStamp).formatDate()}</span>
                                    </div>
                                </div>
                            </div>
                        {{/each}}
                    {{else}}
                        {{each notifications}}
                            <div class="form-group">
                                <div class="col-md-8 col-xs-12">
                                    <%--<span class="from-to">${fromOrgan.name} -> ${toOrganization.name}</span>--%>
                                    <span class="from-to">${fromOrgan.name}</span>
                                </div>
                                <div class="col-md-2 col-xs-12 text-center">
                                    {{if taken == false}}
                                        ${app_message.edoc_not_received_message}
                                    {{else}}
                                        ${app_message.edoc_received_message}
                                    {{/if}}
                                </div>
                                <div class="col-md-1 col-xs-12 text-center">
                                    <span>${convertToDate(modifiedDate).formatTime()}</span>
                                </div>
                                <div class="col-md-1 col-xs-12 text-right" >
                                    <span>${convertToDate(modifiedDate).formatDate()}</span>
                                </div>
                            </div>
                        {{/each}}
                    {{/if}}
                </div>
            </div>
        </div>
    </form>
</script>
<script src="<c:url value="/asset/js/jquery.min.js"/>"></script>
<script src="<c:url value="/asset/js/message.js"/>"></script>
<script src="<c:url value="/asset/js/jquery.cookie.js"/>"></script>
<script src="<c:url value="/asset/js/notify.min.js"/>"></script>
<script src="<c:url value="/asset/js/jquery.formatter.js"/>"></script>
<script src="<c:url value="/asset/js/select2.min.js"/>"></script>
<%--datetimepicker--%>
<script src="<c:url value="/asset/js/jquery.datetimepicker.full.js"/>"></script>
<!-- Bootstrap Core JavaScript -->
<script src="<c:url value="/asset/js/bootstrap.min.js"/>"></script>
<%--JQuery template--%>
<script src="<c:url value="/asset/js/jquery.tmpl.min.js"/>"></script>
<script src="<c:url value="/asset/js/datetime.js" />"></script>
<!-- TreeGrid datatable-->
<script src="<c:url value="/asset/js/TreeGrid/jquery.easing.1.3.min.js"/>"></script>
<script src="<c:url value="/asset/js/TreeGrid/jsrender.min.js"/>"></script>
<script src="<c:url value="/asset/js/TreeGrid/ej.web.all.min.js"/>"></script>
<%--<!-- DataTables JavaScript -->
<script src="<c:url value="/asset/js/dataTables/jquery.dataTables.min.js"/>"></script>
<script src="<c:url value="/asset/js/dataTables/dataTables.bootstrap.min.js"/>"></script>
<script src="<c:url value="/asset/js/dataTables/dataTables.responsive.min.js"/>"></script>
<script src="<c:url value="/asset/js/dataTables/responsive.bootstrap.min.js"/>"></script>--%>
<!-- Custom Theme JavaScript -->
<script src="<c:url value="/asset/js/report.js" />"></script>
<script src="<c:url value="/asset/js/trace.js" />"></script>
</body>
</html>
