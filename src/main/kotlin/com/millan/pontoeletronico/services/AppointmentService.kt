package com.millan.pontoeletronico.services

import com.millan.pontoeletronico.domain.documents.Appointment
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.*

interface AppointmentService {
    fun findByEmployeeId(employeeId:String , pageRequest:PageRequest) : Page<Appointment>

    fun findById(id: String): Optional<Appointment>

    fun save(appointment: Appointment) : Appointment

    fun delete(id: String)
}