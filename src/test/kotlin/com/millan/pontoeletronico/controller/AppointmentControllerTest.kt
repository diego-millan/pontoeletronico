package com.millan.pontoeletronico.controller

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.millan.pontoeletronico.domain.documents.Appointment
import com.millan.pontoeletronico.domain.documents.Employee
import com.millan.pontoeletronico.domain.enums.ProfileEnum
import com.millan.pontoeletronico.domain.enums.TypeEnum
import com.millan.pontoeletronico.dto.AppointmentDTO
import com.millan.pontoeletronico.services.AppointmentService
import com.millan.pontoeletronico.services.EmployeeService
import com.millan.pontoeletronico.utils.PasswordUtils
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.text.SimpleDateFormat
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val appointmentService: AppointmentService? = null

    @MockBean
    private val employeeService: EmployeeService? = null

    private val baseURL: String = "/api/appointment/"
    private val employeeId: String = "1"
    private val appointmentId: String = "1"
    private val type: String = TypeEnum.WORK_START.name
    private val date: Date = Date()
    private val dateFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    @Test
    @WithMockUser
    @Throws(Exception::class)
    fun `when test save new appointment then return success`() {

        val appointment: Appointment = createAppointment()

        BDDMockito.given<Optional<Employee>>(employeeService?.findById(employeeId))
            .willReturn(Optional.of(createEmployee()))

        BDDMockito.given(appointmentService?.save(createAppointment())).willReturn(appointment)

        mockMvc!!.perform(
            MockMvcRequestBuilders.post(baseURL)
                .with(csrf())
                .param("action", "signup")
                .content(getJSONPostRequest())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated)
                .andExpect(jsonPath("$.data.type").value(type))
                .andExpect(jsonPath("$.data.date").value(dateFormat.format(date)))
                .andExpect(jsonPath("$.data.employeeId").value(employeeId))
                .andExpect(jsonPath("$.errors").isEmpty)
    }

    @Test
    @WithMockUser
    @Throws(Exception::class)
    fun `when test save appointment with invalid user then throw precondition failed`() {

        BDDMockito.given<Optional<Employee>>(employeeService?.findById(employeeId))
            .willReturn(Optional.empty())
        mockMvc!!.perform(
            MockMvcRequestBuilders.post(baseURL)
                .with(csrf())
                .param("action", "signup")
                .content(getJSONPostRequest())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isPreconditionFailed)
                .andExpect(jsonPath("$.errors").value("Employee ID is invalid"))
                .andExpect(jsonPath("$.data").isEmpty)
    }

    @Test
    @WithMockUser
    @Throws(Exception::class)
    fun `when test when delete appointment by id then return status 202`() {
        BDDMockito.given<Optional<Appointment>>(appointmentService?.findById(appointmentId))
            .willReturn(Optional.of(createAppointment()))

        mockMvc!!.perform(
            MockMvcRequestBuilders
                .delete(baseURL + appointmentId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted)
    }

    @Throws(JsonProcessingException::class)
    private fun getJSONPostRequest(): String {

        val appointmentDTO = AppointmentDTO(
            dateFormat.format(date), type, "description", "1.234,4.123", employeeId
        )

        val mapper = ObjectMapper()

        return mapper.writeValueAsString(appointmentDTO)
    }

    private fun createEmployee(): Employee = Employee(
        "Alexandra", "email@mail.com", PasswordUtils().generateBcrypt("password"), "07364412332",
        ProfileEnum.ROLE_USER, "1", 8.0, 8.0f, 1.0f, "1"
    )

    private fun createAppointment(): Appointment =
        Appointment(Date(), TypeEnum.WORK_START, appointmentId)

}