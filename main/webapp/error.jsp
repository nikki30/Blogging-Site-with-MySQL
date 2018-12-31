<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Invalid Request</title>
</head>
<body>
    <h1>Invalid Request</h1>

    <h2>Request</h2>
    <b>action:</b>  <c:out value="${userAction}"/><br>
    <b>username:</b> <c:out value="${username}"/><br>
    <b>postid:</b> <c:out value="${postid}"/><br>
    <b>title:</b> <c:out value="${title}"/><br>
    <b>body:</b> <c:out value="${body}"/><br>

    <h2>Reason of Error</h2>
    <c:out value="${errorMsg}"/>
</body>
</html>

