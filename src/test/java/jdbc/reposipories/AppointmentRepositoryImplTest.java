package jdbc.reposipories;

import com.solvd.hospital.entities.Appointment;
import com.solvd.hospital.repositories.AppointmentRepository;
import com.solvd.hospital.repositories.impl.AppointmentRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDateTime;
import java.util.List;

import static jdbc.reposipories.DoctorRepositoryImplTest.createDoctorObj;
import static jdbc.reposipories.PatientRepositoryImplTest.createPatientObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AppointmentRepositoryImplTest {

    private static final AppointmentRepository appointmentRepository = new AppointmentRepositoryImpl();

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
