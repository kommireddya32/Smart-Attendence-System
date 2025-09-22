<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/faculty-style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="faculty-sidebar.jsp">
            <jsp:param name="activePage" value="myProfile"/>
        </jsp:include>

        <main class="main-content">
            <header class="main-header">
                <h1>My Profile</h1>
            </header>

            <section class="content-form" style="max-width: 600px;">
                
                <c:if test="${not empty message}">
                    <p class="${success ? 'success' : 'error'}">${message}</p>
                </c:if>

                <form action="${pageContext.request.contextPath}/faculty/profile/update" method="POST">
                    <div class="form-group">
                        <label for="name">Full Name</label>
                        <input type="text" id="name" name="name" value="${faculty.name}" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" value="${faculty.email}" readonly>
                    </div>
                    <div class="form-group">
                        <label for="department">Department</label>
                        <input type="text" id="department" name="department" value="${faculty.department}" required>
                    </div>

                    <hr style="margin: 30px 0;">
                    <h2>Change Password</h2>
                    
                    <div class="form-group">
                        <label for="currentPassword">Current Password</label>
                        <input type="password" id="currentPassword" name="currentPassword">
                    </div>
                    <div class="form-group">
                        <label for="newPassword">New Password (leave blank to keep unchanged)</label>
                        <input type="password" id="newPassword" name="newPassword">
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Save Changes</button>
                    </div>
                </form>
            </section>
        </main>
    </div>
</body>
</html>