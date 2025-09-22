<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Smart Attendance System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin-style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
    <div class="dashboard-container">
        
        <jsp:include page="sidebar.jsp">
            <jsp:param name="activePage" value="dashboard"/>
        </jsp:include>

        <main class="main-content">
            <header class="main-header">
                <h1>Dashboard Overview</h1>
            </header>

            <section class="stat-cards">
                <div class="card card-students">
                    <h4>Total Students</h4>
                    <p>${totalStudents}</p>
                </div>
                <div class="card card-faculty">
                    <h4>Total Faculty</h4>
                    <p>${totalFaculty}</p>
                </div>
                <div class="card card-departments">
                    <h4>Departments</h4>
                    <p>8</p>
                </div>
            </section>

            <section class="activity-feed">
                <h2>Recent Activity</h2>
                <div class="activity-list">
                    <div class="activity-item">
                        <span class="activity-icon">👤</span>
                        <p>New faculty member 'Dr. Smith' was added.</p>
                        <span class="activity-time">2h ago</span>
                    </div>
                    <div class="activity-item">
                        <span class="activity-icon">✏️</span>
                        <p>Student 'John Doe' details were updated.</p>
                        <span class="activity-time">5h ago</span>
                    </div>
                </div>
            </section>
        </main>
    </div>
</body>
</html>