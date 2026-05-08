package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    User findByPhone(String phone);
    Optional<User> findById(long id);
    User findByUserCode(String userCode);
}
