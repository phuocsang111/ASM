<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav class="navbar navbar-expand-lg header-nav bg-success">
	<div class="container">
		<a class="navbar-brand" href="<c:url value='/index'/>"> <i
			class="fa-solid fa-play-circle me-2"></i>POLY ENTERTAINMENT
		</a>

		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarNav">
			<span class="navbar-toggler-icon"></span>
		</button>

		<div class="collapse navbar-collapse" id="navbarNav">
			<ul class="navbar-nav ms-auto align-items-center">

				<c:if test="${not empty sessionScope.user}">
					<li class="nav-item text-white"><a class="nav-link"
						href="<c:url value='/favorites'/>">My Favorites</a></li>
				</c:if>

				<li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle custom-link text-white" href="#" role="button" data-bs-toggle="dropdown">
                        My Account
                    </a>
                    
                    <ul class="dropdown-menu custom-dropdown">
                        
                        <c:if test="${empty sessionScope.user}">
                            <li><a class="dropdown-item" href="<c:url value='/login'/>">Login</a></li>
                            <li><a class="dropdown-item" href="<c:url value='/forgot-password'/>">Forgot Password</a></li>
                            <li><a class="dropdown-item" href="<c:url value='/register'/>">Registration</a></li>
                        </c:if>
                        
                        <c:if test="${not empty sessionScope.user}">
                            <li><a class="dropdown-item" href="<c:url value='/logout'/>">Logoff</a></li>
                            <li><a class="dropdown-item" href="<c:url value='/change-password'/>">Change Password</a></li>
                            <li><a class="dropdown-item" href="<c:url value='/edit-profile'/>">Edit Profile</a></li>
                        </c:if>

                        </ul>
                </li>
			</ul>
		</div>
	</div>
</nav>