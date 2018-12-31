<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" href="./style.css">
    <title>Edit Post</title>
</head>
<body>
    <div class="division"><h1>Edit Post</h1></div>
    <form action = "post" method = "POST">
        <div class="division">
            <button type="submit" name = "action" value = "save" class="btn btn-light">Save</button>
            <button type="submit" name = "action" value = "list" class="btn btn-light">Close</button>
            <button type="submit" name = "action" value = "preview" class="btn btn-light">Preview</button>
            <button type="submit" name = "action" value = "delete" class="btn btn-light">Delete</button>
        </div>
         <input type="hidden" name="username" value= '<c:out value="${username}"/>' >
         <input type="hidden" name="postid" value= '<c:out value="${postid}"/>' >
        
         <div class="division">
            <label for="title" class="deco_text">Title</label><br/>
            <input type="text" name="title" id="title" value= '<c:out value="${title}"/>' >
        </div>
        <div class="division">
            <label for="body"  class="deco_text">Body</label><br/>
            <textarea style="height: 30rem;" id="body" name="body"><c:out value="${body}"/></textarea>
        </div>
    </form>
</body>
</html>
