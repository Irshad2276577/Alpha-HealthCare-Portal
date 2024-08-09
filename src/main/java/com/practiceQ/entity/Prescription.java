package com.practiceQ.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    @Column(name = "prescription_id", nullable = false)
    private String prescriptionId;

    @Column(name = "prescription_name", nullable = false)
    private String prescriptionName;

    @Column(name = "prescription_validity", nullable = false)
    private String prescriptionValidity;

    @Column(name = "prescription_cost")
    private Float prescriptionCost;

    @ManyToOne
    @JoinColumn(name = "patient_patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_doctor_id")
    private Doctor doctor;

}