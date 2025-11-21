package com.poly.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.poly.dao.FavoriteDAO;
import com.poly.dao.VideoDAO;
import com.poly.entity.Favorite;
import com.poly.entity.User;
import com.poly.entity.Video;

@WebServlet(urlPatterns = {"/favorites", "/like", "/unlike"})
public class FavoriteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private FavoriteDAO favoriteDAO;
    private VideoDAO videoDAO;

    public FavoriteServlet() {
        super();
        this.favoriteDAO = new FavoriteDAO();
        this.videoDAO = new VideoDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        User user = (User) request.getSession().getAttribute("user");
        
        if (uri.contains("/favorites")) {
            // Hiển thị trang "My Favorites"
            List<Favorite> favorites = favoriteDAO.findByUser(user.getId());
            request.setAttribute("videos", favorites); // Gửi danh sách favorites
            request.setAttribute("view", "/site/home/favorites.jsp");
            
        } else if (uri.contains("/like")) {
            // Xử lý "Like" video
            handleLike(request, response, user);
            // Quay lại trang chi tiết (hoặc trang chủ)
            response.sendRedirect(request.getContextPath() + "/detail?id=" + request.getParameter("id"));
            return; // Dừng lại để tránh forward
            
        } else if (uri.contains("/unlike")) {
            // Xử lý "Unlike" video (từ trang favorites)
            handleUnlike(request, response, user);
            // Tải lại trang favorites
            response.sendRedirect(request.getContextPath() + "/favorites");
            return; // Dừng lại để tránh forward
        }

        request.getRequestDispatcher("/site/layout.jsp").forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Có thể dùng doPost cho Like/Unlike nếu dùng form
        doGet(request, response);
    }

    private void handleLike(HttpServletRequest request, HttpServletResponse response, User user) {
        String videoId = request.getParameter("id");
        if (videoId == null) return;
        
        Video video = videoDAO.findById(Video.class, videoId);
        
        // Kiểm tra xem đã like chưa
        if (!favoriteDAO.isFavorited(user.getId(), videoId)) {
            Favorite fav = new Favorite();
            fav.setUser(user);
            fav.setVideo(video);
            favoriteDAO.create(fav);
        }
    }
    
    private void handleUnlike(HttpServletRequest request, HttpServletResponse response, User user) {
        String videoId = request.getParameter("id");
        if (videoId == null) return;
        
        Favorite fav = favoriteDAO.findFavorite(user.getId(), videoId);
        if (fav != null) {
            favoriteDAO.delete(fav);
        }
    }
}