package com.practiceQ.controller;

import com.practiceQ.AppContants.Constants;
import com.practiceQ.entity.AcceptAppointment;
import com.practiceQ.entity.Doctor;
import com.practiceQ.payload.AcceptAppointmentDto;
import com.practiceQ.payload.DoctorAppointmentsDto;
import com.practiceQ.payload.DoctorDto;
import com.practiceQ.payload.SignInDto;
import com.practiceQ.service.AcceptAppointmentService;
import com.practiceQ.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AcceptAppointmentService acceptAppointmentService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInDto signInDto){
        String token = doctorService.verifyLogin(signInDto);
        if(token != null){
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid Credential", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/profile")
    public Doctor currentDoctor(@AuthenticationPrincipal Doctor doctor){
        return doctor;
    }



    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorAppointmentsDto> getAllAppointmentsForDoctor(@PathVariable String doctorId,
       @RequestParam(name = "pageNo",defaultValue = Constants.DEFAULT_PAGE_Number,required = false) int pageNo,
         @RequestParam(name = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE, required =false) int pageSize){
        DoctorAppointmentsDto byId = doctorService.findById(doctorId,pageNo,pageSize);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @GetMapping("/{doctorId}/{search}")
    public ResponseEntity<DoctorAppointmentsDto> searchDoctor(@PathVariable String doctorId, @PathVariable boolean search,
      @RequestParam(name = "pageNo",defaultValue = Constants.DEFAULT_PAGE_Number,required = false) int pageNo,
      @RequestParam(name = "pageSize",defaultValue = Constants.DEFAULT_PAGE_SIZE, required =false) int pageSize){
        DoctorAppointmentsDto byId = doctorService.findById(doctorId,search,pageNo,pageSize);
        return new ResponseEntity<>(byId, HttpStatus.OK);
    }

    @PostMapping("/acceptAppointments")
    public ResponseEntity<?> acceptAppointment(@Valid @RequestBody AcceptAppointmentDto acceptAppointmentDto,BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ResponseEntity<>(bindingResult
                    .getFieldError()
                    .getDefaultMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        AcceptAppointment acceptAppointment = acceptAppointmentService.acceptAppointment(acceptAppointmentDto);

        return new ResponseEntity<>(acceptAppointment, HttpStatus.CREATED);
    }




}
