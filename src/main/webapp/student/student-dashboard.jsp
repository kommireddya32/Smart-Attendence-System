<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/student-style.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;500;600&display=swap" rel="stylesheet">
    <script src="https://unpkg.com/html5-qrcode" type="text/javascript"></script>
</head>
<body>
    <div class="dashboard-container">
        <jsp:include page="student-sidebar.jsp">
            <jsp:param name="activePage" value="dashboard" />
        </jsp:include>

        <main class="main-content">
            <header class="main-header">
                <h1>Student Dashboard</h1>
            </header>

            <section class="scanner-section">
                <button id="start-scan-btn" class="btn btn-primary">Scan Attendance QR Code</button>
                
                <div id="scan-result"></div>

                <div id="qr-reader" style="display: none;"></div>
                <button id="stop-scan-btn" class="btn btn-secondary" style="display: none;">Stop Scanning</button>
            </section>

            <section class="content-table" style="margin-top: 30px;">
                <h2>My Attendance History</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Date</th>
                            <th>Department</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="record" items="${attendanceHistory}">
                            <tr>
                                <td>${record.attendanceDate}</td>
                                <td>${record.faculty.department}</td>
                                <td>${record.status}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </section>
        </main>
    </div>

    <%-- Your JavaScript does not need any changes. It already targets the 'scan-result' div. --%>
    <script>
        const startScanBtn = document.getElementById('start-scan-btn');
        const stopScanBtn = document.getElementById('stop-scan-btn');
        const qrReaderDiv = document.getElementById('qr-reader');
        const scanResultDiv = document.getElementById('scan-result');
        const basePath = '${pageContext.request.contextPath}';
        const html5QrCode = new Html5Qrcode("qr-reader");

        const qrCodeSuccessCallback = (decodedText, decodedResult) => {
            sendAttendanceData(decodedText);
        };

        const config = { fps: 10, qrbox: { width: 250, height: 250 } };

        startScanBtn.addEventListener('click', () => {
            scanResultDiv.innerHTML = '';
            qrReaderDiv.style.display = 'block';
            stopScanBtn.style.display = 'inline-block';
            startScanBtn.style.display = 'none';
            html5QrCode.start({ facingMode: "environment" }, config, qrCodeSuccessCallback);
        });

        stopScanBtn.addEventListener('click', () => {
            html5QrCode.stop().then(ignore => {
                qrReaderDiv.style.display = 'none';
                stopScanBtn.style.display = 'none';
                startScanBtn.style.display = 'block';
                scanResultDiv.innerHTML = '';
            }).catch(err => console.error("Failed to stop the scanner.", err));
        });

        function sendAttendanceData(qrData) {
            const apiUrl = `${basePath}/student/mark-attendance`;
            html5QrCode.pause();
            scanResultDiv.innerHTML = `<p>Processing...</p>`;

            fetch(apiUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ qrData: qrData })
            })
            .then(response => {
                if (!response.ok) throw new Error(`Server Error: ${response.status}`);
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    html5QrCode.stop().then(() => {
                        qrReaderDiv.style.display = 'none';
                        stopScanBtn.style.display = 'none';
                        startScanBtn.style.display = 'block';
                        scanResultDiv.innerHTML = `<p class="success">${data.message}</p>`;
                    });
                } else {
                    scanResultDiv.innerHTML = `<p class="error">${data.message}</p>`;
                    html5QrCode.resume();
                }
            })
            .catch(err => {
                console.error("Error sending attendance data:", err);
                scanResultDiv.innerHTML = `<p class="error">${err.message}</p>`;
                html5QrCode.resume();
            });
        }
    </script>
</body>
</html>