
<%@page import="org.gs1us.sgg.gbservice.api.Product"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<% String contextPath = request.getContextPath(); %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The meta tag below can not be altered or removed -->
        <meta name="gs1-template" content="gs1-v1.0"/>
        <meta name="format-detection" content="telephone=no">
        <title><%= request.getParameter("pageTitle") %></title>
        
        <link rel='SHORTCUT ICON' href='<%= contextPath + "/images/favicon.ico" %>' type='image/x-icon' />

        <link rel="stylesheet" type="text/css" href="//cloud.typography.com/6247692/752844/css/fonts.css" />
        <link href='//fonts.googleapis.com/css?family=Source+Code+Pro:500' rel='stylesheet' type='text/css' />

        <link href="<%= contextPath + "/css/bootstrap.min.css" %>" rel="stylesheet" />
        <link href="<%= contextPath + "/css/bootstrap-theme.min.css" %>" rel="stylesheet" />
        <link href="<%= contextPath + "/css/bootstrap-accessibility.css" %>" rel="stylesheet" />
        <link href="<%= contextPath + "/css/sgg.css" %>" rel="stylesheet" />

        
    </head>

    <body>
    <!-- Google Tag Manager -->
<noscript><iframe src="//www.googletagmanager.com/ns.html?id=GTM-P2JJVS"
height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript>
<script>(function(w,d,s,l,i){w[l]=w[l]||[];w[l].push({'gtm.start':
new Date().getTime(),event:'gtm.js'});var f=d.getElementsByTagName(s)[0], j=d.createElement(s),dl=l!='dataLayer'?'&l='+l:'';j.async=true;j.src=
'//www.googletagmanager.com/gtm.js?id='+i+dl;f.parentNode.insertBefore(j,f);
})(window,document,'script','dataLayer','GTM-P2JJVS');</script>
<!-- End Google Tag Manager -->
    
        <div class="toolkit">
		<header>
		</header>


			<div class="container-fluid site-content">

