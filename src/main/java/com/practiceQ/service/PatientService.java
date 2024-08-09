package com.practiceQ.service;

import com.practiceQ.entity.Patient;
import com.practiceQ.payload.*;

import java.util.List;

public interface PatientService {

    Patient signup(Patient patient);

    PatientAppointmentsDto findById(String patientId, String search);

    List<ListOfDoctorsForPatient> getAllDoctors(int pageNo, int pageSize);

    String deleteAppointment(DeleteAppointmentsForPatient deleteAppointmentsForPatient);

    List<ListOfDoctorsForPatient> searchDoctor(String search, int pageNo, int pageSize);

    boolean existsByEmail(String email);

    PatientWithInvoice getAllInvoice(String patientId);

    String verifyLogin(SignInDto signinDto);



}
