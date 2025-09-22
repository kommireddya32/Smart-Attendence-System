<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- File: src/main/webapp/WEB-INF/views/admin/sidebar.jsp --%>
<aside class="sidebar">
    <div class="sidebar-header">
        <h2>Smart Attendance</h2>
    </div>
    <nav class="sidebar-nav">
        <%-- The 'active' class will be set dynamically by each page --%>
        <a href="/admin/dashboard" class="${param.activePage == 'dashboard' ? 'active' : ''}">Dashboard</a>
        <a href="/admin/students" class="${param.activePage == 'students' ? 'active' : ''}">Manage Students</a>
        <a href="/admin/faculty" class="${param.activePage == 'faculty' ? 'active' : ''}">Manage Faculty</a>
        <a href="/logout">Logout</a>
    </nav>
</aside>