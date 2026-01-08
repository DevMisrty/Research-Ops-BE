package com.example.practiceddatajpa.services;

import com.example.practiceddatajpa.model.Insurance;
import com.example.practiceddatajpa.model.Patient;
import com.example.practiceddatajpa.repo.InsuranceRepository;
import com.example.practiceddatajpa.repo.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final PatientRepository patientRepository;

    @Transactional
    public void assignInsuranceToPatient(Insurance insurance, UUID patientId){
        Patient patient = patientRepository.findById(patientId).orElseThrow(()-> new RuntimeException("Patient not found"));
        patient.setInsurance(insurance);// owning side, so we need to set it.

        insurance.setPatient(patient);// bidirectional consistency maintained
        patientRepository.save(patient);// as it is cascade, and always save from the OWNER side, when dealing with relationship persistency.
    }

}
