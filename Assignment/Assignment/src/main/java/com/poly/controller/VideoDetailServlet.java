package com.poly.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.poly.dao.VideoDAO;
import com.poly.entity.Video;

@WebServlet("/detail")
public class VideoDetailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VideoDAO videoDAO;

    public VideoDetailServlet() {
        super();
        this.videoDAO = new VideoDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String videoId = request.getParameter("id");
        
        if (videoId != null) {
            Video video = videoDAO.findById(Video.class, videoId);
            
            if (video != null) {
                // Tăng lượt xem (theo spec [Poster].Click)
                video.setViews(video.getViews() + 1);
                videoDAO.update(video);
                
                request.setAttribute("video", video);
                
                // (Xử lý logic lấy các video đã xem từ cookie)
                
                // (Xử lý lấy các video liên quan ở sidebar)
                
                request.setAttribute("view", "/site/home/detail.jsp");
            }
        }
        
        request.getRequestDispatcher("/site/layout.jsp").forward(request, response);
    }
}