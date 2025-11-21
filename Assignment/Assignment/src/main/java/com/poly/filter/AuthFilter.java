package com.poly.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.poly.entity.User;

// Thêm urlPatterns "/admin/*" để chặn tất cả các trang quản trị
@WebFilter({"/favorites", "/like", "/share", "/admin/*"}) 
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        
        User user = (User) req.getSession().getAttribute("user");
        String error = "";

        if (user == null) {
            // 1. Chưa đăng nhập
            error = "Vui lòng đăng nhập!";
        } else if (!user.getAdmin() && uri.contains("/admin/")) {
            // 2. Đã đăng nhập nhưng KHÔNG phải Admin mà cố vào trang admin
            error = "Bạn không có quyền quản trị!";
        }

        if (!error.isEmpty()) {
            req.setAttribute("message", error);
            // Lưu lại trang muốn đến để quay lại sau khi login (nếu cần)
            req.getSession().setAttribute("securityUri", uri); 
            resp.sendRedirect(req.getContextPath() + "/login?message=" + error);
        } else {
            // Hợp lệ -> Cho qua
            chain.doFilter(request, response);
        }
    }
}