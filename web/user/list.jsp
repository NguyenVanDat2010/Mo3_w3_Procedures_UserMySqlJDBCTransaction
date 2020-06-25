<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: VCOM
  Date: 25/06/2020
  Time: 10:40 SA
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List User</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<h1>List User</h1>
<p>
    <a href="/users">Home</a>
    <a href="/users?action=create">Create new user</a>
</p>

<form method="post" action="/users?action=search">
    <input type="text" name="searchValue" placeholder="Enter your word">
    <input type="submit" name="search" value="Search user">
</form>

<form method="post" action="/users?action=sort">
    <select style="width: 150px; height: 30px;" name="sortBy">
        <option>Sort by</option>
        <option value="id">Id</option>
        <option value="name">Name</option>
        <option value="email">Email</option>
        <option value="country">Country</option>
    </select>
    <SELECT STYLE="width: 150px; height: 30px;"name="styleSort">
        <option>Style sort</option>
        <option value="asc">ASC</option>
        <option value="desc">DESC</option>
    </SELECT>
    <button type="submit">Sort</button>
<%--    <a href="/users?action=sort"><button type="submit">Sort</button></a>--%>
</form>

<table>
    <tr>
        <th>Name</th>
        <th>Email</th>
        <th>Country</th>
        <th>Edit</th>
        <th>Delete</th>
    </tr>
    <c:forEach items="${requestScope['users']}" var="user">
        <tr>
            <td>${user.getName()}</td>
            <td>${user.getEmail()}</td>
            <td>${user.getCountry()}</td>
            <td><a href="/users?action=edit&id=${user.getId()}"><button type="button">Edit</button></a></td>
            <td><a href="/users?action=delete&id=${user.getId()}"><button type="button">Delete</button></a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
