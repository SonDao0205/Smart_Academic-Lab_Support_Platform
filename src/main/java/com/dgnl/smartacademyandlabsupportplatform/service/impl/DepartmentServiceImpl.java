package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Department;
import com.dgnl.smartacademyandlabsupportplatform.repository.DeparmentRepository;
import com.dgnl.smartacademyandlabsupportplatform.service.DepartmentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DeparmentRepository deparmentRepository;
    public DepartmentServiceImpl(DeparmentRepository deparmentRepository) {
        this.deparmentRepository = deparmentRepository;
    }
    @Override
    public List<Department> getAll() {
        return deparmentRepository.findAll();
    }
}
