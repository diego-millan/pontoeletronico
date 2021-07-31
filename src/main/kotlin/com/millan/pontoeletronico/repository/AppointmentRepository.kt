package com.millan.pontoeletronico.repository

import com.millan.pontoeletronico.domain.documents.Appointment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface AppointmentRepository : MongoRepository<Appointment, String> {
    fun findByEmployeeId(employeeId: String, pageable: Pageable) : Page<Appointment>
}