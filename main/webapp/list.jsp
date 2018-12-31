<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
	<link rel="stylesheet" href="./style.css">
    <title>List Page</title>
</head>
<body>
    <span/>
    <form>
        <form action="open" id="0">
            <input type="hidden" name="username" value=<c:out value="${username}"/>>
            <input type="hidden" name="postid" value=0>
            <input type="hidden" name="newOpenFlag" value="true">
            <button type="submit" name="action" id="NewPost" value="open" class="btn btn-info center-block">New Post</button>
        </form>
    </form>
    <table style="margin-top:10px">
        <tr>
            <th>  Title  </th>
            <th>  Created  </th>
            <th>  Modified  </th>
            <th>  Options </th>
        </tr>

        <c:forEach items="${listOfPosts}" var="post">

        <tr>
        <form action="post" method = "POST" id=<c:out value="${postId}"/> >
            <input type="hidden" name="username" value= '<c:out value="${post.username}"/>' >
            <input type="hidden" name="postid" value= '<c:out value="${post.postId}"/>' >
            <input type="hidden" name="body" value= '<c:out value="${post.body}"/>' >
            <input type="hidden" name="title" value= '<c:out value="${post.title}"/>'>
            <td>
                <c:out value="${post.title}"/>
            </td>
            <td>
                <!--fmt:formatDate value="${post.createdDate}"  /-->
                <c:out value="${post.createdDate}"/>
            </td>
            <td>
                <!--fmt:formatDate value="${post.modifiedDate}" /-->
                <c:out value="${post.modifiedDate}"/>
            </td>
            <td>
                <button type="submit" name="action" value="open" class="btn btn-success">Open</button>
                <button type="submit" name="action" value="delete" class="btn btn-danger">Delete</button>
            </td>
        </form>
        </tr>
        </c:forEach>
    </table>
</body>
</html>
