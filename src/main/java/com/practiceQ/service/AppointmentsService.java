package com.practiceQ.service;

import com.practiceQ.entity.Appointments;
import com.practiceQ.payload.AppointmentsDto;

public interface AppointmentsService {

    Appointments createAppointments(AppointmentsDto appointmentsDto);
    Appointments updateAppointments(AppointmentsDto appointmentsDto);
}
