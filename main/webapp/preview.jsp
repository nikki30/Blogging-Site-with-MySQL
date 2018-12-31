<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<%@ page import="java.util.*" %>
<html>
<head>
    <meta charset="UTF-8">
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link rel="stylesheet" href="./style.css">
    <title>Preview Post</title>
</head>
<body>
   <div class = "division">
   		<form id = "previewForm" action = "post" method = "POST">
   			<input name = "username" type = "hidden" value= '<c:out value="${username}"/>' >
   			<input name = "postid" type = "hidden" value= '<c:out value="${postid}"/>' >
   			<input name = "title" type = "hidden" value= '<c:out value="${title}"/>' >
   			<input name = "body" type = "hidden" value= '<c:out value="${body}"/>' >
        <input type="hidden" name="closepreviewflag" value="true">
   			<button type = "submit" name = "action" value = "open" class="btn btn-info">Close Preview</button>
   		</form>
   </div>
	<div class = "division">
		<h1 id = "title">
			${title}
		</h1>
		<div id = "body">
			 <p  class = "bodyText">${parsedText}</p>
		</div>
	</div>
</body>
</html>
