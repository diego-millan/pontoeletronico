package com.millan.pontoeletronico.services.impl

import com.millan.pontoeletronico.domain.documents.Appointment
import com.millan.pontoeletronico.repository.AppointmentRepository
import com.millan.pontoeletronico.services.AppointmentService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class AppointmentServiceImpl(private val repository : AppointmentRepository) : AppointmentService {

    override fun findByEmployeeId(employeeId: String, pageRequest: PageRequest): Page<Appointment> =
        repository.findByEmployeeId(employeeId, pageRequest)

    override fun findById(id: String): Optional<Appointment> = repository.findById(id)

    override fun save(appointment: Appointment): Appointment = repository.save(appointment)

    override fun delete(id: String) = repository.deleteById(id)

}