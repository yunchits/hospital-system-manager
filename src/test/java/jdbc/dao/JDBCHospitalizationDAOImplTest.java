package jdbc.dao;

import com.solvd.hospital.entities.Hospitalization;
import com.solvd.hospital.dao.HospitalizationDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCHospitalizationDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;

import static jdbc.dao.JDBCPatientDAOImplTest.createPatientObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCHospitalizationDAOImplTest {

    private static final HospitalizationDAO HOSPITALIZATION_DAO = new JDBCHospitalizationDAOImpl();

    @Test
    void createHospitalization() {
        Hospitalization hospitalization = createHospitalizationObject();

        Assertions.assertNotNull(hospitalization.getId());
    }

    @Test
    void findByPatientId() {
        Hospitalization hospitalization = createHospitalizationObject();
        List<Hospitalization> hospitalizations = HOSPITALIZATION_DAO.findByPatientId(hospitalization.getPatient().getId());

        Assertions.assertFalse(hospitalizations.isEmpty());
    }

    @Test
    void deleteHospitalization() {
        Hospitalization hospitalization = createHospitalizationObject();

        boolean result = HOSPITALIZATION_DAO.delete(hospitalization.getId());

        Assertions.assertTrue(result);

        List<Hospitalization> deletedHospitalizations = HOSPITALIZATION_DAO.findByPatientId(hospitalization.getPatient().getId());
        Assertions.assertTrue(deletedHospitalizations.isEmpty());
    }

    private Hospitalization createHospitalizationObject() {
        return HOSPITALIZATION_DAO.create(new Hospitalization()
            .setPatient(createPatientObj())
            .setAdmissionDate(LocalDate.now())
            .setDischargeDate(LocalDate.now().plusDays(7)));
    }
}
