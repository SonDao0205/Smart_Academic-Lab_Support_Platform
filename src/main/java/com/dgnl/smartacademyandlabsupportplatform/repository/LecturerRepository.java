package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Long> {
    // Tìm giảng viên theo ID của User
    Optional<Lecturer> findByUserId(Long userId);

    // Xóa bản ghi giảng viên nếu chuyển role sang student
    void deleteByUserId(Long userId);
}