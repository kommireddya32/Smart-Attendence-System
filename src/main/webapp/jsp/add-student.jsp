<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Student - Admin</title>
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
                <h1>Add New Student</h1>
            </header>

            <section class="content-form">
                <form action="${pageContext.request.contextPath}/admin/students/add" method="POST">
                    <div class="form-group">
                        <label for="name">Full Name</label>
                        <input type="text" id="name" name="name" required>
                    </div>
                    <div class="form-group">
                        <label for="email">Email Address</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <div class="form-group">
    					<label for="department">Department</label>
    					<select id="department" name="department" required>
  					      	<option value="" disabled selected>Select a department</option>
					        <option value="CSE">CSE</option>
					        <option value="AIML">AIML</option>
        					<option value="CSD">CSD</option>
        					<option value="CSC">CSC</option>
        					<option value="ECE">ECE</option>
        					<option value="MECH">MECH</option>
        					<option value="IT">IT</option>
        					<option value="EEE">EEE</option>
    					</select>
					</div>
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">Save Student</button>
                        <a href="${pageContext.request.contextPath}/admin/students" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </section>
        </main>
    </div>
</body>
</html>