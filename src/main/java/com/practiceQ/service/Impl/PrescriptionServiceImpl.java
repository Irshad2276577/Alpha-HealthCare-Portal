package com.practiceQ.service.Impl;

import com.practiceQ.entity.Doctor;
import com.practiceQ.entity.Patient;
import com.practiceQ.entity.Prescription;
import com.practiceQ.exception.DoctorNotFoundException;
import com.practiceQ.exception.PatientNotFoundException;
import com.practiceQ.payload.*;
import com.practiceQ.repository.DoctorRepository;

import com.practiceQ.repository.PatientRepository;
import com.practiceQ.repository.PrescriptionRepository;
import com.practiceQ.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;
    @Override
    public PrescriptionResponse createPrescription(PrescriptionDto prescriptionDto) {
        Prescription prescription = new Prescription();
        prescription.setPrescriptionId(UUID.randomUUID().toString());
        prescription.setPrescriptionName(prescriptionDto.getPrescriptionName());
        prescription.setPrescriptionValidity(prescriptionDto.getPrescriptionValidity());
        prescription.setPrescriptionCost(prescriptionDto.getPrescriptionCost());
        prescription.setPatient(patientRepository.findById(prescriptionDto.getPatientId())
                .orElseThrow(()-> new PatientNotFoundException("Patient not found for id "+prescriptionDto.getPatientId())));

        prescription.setDoctor(doctorRepository.findById(prescriptionDto.getDoctorId()).orElseThrow(()->
                new DoctorNotFoundException("Doctor not found")));

        Prescription saved = prescriptionRepository.save(prescription);

        PrescriptionResponse prescriptionResponse=new PrescriptionResponse();
        prescriptionResponse.setPrescriptionName(saved.getPrescriptionName());
        prescriptionResponse.setPrescriptionCost(saved.getPrescriptionCost());
        prescriptionResponse.setPrescriptionCourseDuration(saved.getPrescriptionValidity());

        ListOfDoctorsForPatient doctor=new ListOfDoctorsForPatient();
        Doctor doctorInfo = doctorRepository.findById(saved.getDoctor().getDoctorId()).orElseThrow(() ->
                new DoctorNotFoundException("Doctor not found"));
        doctor.setFirstName(doctorInfo.getFirstName());
        doctor.setLastName(doctorInfo.getLastName());
        doctor.setEmail(doctorInfo.getEmail());
        doctor.setSpecialization(doctorInfo.getSpecialization());

        prescriptionResponse.setDoctor(doctor);

        PatientDto patientDto = new PatientDto();
        Patient patientInfo = patientRepository.findById(saved.getPatient().getPatientId()).orElseThrow(() ->
                new PatientNotFoundException("Patient not found"));
        patientDto.setPatientId(patientInfo.getPatientId());
        patientDto.setFirstName(patientInfo.getFirstName());
        patientDto.setLastName(patientInfo.getLastName());
        patientDto.setMobile(patientInfo.getMobile());
        patientDto.setEmail(patientInfo.getEmail());
        prescriptionResponse.setPatient(patientDto);




        return prescriptionResponse;
    }
}
