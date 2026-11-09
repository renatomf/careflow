package com.careflow.patientservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.careflow.patientservice.dto.PatientRequestDTO;
import com.careflow.patientservice.dto.PatientResponseDTO;
import com.careflow.patientservice.exception.EmailAlreadyExistsException;
import com.careflow.patientservice.mapper.PatientMapper;
import com.careflow.patientservice.model.Patient;
import com.careflow.patientservice.repository.PatientRepository;

@Service 
public class PatientService {
  private final PatientRepository patientRepository;

  public PatientService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  public List<PatientResponseDTO> getPatients() {
    List<Patient> patients = patientRepository.findAll();

    List<PatientResponseDTO> patientResponseDTOs = patients.stream()
        .map(PatientMapper::toDTO)
        .toList();

    return patientResponseDTOs;
  }

  public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
    if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
      throw new EmailAlreadyExistsException("A patient with this email "  + " already exists." + patientRequestDTO.getEmail());
    }

    

    Patient newPatient = patientRepository.save(
      PatientMapper.toModel(patientRequestDTO));
    
    return PatientMapper.toDTO(newPatient);
  }
}
