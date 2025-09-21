<%-- File: student-sidebar.jsp --%>
<aside class="sidebar">
    <div class="sidebar-header">
        <h2>Student Portal</h2>
    </div>
    <nav class="sidebar-nav">
        <a href="${pageContext.request.contextPath}/student/dashboard" class="${param.activePage == 'dashboard' ? 'active' : ''}">Dashboard</a>
        <a href="#" class="${param.activePage == 'history' ? 'active' : ''}">My Attendance</a>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>
</aside>