package com.poly.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.poly.dao.VideoDAO;
import com.poly.entity.Video;

@WebServlet("/index")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VideoDAO videoDAO;

    public HomeServlet() {
        super();
        this.videoDAO = new VideoDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Lấy từ khóa tìm kiếm
        String keyword = request.getParameter("keyword");
        
        List<Video> videos;
        
        // 2. Kiểm tra logic: Nếu có tìm kiếm thì ưu tiên Search, ngược lại thì Phân trang
        if (keyword != null && !keyword.trim().isEmpty()) {
            // A. Trường hợp đang tìm kiếm (Hiện tất cả kết quả tìm được)
            videos = videoDAO.findByTitle(keyword);
            request.setAttribute("message", "Kết quả tìm kiếm cho: " + keyword);
            
            // Reset phân trang để không hiện nút 1, 2 khi đang search
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            
        } else {
            // B. Trường hợp chạy bình thường (Phân trang)
            String pageParam = request.getParameter("page");
            int page = 1;
            try {
                if (pageParam != null) page = Integer.parseInt(pageParam);
            } catch (Exception e) { page = 1; }
            
            int pageSize = 6;
            long totalItems = videoDAO.count();
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            
            if (page > totalPages) page = totalPages;
            if (page < 1) page = 1;
            
            videos = videoDAO.findAll(page, pageSize);
            
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
        }
        
        // 3. Gửi dữ liệu về JSP
        request.setAttribute("videos", videos);
        request.setAttribute("searchKeyword", keyword); // Gửi lại từ khóa để hiện trong ô input
        
        request.setAttribute("view", "/site/home/index.jsp");
        request.getRequestDispatcher("/site/layout.jsp").forward(request, response);
    }
}