package com.practiceQ.payload;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionDto {
    private String prescriptionId;

    @NotEmpty(message = "Prescription Name must not be empty")
    @Size(min = 5,message = "Prescription Name must be at least 5 characters")
    private String prescriptionName;

    @NotEmpty(message = "Prescription Validity  must not be empty")
    private String prescriptionValidity;

    private Float prescriptionCost;

    @NotEmpty(message = "Patient Id must not be empty")
    private String patientId;

    @NotEmpty(message = "Doctor Id must not be empty")
    private String doctorId;
}
