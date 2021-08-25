package com.millan.pontoeletronico.controller

import com.millan.pontoeletronico.domain.documents.Appointment
import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.domain.enums.TypeEnum
import com.millan.pontoeletronico.dto.AppointmentDTO
import com.millan.pontoeletronico.response.Response
import com.millan.pontoeletronico.services.AppointmentService
import com.millan.pontoeletronico.services.EmployeeService
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.ObjectError
import org.springframework.web.bind.annotation.*
import java.text.SimpleDateFormat
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/appointment")
class AppointmentController(
    val appointmentService: AppointmentService,
    val employeeService: EmployeeService
) {

    @Value("\${pageable.pages.size}")
    val pageSize: Int = 15

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @PostMapping
    fun save(@Valid @RequestBody appointmentDTO: AppointmentDTO, result: BindingResult)
            : ResponseEntity<Response<AppointmentDTO>> {

        val response: Response<AppointmentDTO> = Response()
        val responseEntity: ResponseEntity<Response<AppointmentDTO>>

        validateEmployee(appointmentDTO, result)

        if (result.hasErrors()) {
            for (error in result.allErrors) response.errors.add(error.defaultMessage!!)
            responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response)

        } else {
            val appointment: Appointment = convertAppointmentFromDTO(appointmentDTO, result, appointmentDTO.id)

            appointmentService.save(appointment)

            response.data = convertAppointmentDTO(appointment)
            responseEntity = ResponseEntity.status(HttpStatus.CREATED).body(response)
        }

        return responseEntity
    }

    @GetMapping(value = ["/{id}"])
    fun findById(@PathVariable("id") id: String): ResponseEntity<Response<AppointmentDTO>> {
        val responseEntity: ResponseEntity<Response<AppointmentDTO>>
        val response: Response<AppointmentDTO> = Response()

        val appointment: Optional<Appointment> = appointmentService.findById(id)

        if (appointment.isEmpty) {
            response.errors.add("Appointment not found with id $id")
            responseEntity = ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        } else {
            response.data = convertAppointmentDTO(appointment.get())
            responseEntity = ResponseEntity.ok(response)
        }

        return responseEntity
    }

    @GetMapping(value = ["employee/{employeeId}"])
    fun findByEmployeeId(
        @PathVariable("employeeId") employeeId: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "ord", defaultValue = "id") ord: String,
        @RequestParam(value = "dir", defaultValue = "DESC") dir: String
    )
            : ResponseEntity<Response<Page<AppointmentDTO>>> {

        val responseEntity: ResponseEntity<Response<Page<AppointmentDTO>>>
        val response: Response<Page<AppointmentDTO>> = Response<Page<AppointmentDTO>>()

        val pageRequest: PageRequest =
            PageRequest.of(page, pageSize, Sort.Direction.valueOf(dir), ord)

        val appointments: Page<Appointment> =
            appointmentService.findByEmployeeId(employeeId, pageRequest)

        val appointmentsDTO: Page<AppointmentDTO> =
            appointments.map { appointment -> convertAppointmentDTO(appointment) }

        response.data = appointmentsDTO

        responseEntity = ResponseEntity.ok(response)
        return responseEntity
    }

    @PutMapping(value = ["/{id}"])
    fun update(@PathVariable("id") id: String,
                          @Valid @RequestBody appointmentDTO: AppointmentDTO,
                          result: BindingResult) : ResponseEntity<Response<AppointmentDTO>> {

        val responseEntity: ResponseEntity<Response<AppointmentDTO>>
        val response: Response<AppointmentDTO> = Response<AppointmentDTO>()

        validateEmployee(appointmentDTO, result)

        val appointment : Appointment = convertAppointmentFromDTO(appointmentDTO, result, id)

        if (result.hasErrors()) {
            for (error in result.allErrors) response.errors.add(error.defaultMessage!!)
            responseEntity = ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response)
        } else {
            appointmentService.save(appointment)

            response.data = convertAppointmentDTO(appointment)
            responseEntity = ResponseEntity.status(HttpStatus.ACCEPTED).body(response)
        }

        return responseEntity
    }

    @DeleteMapping(value = ["/{id}"])
    fun delete(@PathVariable("id") id : String) : ResponseEntity<Response<String>> {
        val response: Response<String> = Response()
        val appointment: Optional<Appointment> = appointmentService.findById(id)
        if (appointment.isEmpty) {
            response.errors.add("Error trying to delete apppointment, id $id not found")
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response)
        }
        appointmentService.delete(id)
        return ResponseEntity.accepted().build()
    }


    private fun convertAppointmentDTO(appointment: Appointment): AppointmentDTO = AppointmentDTO(
        dateFormat.format(appointment.date), appointment.type.toString(),
        appointment.description, appointment.location, appointment.employeeId, appointment.id
    )

    private fun convertAppointmentFromDTO(
        appointmentDTO: AppointmentDTO,
        result: BindingResult,
        appointmentId: String?
    ): Appointment {

        if (appointmentId != null) {

            val appointment: Optional<Appointment> = appointmentService.findById(appointmentId)

            if (appointment.isEmpty) {
                result.addError(ObjectError("appointment", "Appointment not found"))
            }
        }

        return Appointment(
            dateFormat.parse(appointmentDTO.date),
            TypeEnum.valueOf(appointmentDTO.type!!), appointmentDTO.employeeId!!,
            appointmentDTO.description, appointmentDTO.location, appointmentDTO.id
        )
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