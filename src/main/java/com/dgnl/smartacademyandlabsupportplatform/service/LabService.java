package com.dgnl.smartacademyandlabsupportplatform.service;

import com.dgnl.smartacademyandlabsupportplatform.model.entity.Lab;

import java.util.List;

public interface LabService{
    List<Lab> getAll();
    Lab getById(Long id);
}
