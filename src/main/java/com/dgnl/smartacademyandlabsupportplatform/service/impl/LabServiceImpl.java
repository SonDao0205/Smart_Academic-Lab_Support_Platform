package com.dgnl.smartacademyandlabsupportplatform.service.impl;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lab;
import com.dgnl.smartacademyandlabsupportplatform.repository.LabRepository;
import com.dgnl.smartacademyandlabsupportplatform.service.LabService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabServiceImpl implements LabService {
    private final LabRepository labRepository;
    public LabServiceImpl(LabRepository labRepository) {
        this.labRepository = labRepository;
    }
    @Override
    public List<Lab> getAll() {
        return labRepository.findAll();
    }
}
