package com.dgnl.smartacademyandlabsupportplatform.repository;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Equipment;
import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("SELECT e FROM Equipment e WHERE e.isDeleted = false")
    List<Equipment> findAllActive();

    @Query("SELECT e FROM Equipment e WHERE e.id = :id AND e.isDeleted = false")
    Optional<Equipment> findActiveById(@Param("id") Long id);

    @Query("SELECT e FROM Equipment e WHERE e.lab.id = :labId AND e.isDeleted = false")
    List<Equipment> getActiveEquipmentByLabId(@Param("labId") Long labId);

    @Modifying
    @Query("UPDATE Equipment e SET e.isDeleted = true WHERE e.id = :id")
    void softDeleteById(@Param("id") Long id);
}
