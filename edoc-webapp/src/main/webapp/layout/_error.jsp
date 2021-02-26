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
    <!-- MetisMenu CSS -->
    <link href="<c:url value="/asset/css/metisMenu.min.css"/>" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Roboto:ital,wght@0,300;0,400;0,500;0,700;1,300&display=swap" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="<c:url value="/asset/css/startmin.css"/>" rel="stylesheet">
    <!-- Custom Fonts -->
    <link href="<c:url value="/asset/css/font-awesome.min.css"/>" rel="stylesheet" type="text/css">
    <link href="<c:url value="/asset/css/error.css"/>" rel="stylesheet" type="text/css">
</head>
<body>
    <div id="wrapper">
        <div id="page-wrapper">
            <div class="col-md-12 col-sm-12 col-xs-12">
                <tiles:insertAttribute name="body"/>
            </div>
        </div>
    </div>
    <!-- jQuery -->
    <script src="<c:url value="/asset/js/jquery.min.js"/>"></script>
    <!-- Bootstrap Core JavaScript -->
    <script src="<c:url value="/asset/js/bootstrap.min.js"/>"></script>
    <!-- Metis Menu Plugin JavaScript -->
    <script src="<c:url value="/asset/js/metisMenu.min.js"/>"></script>
    <!-- Morris Charts JavaScript -->
    <script src="<c:url value="/asset/js/raphael.min.js"/>"></script>
    <!-- Custom Theme JavaScript -->
    <script src="<c:url value="/asset/js/startmin.js"/>"></script>
    <!-- Page-Level Demo Scripts - Tables - Use for reference -->
</body>
</html>
