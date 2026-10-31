package com.hospital.management.service;

import com.hospital.management.model.LabTest;
import com.hospital.management.repository.LabTestRepository;
import com.hospital.management.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LabTestServiceImpl implements LabTestService {

    @Autowired
    private LabTestRepository labTestRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public LabTest createLabTest(LabTest labTest) {
        return labTestRepository.save(labTest);
    }

    @Override
    public List<LabTest> getAllLabTests() {
        return labTestRepository.findByActiveTrue();
    }

    @Override
    public Optional<LabTest> getLabTestById(Long id) {
        return labTestRepository.findByIdAndActiveTrue(id);
    }

    @Override
    public LabTest updateLabTest(Long id, LabTest labTestDetails) {
        return labTestRepository.findByIdAndActiveTrue(id).map(labTest -> {
            labTest.setTestName(labTestDetails.getTestName());
            labTest.setTestFee(labTestDetails.getTestFee());
            labTest.setResult(labTestDetails.getResult());
            labTest.setStatus(labTestDetails.getStatus());
            
            // Update patient if provided
            if (labTestDetails.getPatient() != null && labTestDetails.getPatient().getId() != null) {
                patientRepository.findById(labTestDetails.getPatient().getId()).ifPresent(labTest::setPatient);
            }

            return labTestRepository.save(labTest);
        }).orElse(null);
    }

    @Override
    public void deleteLabTest(Long id) {
        labTestRepository.findByIdAndActiveTrue(id).ifPresent(labTest -> {
            labTest.setActive(false);
            labTestRepository.save(labTest);
        });
    }
}

