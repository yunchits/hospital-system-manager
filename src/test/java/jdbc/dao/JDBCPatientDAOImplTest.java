package jdbc.dao;

import com.solvd.hospital.entities.patient.Gender;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.dao.PatientDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCPatientDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCPatientDAOImplTest {

    private static final PatientDAO PATIENT_DAO = new JDBCPatientDAOImpl();

    @Test
    void createPatient() {
        Patient createdPatient = createPatientObj();

        Assertions.assertNotNull(createdPatient.getId());
    }

    @Test
    void findAllPatients() {
        List<Patient> patients = PATIENT_DAO.findAll();

        Assertions.assertFalse(patients.isEmpty());
    }

    @Test
    void findPatientById() {
        Patient createdPatient = createPatientObj();

        Optional<Patient> foundPatient = PATIENT_DAO.findById(createdPatient.getId());

        Assertions.assertTrue(foundPatient.isPresent());
        Assertions.assertEquals(createdPatient.getId(), foundPatient.get().getId());
    }

    @Test
    void deletePatient() {
        Patient createdPatient = createPatientObj();

        boolean isDeleted = PATIENT_DAO.delete(createdPatient.getId());

        Assertions.assertTrue(isDeleted);
    }

    static Patient createPatientObj() {
        return PATIENT_DAO.create(
            new Patient()
                .setFirstName("Bob")
                .setLastName("Johnson")
                .setBirthDate(LocalDate.of(1970, 8, 20))
                .setGender(Gender.MALE));
    }
}
