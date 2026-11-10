package com.careflow.patientservice.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careflow.patientservice.dto.PatientRequestDTO;
import com.careflow.patientservice.dto.PatientResponseDTO;
import com.careflow.patientservice.service.PatientService;
import com.careflow.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/patients")
public class PatientController {
  private final PatientService patientService;

  public PatientController(PatientService patientService) {
    this.patientService = patientService;
  }

  @GetMapping
  public ResponseEntity<List<PatientResponseDTO>> getPatients() {
    List<PatientResponseDTO> patients = patientService.getPatients();
    return ResponseEntity.ok().body(patients);
  }

  @PostMapping
  public ResponseEntity<PatientResponseDTO> createPatient(
    @Validated({Default.class, CreatePatientValidationGroup.class})
    @RequestBody PatientRequestDTO patientRequestDTO) {
    
      PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDTO);

    return ResponseEntity.ok().body(patientResponseDTO);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
    @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
    PatientResponseDTO patientResponseDTO = patientService.updatePatient(id, patientRequestDTO);
    
    return ResponseEntity.ok().body(patientResponseDTO);
  }
}
