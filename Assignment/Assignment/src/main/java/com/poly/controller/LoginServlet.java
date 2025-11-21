package com.poly.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import com.poly.dao.UserDAO;
import com.poly.entity.User;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public LoginServlet() {
        super();
        this.userDAO = new UserDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.setAttribute("view", "/site/user/login.jsp");
        request.getRequestDispatcher("/site/layout.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userId = request.getParameter("username");
        String pass = request.getParameter("password");
        
        User user = userDAO.checkLogin(userId, pass);
        
        if (user != null) {
            // 1. Đăng nhập thành công: Lưu vào session
            HttpSession session = request.getSession();
            session.setAttribute("user", user); 
            
            // 2. Kiểm tra xem có link nào bị chặn trước đó không (từ AuthFilter)
            String redirectUri = (String) session.getAttribute("securityUri");
            if (redirectUri != null) {
                session.removeAttribute("securityUri");
                response.sendRedirect(redirectUri);
                return;
            }
            
            // 3. Phân quyền chuyển hướng
            if (user.getAdmin()) {
                // Nếu là Admin -> Vào trang quản trị
                response.sendRedirect(request.getContextPath() + "/admin");
            } else {
                // Nếu là User thường -> Về trang chủ
                response.sendRedirect(request.getContextPath() + "/index");
            }
            
        } else {
            // 4. Đăng nhập thất bại
            request.setAttribute("message", "Sai tên đăng nhập hoặc mật khẩu!");
            request.setAttribute("view", "/site/user/login.jsp");
            request.getRequestDispatcher("/site/layout.jsp").forward(request, response);
        }
    }
}