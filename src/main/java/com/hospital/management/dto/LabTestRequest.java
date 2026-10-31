package com.hospital.management.dto;

public class LabTestRequest {
    
    private String testName;
    private Double testFee;
    private String result;
    private String status;
    private Long patientId;

    public LabTestRequest() {
    }

    public LabTestRequest(String testName, Double testFee, String result, String status, Long patientId) {
        this.testName = testName;
        this.testFee = testFee;
        this.result = result;
        this.status = status;
        this.patientId = patientId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Double getTestFee() {
        return testFee;
    }

    public void setTestFee(Double testFee) {
        this.testFee = testFee;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }
}

