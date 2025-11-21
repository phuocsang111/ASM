package com.poly.dao;

import com.poly.entity.Video;
import java.util.List;
import jakarta.persistence.TypedQuery; // Nhớ dùng jakarta

public class VideoDAO extends AbstractDAO<Video> {
    public VideoDAO() {
        super();
    }

    // 1. Hàm lấy danh sách video có phân trang
    public List<Video> findAll(int pageNumber, int pageSize) {
        String jsql = "SELECT o FROM Video o WHERE o.active = true";
        TypedQuery<Video> query = em.createQuery(jsql, Video.class);
        
        // Tính vị trí bắt đầu: (Trang hiện tại - 1) * Kích thước trang
        query.setFirstResult((pageNumber - 1) * pageSize); 
        query.setMaxResults(pageSize);
        
        return query.getResultList();
    }

    // 2. Hàm đếm tổng số video đang hoạt động
    public long count() {
        String jsql = "SELECT count(o) FROM Video o WHERE o.active = true";
        TypedQuery<Long> query = em.createQuery(jsql, Long.class);
        return query.getSingleResult();
    }
    
    // Giữ lại hàm findTop6ByViews nếu muốn dùng cho slide hoặc mục nổi bật
    public List<Video> findTop6ByViews() {
        String jsql = "SELECT o FROM Video o WHERE o.active = true ORDER BY o.views DESC";
        TypedQuery<Video> query = em.createQuery(jsql, Video.class);
        query.setMaxResults(6);
        return query.getResultList();
    }
    public List<Video> findByTitle(String keyword) {
        String jsql = "SELECT o FROM Video o WHERE o.title LIKE :keyword AND o.active = true";
        TypedQuery<Video> query = em.createQuery(jsql, Video.class);
        
        // Sử dụng % để tìm kiếm gần đúng (chứa từ khóa)
        query.setParameter("keyword", "%" + keyword + "%");
        
        return query.getResultList();
    }
}