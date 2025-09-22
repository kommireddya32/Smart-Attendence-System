<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Students - Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="sidebar.jsp">
            <jsp:param name="activePage" value="students"/>
        </jsp:include>

        <main class="main-content">
            <header class="main-header">
                <h1>Manage Students</h1>
                <div class="header-actions">
                    <form action="/admin/students" method="GET" class="search-bar">
                        <input type="text" name="keyword" placeholder="Search by name..." value="${param.keyword}">
                        <button type="submit">Search</button>
                    </form>
                    <a href="${pageContext.request.contextPath}/admin/students/add" class="btn btn-primary">Add New Student</a>
                </div>
            </header>

            <section class="content-table">
                <table>
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Name</th>
                            <th>Email</th>
                            <th>Department</th>
                            <th>Password</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="student" items="${students}" varStatus="loop">
                            <tr>
                                <td>${loop.count}</td>
                                <td>${student.name}</td>
                                <td>${student.email}</td>
                                <td>${student.department}</td>
                                <td>${student.password}</td> 
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/admin/students/edit?id=${student.id}" class="btn-action btn-edit">Edit</a>
									<form action="/admin/students/delete" method="POST" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this student?');">
                                        <input type="hidden" name="id" value="${student.id}">
                                        <button type="submit" class="btn-action btn-delete">Delete</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </section>
        </main>
    </div>
</body>
</html>