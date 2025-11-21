<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="row mb-4 justify-content-center">
	<div class="col-md-6">
		<form action="<c:url value='/index'/>" method="get"
			class="d-flex shadow-sm rounded overflow-hidden bg-white">

			<input class="form-control border-0 py-2 ps-4" type="search"
				name="keyword" placeholder="Nhập tên video bạn muốn tìm..."
				value="${searchKeyword}" aria-label="Search">

			<button class="btn fw-bold px-4" type="submit"
				style="background-color: #ff9900; color: white; border-radius: 0;">
				<i class="fa-solid fa-magnifying-glass"></i> Search
			</button>

			<c:if test="${not empty searchKeyword}">
				<a href="<c:url value='/index'/>" class="btn btn-secondary px-3"
					style="border-radius: 0; line-height: 2.2;"> <i
					class="fa-solid fa-xmark"></i>
				</a>
			</c:if>
		</form>
	</div>
</div>

<c:if test="${not empty message}">
	<div class="alert alert-info text-center mb-4 shadow-sm border-0"
		style="background-color: #e3f2fd; color: #0d47a1;">
		<i class="fa-solid fa-circle-info me-2"></i> ${message}
	</div>
</c:if>

<div class="row">
	<c:forEach var="video" items="${videos}">

		<div class="col-md-4 mb-4">
			<div class="card video-card h-100">

				<a href="<c:url value='/detail?id=${video.id}'/>"
					class="position-relative text-decoration-none"> <img
					class="card-img-top" src="${video.poster}" alt="${video.title}"
					onerror="this.src='https://placehold.co/600x400?text=No+Image'">

					<div
						class="position-absolute top-50 start-50 translate-middle text-white opacity-75"
						style="pointer-events: none;">
						<i class="fa-regular fa-circle-play fa-3x"></i>
					</div>
				</a>

				<div class="card-body d-flex flex-column">
					<h5 class="card-title mb-1">
						<a href="<c:url value='/detail?id=${video.id}'/>"
							class="text-dark text-decoration-none fw-bold">
							${video.title} </a>
					</h5>

					<small class="text-muted mb-3"> <i
						class="fa-solid fa-eye me-1"></i> <fmt:formatNumber
							value="${video.views}" /> views
					</small>

					<div class="mt-auto pt-2 border-top d-flex justify-content-between">
						<a href="<c:url value='/like?id=${video.id}'/>"
							class="btn btn-light btn-sm flex-grow-1 me-2 fw-bold text-success">
							<i class="fa-regular fa-thumbs-up"></i> Like
						</a> <a href="<c:url value='/share?id=${video.id}'/>"
							class="btn btn-light btn-sm flex-grow-1 fw-bold text-warning">
							<i class="fa-solid fa-share"></i> Share
						</a>
					</div>
				</div>
			</div>
		</div>

	</c:forEach>

	<c:if test="${empty videos}">
		<div class="col-12 text-center py-5">
			<img src="https://cdn-icons-png.flaticon.com/512/7486/7486754.png"
				width="100" alt="Not found" class="mb-3 opacity-50">
			<p class="text-muted fs-5">Không tìm thấy video nào phù hợp.</p>
		</div>
	</c:if>
</div>

<c:if test="${empty searchKeyword && totalPages > 1}">
	<nav aria-label="Page navigation"
		class="mt-4 mb-5 d-flex justify-content-center">
		<ul class="pagination shadow-sm">

			<%-- Nút Về Trang Đầu --%>
			<li class="page-item ${currentPage == 1 ? 'disabled' : ''}"><a
				class="page-link" href="<c:url value='/index?page=1'/>"> <i
					class="fa-solid fa-angles-left"></i>
			</a></li>

			<%-- Nút Lùi 1 Trang --%>
			<li class="page-item ${currentPage == 1 ? 'disabled' : ''}"><a
				class="page-link"
				href="<c:url value='/index?page=${currentPage - 1}'/>"> <i
					class="fa-solid fa-angle-left"></i>
			</a></li>

			<%-- Vòng lặp số trang --%>
			<c:forEach begin="1" end="${totalPages}" var="i">
				<li class="page-item ${currentPage == i ? 'active' : ''}"><a
					class="page-link" href="<c:url value='/index?page=${i}'/>">${i}</a>
				</li>
			</c:forEach>

			<%-- Nút Tiến 1 Trang --%>
			<li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
				<a class="page-link"
				href="<c:url value='/index?page=${currentPage + 1}'/>"> <i
					class="fa-solid fa-angle-right"></i>
			</a>
			</li>

			<%-- Nút Về Trang Cuối --%>
			<li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
				<a class="page-link"
				href="<c:url value='/index?page=${totalPages}'/>"> <i
					class="fa-solid fa-angles-right"></i>
			</a>
			</li>

		</ul>
	</nav>
</c:if>