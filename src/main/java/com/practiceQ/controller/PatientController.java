package com.practiceQ.controller;


import com.practiceQ.AppContants.Constants;
import com.practiceQ.entity.Patient;
import com.practiceQ.payload.*;

import com.practiceQ.service.Impl.EmailService;
import com.practiceQ.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody Patient patient, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult
                    .getFieldError()
                    .getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(patientService.existsByEmail(patient.getEmail())){
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }
        Patient signup = patientService.signup(patient);

        emailService.sendEmail(patient.getEmail(),"PracticeQ Registration","You are successfully registered as patient in PracticeQ.");
        return new ResponseEntity<>(signup,HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signinDto){
        String token = patientService.verifyLogin(signinDto);

        if(token!=null){
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credentials ", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/profile")
    public ResponseEntity<Patient> getCurrentUser(@AuthenticationPrincipal Patient patient){
        return new ResponseEntity<>(patient,HttpStatus.OK);
    }

    @GetMapping("/{patientId}/{search}")
    public ResponseEntity<PatientAppointmentsDto> allAppointments(@PathVariable String patientId, @PathVariable String search){
        PatientAppointmentsDto byId = patientService.findById(patientId,search);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<ListOfDoctorsForPatient>> getAllDoctors(
            @RequestParam(name = "pageNo",defaultValue = Constants.DEFAULT_PAGE_Number) int pageNo,
            @RequestParam(name = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE) int pageSize
    ){
        List<ListOfDoctorsForPatient> allDoctors = patientService.getAllDoctors(pageNo, pageSize);
        return new ResponseEntity<>(allDoctors, HttpStatus.OK);
    }


    @DeleteMapping
    public ResponseEntity<String> deleteAppointment(@RequestBody DeleteAppointmentsForPatient deleteAppointmentsForPatient){
        String s = patientService.deleteAppointment(deleteAppointmentsForPatient);
        return new ResponseEntity<>(s,HttpStatus.OK);
    }

    @GetMapping("/doctors/{search}")
    public ResponseEntity<List<ListOfDoctorsForPatient>> searchDoctor(@PathVariable String search,
           @RequestParam(name = "pageNo",defaultValue = Constants.DEFAULT_PAGE_Number,required = false) int pageNo,
            @RequestParam(name="pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE,required = false) int pageSize){
        List<ListOfDoctorsForPatient> listOfDoctorsForPatients = patientService.searchDoctor(search,pageNo,pageSize);
        return new ResponseEntity<>(listOfDoctorsForPatients,HttpStatus.OK);
    }

    @GetMapping("/invoices/{patientId}")
    public ResponseEntity<PatientWithInvoice> getAllInvoices(@PathVariable String patientId){
        PatientWithInvoice allInvoice = patientService.getAllInvoice(patientId);
        return new ResponseEntity<>(allInvoice, HttpStatus.OK);
    }


}
