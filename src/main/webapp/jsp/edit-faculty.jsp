<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- File: edit-faculty.jsp --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Edit Faculty - Admin</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin-style.css">
<%-- ... other head tags ... --%>
</head>
<body>
	<div class="dashboard-container">
		<jsp:include page="sidebar.jsp">
			<jsp:param name="activePage" value="faculty" />
		</jsp:include>

		<main class="main-content">
			<header class="main-header">
				<h1>Edit Faculty: ${faculty.name}</h1>
			</header>

			<section class="content-form">
				<form
					action="${pageContext.request.contextPath}/admin/faculty/update"
					method="POST">

					<%-- Hidden input to send the faculty's ID with the form --%>
					<input type="hidden" name="id" value="${faculty.id}">

					<div class="form-group">
						<label for="name">Full Name</label> <input type="text" id="name"
							name="name" value="${faculty.name}" required>
					</div>
					<div class="form-group">
						<label for="email">Email Address</label> <input type="email"
							id="email" name="email" value="${faculty.email}" required>
					</div>
					<div class="form-group">
						<label for="department">Department</label> <select id="department"
							name="department" required>
							<option value="" disabled>Select a department</option>
							<%-- The JSTL 'if' condition adds the 'selected' attribute if the department matches --%>
							<option value="CSE"
								${faculty.department == 'CSE' ? 'selected' : ''}>CSE</option>
							<option value="AIML"
								${faculty.department == 'AIML' ? 'selected' : ''}>AIML</option>
							<option value="CSD"
								${faculty.department == 'CSD' ? 'selected' : ''}>CSD</option>
							<option value="CSC"
								${faculty.department == 'CSC' ? 'selected' : ''}>CSC</option>
							<option value="ECE"
								${faculty.department == 'ECE' ? 'selected' : ''}>ECE</option>
							<option value="MECH"
								${faculty.department == 'MECH' ? 'selected' : ''}>MECH</option>
							<option value="IT"
								${faculty.department == 'IT' ? 'selected' : ''}>IT</option>
							<option value="EEE"
								${faculty.department == 'EEE' ? 'selected' : ''}>EEE</option>
						</select>
					</div>
					<%-- Note: We don't pre-fill the password for security. Leave it blank. --%>
					<div class="form-group">
						<label for="password">New Password (leave blank to keep
							unchanged)</label> <input type="password" id="password" name="password">
					</div>

					<div class="form-actions">
						<button type="submit" class="btn btn-primary">Update
							Faculty</button>
						<a href="${pageContext.request.contextPath}/admin/faculty"
							class="btn btn-secondary">Cancel</a>
					</div>
				</form>
			</section>
		</main>
	</div>
</body>
</html>