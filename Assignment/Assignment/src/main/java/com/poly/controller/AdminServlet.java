package com.poly.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

import com.poly.dao.FavoriteDAO;
import com.poly.dao.ShareDAO;
import com.poly.dao.StatsDAO;
import com.poly.dao.UserDAO;
import com.poly.dao.VideoDAO;
import com.poly.entity.User;
import com.poly.entity.Video;

@WebServlet({
    "/admin", "/admin/videos", 
    "/admin/video/edit", "/admin/video/create", "/admin/video/update", "/admin/video/delete", "/admin/video/reset",
    "/admin/users", 
    "/admin/user/edit", "/admin/user/create", "/admin/user/update", "/admin/user/delete", "/admin/user/reset",
    "/admin/reports"
})
public class AdminServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private VideoDAO videoDAO = new VideoDAO();
    private UserDAO userDAO = new UserDAO();
    private StatsDAO statsDAO = new StatsDAO();
    private FavoriteDAO favDAO = new FavoriteDAO();
    private ShareDAO shareDAO = new ShareDAO();

    protected void service(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String uri = request.getRequestURI();
        String view = "";
        String message = "";
        
        try {
            // ==========================================
            // 1. XỬ LÝ QUẢN LÝ VIDEO
            // ==========================================
            if (uri.contains("video")) {
                Video video = new Video();
                
                if (uri.contains("edit")) {
                    String id = request.getParameter("id");
                    video = videoDAO.findById(Video.class, id);
                    
                } else if (uri.contains("create")) {
                    BeanUtils.populate(video, request.getParameterMap());
                    // Fix lỗi Active cho Create
                    if (request.getParameter("active") != null) {
                        video.setActive(Boolean.parseBoolean(request.getParameter("active")));
                    }
                    
                    if (videoDAO.findById(Video.class, video.getId()) == null) {
                        videoDAO.create(video);
                        message = "Thêm video thành công!";
                        video = new Video(); 
                    } else {
                        message = "Lỗi: Video ID đã tồn tại!";
                    }
                    
                } else if (uri.contains("update")) {
                    BeanUtils.populate(video, request.getParameterMap());
                    
                    // === FIX LỖI UPDATE KHÔNG LƯU ===
                    // Đôi khi BeanUtils không map được Radio button, ta phải set tay
                    if (request.getParameter("active") != null) {
                        video.setActive(Boolean.parseBoolean(request.getParameter("active")));
                    }
                    
                    // Kiểm tra xem ID có tồn tại không trước khi Update
                    if (videoDAO.findById(Video.class, video.getId()) != null) {
                        videoDAO.update(video);
                        message = "Cập nhật video thành công!";
                    } else {
                        message = "Lỗi: Không tìm thấy Video ID để cập nhật!";
                    }
                    
                } else if (uri.contains("delete")) {
                    String id = request.getParameter("id");
                    Video v = videoDAO.findById(Video.class, id);
                    if (v != null) {
                        videoDAO.delete(v);
                        message = "Xóa video thành công!";
                    }
                    video = new Video();
                    
                } else if (uri.contains("reset")) {
                    video = new Video();
                }
                
                request.setAttribute("video", video);
                view = "/admin/videos.jsp";
            } 
            
            // ==========================================
            // 2. XỬ LÝ QUẢN LÝ USER
            // ==========================================
            else if (uri.contains("user")) {
                User user = new User();
                
                if (uri.contains("edit")) {
                    String id = request.getParameter("id");
                    user = userDAO.findById(User.class, id);
                    
                } else if (uri.contains("create")) {
                    BeanUtils.populate(user, request.getParameterMap());
                    // Fix lỗi Admin role
                    if (request.getParameter("admin") != null) {
                        user.setAdmin(Boolean.parseBoolean(request.getParameter("admin")));
                    }
                    
                    if (userDAO.findById(User.class, user.getId()) == null) {
                        userDAO.create(user);
                        message = "Thêm User thành công!";
                        user = new User();
                    } else message = "Username đã tồn tại!";
                    
                } else if (uri.contains("update")) {
                    BeanUtils.populate(user, request.getParameterMap());
                    
                    // Fix lỗi Admin role khi Update
                    if (request.getParameter("admin") != null) {
                        user.setAdmin(Boolean.parseBoolean(request.getParameter("admin")));
                    }
                    
                    userDAO.update(user);
                    message = "Cập nhật User thành công!";
                    
                } else if (uri.contains("delete")) {
                    String id = request.getParameter("id");
                    User currentUser = (User) request.getSession().getAttribute("user");
                    if (currentUser.getId().equals(id)) {
                        message = "Không thể xóa chính mình!";
                    } else {
                        userDAO.delete(userDAO.findById(User.class, id));
                        message = "Xóa User thành công!";
                    }
                    user = new User();
                } else if (uri.contains("reset")) {
                    user = new User();
                }
                
                request.setAttribute("formUser", user);
                view = "/admin/users.jsp";
            }
            
            // ==========================================
            // 3. XỬ LÝ BÁO CÁO
            // ==========================================
            else if (uri.contains("reports")) {
                request.setAttribute("favStats", statsDAO.findVideoLikedInfo());
                request.setAttribute("vidList", videoDAO.findAll(Video.class));
                
                String vid = request.getParameter("vid");
                if (vid != null && !vid.isEmpty()) {
                    request.setAttribute("favUsers", favDAO.findByVideoId(vid));
                    request.setAttribute("vidSelected", vid);
                    request.setAttribute("tab", "tab2");
                }

                String vidShare = request.getParameter("vidShare");
                if (vidShare != null && !vidShare.isEmpty()) {
                    request.setAttribute("shareList", shareDAO.findByVideoId(vidShare));
                    request.setAttribute("vidShareSelected", vidShare);
                    request.setAttribute("tab", "tab3");
                }
                view = "/admin/reports.jsp";
            }

            // ==========================================
            // 4. ĐIỀU HƯỚNG MẶC ĐỊNH
            // ==========================================
            if (view.isEmpty()) {
                if (uri.endsWith("videos")) view = "/admin/videos.jsp";
                else if (uri.endsWith("users")) view = "/admin/users.jsp";
                else if (uri.endsWith("reports")) view = "/admin/reports.jsp";
                else view = "/admin/home.jsp";
            }
            
            if (view.contains("videos.jsp")) {
                request.setAttribute("videos", videoDAO.findAll(Video.class));
            } else if (view.contains("users.jsp")) {
                request.setAttribute("users", userDAO.findAll(User.class));
            }

        } catch (Exception e) {
            e.printStackTrace();
            message = "Lỗi: " + e.getMessage();
        }

        request.setAttribute("message", message);
        request.setAttribute("view", view);
        request.getRequestDispatcher("/admin/layout.jsp").forward(request, response);
    }
}