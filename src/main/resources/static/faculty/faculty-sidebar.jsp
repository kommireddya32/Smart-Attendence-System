<%-- File: faculty-sidebar.jsp --%>
<aside class="sidebar">
    <div class="sidebar-header">
        <h2>Faculty Portal</h2>
    </div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/faculty/dashboard" class="${param.activePage == 'facultyDashboard' ? 'active' : ''}">Dashboard</a>
        <a href="${pageContext.request.contextPath}/faculty/history-page" class="${param.activePage == 'attendanceHistory' ? 'active' : ''}">Attendance History</a>
        <a href="${pageContext.request.contextPath}/faculty/profile" class="${param.activePage == 'myProfile' ? 'active' : ''}">My Profile</a>
        <a href="${pageContext.request.contextPath}/faculty/logout">Logout</a>
    </nav>
</aside>