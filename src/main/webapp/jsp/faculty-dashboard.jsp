<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Faculty Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/faculty-style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="faculty-sidebar.jsp">
            <jsp:param name="activePage" value="facultyDashboard"/>
        </jsp:include>

        <main class="main-content">
            <header class="main-header">
                <h1>Faculty Dashboard</h1>
            </header>

            <section id="start-session-view">
                <div class="content-form" style="max-width: 500px;">
                    <h2>Start New Attendance Session</h2>
                    <form id="start-session-form">
                        <div class="form-group">
                            <label for="department">Select Department</label>
                            <select id="department" name="department" required>
                                <option value="" disabled selected>Select a department...</option>
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
                            <button type="submit" class="btn btn-primary">Start Live Session</button>
                        </div>
                    </form>
                </div>
            </section>

            <section id="live-session-view" style="display: none;">
                <div class="live-session-card">
                    <h2><span id="live-session-title"></span></h2>
                    <div class="live-indicator">
                        <span class="dot"></span> Live (Refreshes every 3 seconds)
                    </div>
                    <div id="qr-code-container">
                        <img id="qr-code-image" src="" alt="Live QR Code">
                    </div>
                    <button id="stop-session-btn" class="btn btn-delete">Stop Session</button>
                </div>
            </section>
        </main>
    </div>

<script>
    const basePath = '${pageContext.request.contextPath}';

    const startSessionForm = document.getElementById('start-session-form');
    const startSessionView = document.getElementById('start-session-view');
    const liveSessionView = document.getElementById('live-session-view');
    const liveSessionTitle = document.getElementById('live-session-title');
    const qrCodeImage = document.getElementById('qr-code-image');
    const stopSessionBtn = document.getElementById('stop-session-btn');
    const departmentSelect = document.getElementById('department'); // Relies on id="department"

    let refreshInterval = null;

    startSessionForm.addEventListener('submit', function (e) {
        e.preventDefault();
        const department = departmentSelect.value; // Reads from the select tag

        if (!department) {
            alert("Please select a department.");
            return;
        }

        liveSessionTitle.textContent = `Live Session - ${department}`;
        startSessionView.style.display = 'none';
        liveSessionView.style.display = 'block';

        fetchQrCode(department);
        refreshInterval = setInterval(() => fetchQrCode(department), 3000);
    });

    function fetchQrCode(department) {
    const timestamp = new Date().getTime();
    
    // 1. Declare the apiUrl variable BEFORE the switch statement
    let apiUrl = ''; 

    // 2. Use the lowercase 'switch' keyword
    switch (department) {
        case "CSE":
            // 3. Assign the value, don't re-declare with const
            apiUrl = `${basePath}/faculty/api/qrcode/generate?department=CSE&t=${timestamp}`;
            break;
        case "AIML":
            apiUrl = `${basePath}/faculty/api/qrcode/generate?department=AIML&t=${timestamp}`;
            break;
        case "CSD":
            apiUrl = `${basePath}/faculty/api/qrcode/generate?department=CSD&t=${timestamp}`;
            break;
        case "CSC":
            apiUrl = `${basePath}/faculty/api/qrcode/generate?department=CSC&t=${timestamp}`;
            break;
        case "ECE":
            apiUrl = `${basePath}/faculty/api/qrcode/generate?department=ECE&t=${timestamp}`;
            break;
        case "MECH":
            apiUrl = `${basePath}/faculty/api/qrcode/generate?department=MECH&t=${timestamp}`;
            break;
        case "IT":
            apiUrl = `${basePath}/faculty/api/qrcode/generate?department=IT&t=${timestamp}`;
            break;
        case "EEE":
            apiUrl = `${basePath}/faculty/api/qrcode/generate?department=EEE&t=${timestamp}`;
            break;
        default:
            // Handle cases where the department might be unknown
            console.error("Unknown department:", department);
            return; 
    }

    // Now the apiUrl variable is accessible here
    fetch(apiUrl)
        .then(response => {
            if (!response.ok) throw new Error("Failed to generate QR.");
            return response.blob();
        })
        .then(blob => {
            qrCodeImage.src = URL.createObjectURL(blob);
        })
        .catch(err => {
            console.error(err);
            alert(err.message);
            stopSession();
        });
}

    stopSessionBtn.addEventListener('click', function () {
        stopSession();
    });

    function stopSession() {
        clearInterval(refreshInterval);
        refreshInterval = null;
        startSessionView.style.display = 'block';
        liveSessionView.style.display = 'none';
        qrCodeImage.src = "";
    }
</script>
</body>
</html>