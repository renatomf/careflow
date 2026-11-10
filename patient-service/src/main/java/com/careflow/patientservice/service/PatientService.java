package com.careflow.patientservice.service;

import java.time.LocalDate;
import java.util.List;

import java.util.UUID;
import org.springframework.stereotype.Service;

import com.careflow.patientservice.dto.PatientRequestDTO;
import com.careflow.patientservice.dto.PatientResponseDTO;
import com.careflow.patientservice.exception.EmailAlreadyExistsException;
import com.careflow.patientservice.exception.PatientNotFoundException;
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

  public PatientResponseDTO updatePatient(UUID id, 
    PatientRequestDTO patientRequestDTO) {
    
    Patient patient = patientRepository.findById(id).orElseThrow(
      () -> new PatientNotFoundException("Patient not found with id: " + id));
    
    if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
      throw new EmailAlreadyExistsException(
        "A patient with this email already exists: "
         + patientRequestDTO.getEmail());
    }

    patient.setName(patientRequestDTO.getName());
    patient.setAddress(patientRequestDTO.getAddress());
    patient.setEmail(patientRequestDTO.getEmail());
    patient.setPhone(patientRequestDTO.getPhone());
    patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

    Patient updatedPatient = patientRepository.save(patient);
    return PatientMapper.toDTO(updatedPatient);
  }
}
