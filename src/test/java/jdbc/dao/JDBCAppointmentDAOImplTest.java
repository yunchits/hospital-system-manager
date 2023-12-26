package jdbc.dao;

import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.dao.AppointmentDAO;
import com.solvd.hospital.dao.impl.JDBCAppointmentDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.List;

import static jdbc.dao.JDBCDoctorDAOImplTest.createDoctorObj;
import static jdbc.dao.JDBCPatientDAOImplTest.createPatientObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCAppointmentDAOImplTest {

    private static final AppointmentDAO appointmentRepository = new JDBCAppointmentDAOImpl();

    @Test
    void createAppointment() {
        Appointment appointment = createAppointmentObj();

        Assertions.assertNotNull(appointment.getId());
    }

    @Test
    void findByPatientId() {
        Appointment appointment = createAppointmentObj();
        List<Appointment> appointments = appointmentRepository.findByPatientId(appointment.getPatient().getId());

        Assertions.assertFalse(appointments.isEmpty());
    }

    @Test
    void deleteByPatient() {
        Appointment appointment = createAppointmentObj();
        boolean isDeleted = appointmentRepository.deleteByPatient(appointment.getPatient().getId());

        Assertions.assertTrue(isDeleted);
    }

    static Appointment createAppointmentObj() {
        return appointmentRepository.create(new Appointment()
            .setPatient(createPatientObj())
            .setDoctor(createDoctorObj())
            .setAppointmentDateTime(LocalDateTime.now()));
    }
}
