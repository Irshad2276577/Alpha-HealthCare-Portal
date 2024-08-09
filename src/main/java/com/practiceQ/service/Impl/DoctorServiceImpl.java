package com.practiceQ.service.Impl;

import com.practiceQ.entity.Appointments;
import com.practiceQ.entity.Doctor;
import com.practiceQ.entity.Patient;

import com.practiceQ.exception.DepartmentNotFoundException;
import com.practiceQ.exception.DoctorNotFoundException;
import com.practiceQ.exception.PatientNotFoundException;
import com.practiceQ.payload.*;
import com.practiceQ.repository.AppointmentsRepository;
import com.practiceQ.repository.DepartmentRepository;
import com.practiceQ.repository.DoctorRepository;

import com.practiceQ.repository.PatientRepository;
import com.practiceQ.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentsRepository appointmentsRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private JWTService  jwtService;

    @Override
    public Doctor signup(DoctorDto doctorDto) {

        Doctor doctor = new Doctor();
        doctor.setDoctorId(UUID.randomUUID().toString());
        doctor.setFirstName(doctorDto.getFirstName());
        doctor.setLastName(doctorDto.getLastName());
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setPassword(BCrypt.hashpw(doctorDto.getPassword(),BCrypt.gensalt(10)));
        doctor.setRole(doctorDto.getRole());

        doctor.setDepartment(departmentRepository.findById(doctorDto.getDepartmentId()).orElseThrow(()
                -> new DepartmentNotFoundException("Department is not found for this department id  " + doctorDto.getDepartmentId())));

        return doctorRepository.save(doctor);

    }

    @Override
    public String verifyLogin(SignInDto signInDto) {

        Doctor doctor = doctorRepository.findByFirstName(signInDto.getUsername()).orElseThrow(() ->
                new DoctorNotFoundException("Doctor not found "));
        if(BCrypt.checkpw(signInDto.getPassword(),doctor.getPassword())){
            return jwtService.generateTokenForDoctor(doctor);
        }
        return null;
    }


    @Override
    public DoctorAppointmentsDto findById(String doctorId, int pageNo, int pageSize) {
        DoctorAppointmentsDto doctorAppointmentsDto = new DoctorAppointmentsDto();
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(()-> new DoctorNotFoundException("Doctor is not found for id "+doctorId));

        Pageable pageable= PageRequest.of(pageNo, pageSize);
        Page<Appointments> all = appointmentsRepository.findAllByDoctor(doctor, pageable);
        List<Appointments> allAppointments = all.getContent();
        List<AppointmentForDoctor> collect = allAppointments.stream().map(appointments -> {
            AppointmentForDoctor appointmentForDoctor = new AppointmentForDoctor();
            appointmentForDoctor.setAppId(appointments.getAppId());
            appointmentForDoctor.setDate(appointments.getDate());
            appointmentForDoctor.setTime(appointments.getTime());
            appointmentForDoctor.setReason(appointments.getReason());
            appointmentForDoctor.setEmergency(appointments.getEmergency());

            PatientDto patientDto = new PatientDto();
            Patient patient = patientRepository.findById(appointments.getPatient().getPatientId()).orElseThrow(()->
                    new PatientNotFoundException("Patient is not found for id "+appointments.getPatient().getPatientId())
            );
            patientDto.setPatientId(patient.getPatientId());
            patientDto.setFirstName(patient.getFirstName());
            patientDto.setLastName(patient.getLastName());
            patientDto.setMobile(patient.getMobile());
            patientDto.setEmail(patient.getEmail());

            appointmentForDoctor.setPatientDto(patientDto);
            return appointmentForDoctor;

        }).collect(Collectors.toList());

        doctorAppointmentsDto.setFirstName(doctor.getFirstName());
        doctorAppointmentsDto.setLastName(doctor.getLastName());
        doctorAppointmentsDto.setSpecialization(doctor.getSpecialization());
        doctorAppointmentsDto.setAppointmentForDoctor(collect);


        return doctorAppointmentsDto;
    }

    @Override
    public DoctorAppointmentsDto findById(String doctorId, boolean search,int pageNo,int pageSize) {
        DoctorAppointmentsDto doctorAppointmentsDto = new DoctorAppointmentsDto();
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(()-> new DoctorNotFoundException("Doctor is not found for id "+doctorId));

        Pageable pageable= PageRequest.of(pageNo, pageSize);

        Page<Appointments> allByDoctorWithSearch = appointmentsRepository.findAllByDoctorWithSearch(doctor, search, pageable);

        List<Appointments> allAppointments = allByDoctorWithSearch.getContent();

        List<AppointmentForDoctor> collect = allAppointments.stream().map(appointments -> {
            AppointmentForDoctor appointmentForDoctor = new AppointmentForDoctor();
            appointmentForDoctor.setAppId(appointments.getAppId());
            appointmentForDoctor.setDate(appointments.getDate());
            appointmentForDoctor.setTime(appointments.getTime());
            appointmentForDoctor.setReason(appointments.getReason());
            appointmentForDoctor.setEmergency(appointments.getEmergency());

            PatientDto patientDto = new PatientDto();
            Patient patient = patientRepository.findById(appointments.getPatient().getPatientId()).orElseThrow(()->
                    new PatientNotFoundException("Patient is not found for id "+appointments.getPatient().getPatientId())
            );
            patientDto.setPatientId(patient.getPatientId());
            patientDto.setFirstName(patient.getFirstName());
            patientDto.setLastName(patient.getLastName());
            patientDto.setMobile(patient.getMobile());
            patientDto.setEmail(patient.getEmail());

            appointmentForDoctor.setPatientDto(patientDto);
            return appointmentForDoctor;

        }).collect(Collectors.toList());

        doctorAppointmentsDto.setFirstName(doctor.getFirstName());
        doctorAppointmentsDto.setLastName(doctor.getLastName());
        doctorAppointmentsDto.setSpecialization(doctor.getSpecialization());
        doctorAppointmentsDto.setAppointmentForDoctor(collect);


        return doctorAppointmentsDto;


    }

    @Override
    public boolean existsByEmail(String email) {

        return doctorRepository.existsByEmail(email);
    }


}
