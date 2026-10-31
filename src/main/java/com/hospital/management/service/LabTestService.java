package com.hospital.management.service;

import com.hospital.management.model.LabTest;

import java.util.List;
import java.util.Optional;

public interface LabTestService {
    
    LabTest createLabTest(LabTest labTest);
    
    List<LabTest> getAllLabTests();
    
    Optional<LabTest> getLabTestById(Long id);
    
    LabTest updateLabTest(Long id, LabTest labTestDetails);
    
    void deleteLabTest(Long id);
}

