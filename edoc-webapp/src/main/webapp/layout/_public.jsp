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
    <!-- Bootstrap Core CSS -->
    <link href="<c:url value="/asset/css/bootstrap.min.css"/>" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;1,300&display=swap" rel="stylesheet">
    <!-- DataTables CSS -->
    <link href="<c:url value="/asset/css/dataTables/jquery.dataTables.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/asset/css/dataTables/dataTables.bootstrap.min.css"/>" rel="stylesheet">
    <!-- DataTables Responsive CSS -->
    <link href="<c:url value="/asset/css/dataTables/responsive.dataTables.min.css"/>" rel="stylesheet">
    <link href="<c:url value="/asset/css/dataTables/responsive.bootstrap.min.css"/>" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="<c:url value="/asset/css/font-awesome.min.css"/>" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="<c:url value="/asset/css/jquery.datetimepicker.min.css"/>">
    <!-- Custom CSS -->
    <link href="<c:url value="/asset/css/startmin.css"/>" rel="stylesheet">
</head>
<body>
    <div id="wrapper">
        <tiles:insertAttribute name="body"/>
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
    <!-- jQuery -->
    <script src="<c:url value="/asset/js/jquery.min.js"/>"></script>
    <script src="<c:url value="/asset/js/message.js"/>"></script>
    <script src="<c:url value="/asset/js/notify.min.js"/>"></script>
    <%--datetimepicker--%>
    <script src="<c:url value="/asset/js/jquery.datetimepicker.full.js"/>"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="<c:url value="/asset/js/bootstrap.min.js"/>"></script>
    <!-- Custom Theme JavaScript -->
    <script src="<c:url value="/asset/js/report.js" />"></script>
    <script src="<c:url value="/asset/js/trace.js" />"></script>
    <script src="<c:url value="/asset/js/datetime.js" />"></script>
    <%--JQuery template--%>
    <script src="<c:url value="/asset/js/jquery.tmpl.min.js"/>"></script>
    <!-- DataTables JavaScript -->
    <script src="<c:url value="/asset/js/dataTables/jquery.dataTables.min.js"/>"></script>
    <script src="<c:url value="/asset/js/dataTables/dataTables.bootstrap.min.js"/>"></script>
    <script src="<c:url value="/asset/js/dataTables/dataTables.responsive.min.js"/>"></script>
    <script src="<c:url value="/asset/js/dataTables/responsive.bootstrap.min.js"/>"></script>
</body>
</html>
