package com.millan.pontoeletronico.service

import com.millan.pontoeletronico.domain.documents.Appointment
import com.millan.pontoeletronico.domain.enums.TypeEnum
import com.millan.pontoeletronico.repository.AppointmentRepository
import com.millan.pontoeletronico.services.AppointmentService
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit4.SpringRunner
import java.util.*

@RunWith(SpringRunner::class)
@SpringBootTest
class AppointmentServiceTest {

    @MockBean
    private val appointmentRepository: AppointmentRepository? = null

    @Autowired
    val appointmentService: AppointmentService? = null

    private val id: String = "1"
    val pageable = PageRequest.of(0, 10, Sort.unsorted())
    @BeforeEach
    fun setUp() {

        BDDMockito.given<Page<Appointment>>(
            appointmentRepository?.findByEmployeeId(
                id,
                pageable
            )
        )
            .willReturn(PageImpl(ArrayList<Appointment>()))
        BDDMockito.given(appointmentRepository?.findById(id)).willReturn(Optional.of(createAppointment()))
        BDDMockito.given(appointmentRepository?.save(Mockito.any(Appointment::class.java)))
            .willReturn(createAppointment())
    }

    @Test
    fun testFindAppointmentByEmployeeId() {
        val appointment: Page<Appointment>? =
            appointmentService?.findByEmployeeId(id, pageable)
        Assert.assertNotNull(appointment)
    }

    @Test
    fun testFindAppointmentById() {
        val appointment: Optional<Appointment>? = appointmentService?.findById(id)
        Assert.assertNotNull(appointment)
    }

    @Test
    fun testSaveAppointment() {
        val appointment: Appointment? = appointmentService?.save(createAppointment())
        Assert.assertNotNull(appointment)
    }

    private fun createAppointment(): Appointment = Appointment(Date(), TypeEnum.WORK_START, id)
}