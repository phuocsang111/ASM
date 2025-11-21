<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark py-3">
    <div class="container">
        <a class="navbar-brand fw-bold text-warning" href="<c:url value='/admin'/>">
            ADMINISTRATION TOOL
        </a>
        
        <div class="collapse navbar-collapse">
            <ul class="navbar-nav ms-auto">
                <li class="nav-item"><a class="nav-link" href="<c:url value='/admin'/>">HOME</a></li>
                <li class="nav-item"><a class="nav-link" href="<c:url value='/admin/videos'/>">VIDEOS</a></li>
                <li class="nav-item"><a class="nav-link" href="<c:url value='/admin/users'/>">USERS</a></li>
                <li class="nav-item"><a class="nav-link" href="<c:url value='/admin/reports'/>">REPORTS</a></li>
                <li class="nav-item ms-3"><a class="nav-link text-danger" href="<c:url value='/index'/>">Exit to Website</a></li>
            </ul>
        </div>
    </div>
</nav>