package com.practiceQ.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionResponse {

    private String prescriptionName;
    private String prescriptionCourseDuration;

    private Float prescriptionCost;

    private PatientDto patient;
    private ListOfDoctorsForPatient doctor;
}