package com.millan.pontoeletronico.controller

import com.millan.pontoeletronico.domain.documents.Appointment
import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.domain.enums.TypeEnum
import com.millan.pontoeletronico.dto.AppointmentDTO
import com.millan.pontoeletronico.response.Response
import com.millan.pontoeletronico.services.AppointmentService
import com.millan.pontoeletronico.services.EmployeeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/appointment")
class AppointmentController(val appointmentService: AppointmentService,
                            val employeeService: EmployeeService) {

    @Value("\${pageable.pages.size}")
    val pageSize: Int = 15

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @PostMapping
    fun save(@Valid @RequestBody appointmentDTO: AppointmentDTO, result: BindingResult)
            : ResponseEntity<Response<AppointmentDTO>> {

        val response: Response<AppointmentDTO> = Response<AppointmentDTO>()
        var responseEntity: ResponseEntity<Response<AppointmentDTO>>

        validateEmployee(appointmentDTO, result)

        if (result.hasErrors()) {
            for (error in result.allErrors) response.errors.add(error.defaultMessage!!)
            responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response)

        } else {
            var appointment: Appointment = convertAppointmentFromDTO(appointmentDTO, result)

            appointmentService.save(appointment)

            response.data = convertAppointmentDTO(appointment)
            responseEntity = ResponseEntity.ok(response)
        }

        return responseEntity

    }

    private fun convertAppointmentDTO(appointment: Appointment): AppointmentDTO = AppointmentDTO(
            dateFormat.format(appointment.date), appointment.type.toString(),
            appointment.description, appointment.location, appointment.employeeId, appointment.id)

    private fun convertAppointmentFromDTO(appointmentDTO: AppointmentDTO, result: BindingResult): Appointment {

        if (appointmentDTO.id != null) {
            val appointment: Optional<Appointment> = appointmentService.findById(appointmentDTO.id)

            if (appointment.isEmpty) {
                result.addError(ObjectError("appointment", "Appointment not found"))
            }
        }

        return Appointment(dateFormat.parse(appointmentDTO.date),
                TypeEnum.valueOf(appointmentDTO.type!!), appointmentDTO.employeeId!!,
                appointmentDTO.description, appointmentDTO.location, appointmentDTO.id)
    }

    private fun validateEmployee(appointmentDTO: AppointmentDTO, result: BindingResult) {

        if (appointmentDTO.employeeId == null) {
            result.addError(ObjectError("employee", "Employee ID is invalid"))

        } else {
            val employee: Optional<Employee> = employeeService.findById(appointmentDTO.employeeId)

            if (employee.isEmpty) {
                result.addError(ObjectError("employee", "Employee ID is invalid"))
            }
        }
    }
}