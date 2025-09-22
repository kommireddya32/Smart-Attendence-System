<%-- File: faculty-sidebar.jsp --%>
<aside class="sidebar">
    <div class="sidebar-header">
        <h2>Faculty Portal</h2>
    </div>
    <nav class="sidebar-nav">
        <%-- Link to the controller for the dashboard JSP --%>
        <a href="${pageContext.request.contextPath}/faculty/dashboard" class="${param.activePage == 'facultyDashboard' ? 'active' : ''}">Dashboard</a>

        <%-- Link directly to the HTML file --%>
        <a href="${pageContext.request.contextPath}/faculty-history.html" class="${param.activePage == 'attendanceHistory' ? 'active' : ''}">Attendance History</a>

        <%-- Link to the controller for the profile JSP --%>
        <a href="${pageContext.request.contextPath}/faculty/profile" class="${param.activePage == 'myProfile' ? 'active' : ''}">My Profile</a>

        <a href="${pageContext.request.contextPath}/faculty/logout">Logout</a>
    </nav>
</aside>