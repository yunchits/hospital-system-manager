package jdbc.dao;

import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.dao.PrescriptionDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCPrescriptionDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static jdbc.dao.JDBCDoctorDAOImplTest.createDoctorObj;
import static jdbc.dao.JDBCMedicationDAOImplTest.createMedicationObj;
import static jdbc.dao.JDBCPatientDAOImplTest.createPatientObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCPrescriptionDAOImplTest {

    private static final PrescriptionDAO PRESCRIPTION_DAO = new JDBCPrescriptionDAOImpl();

    @Test
    void createPrescription() {
        Prescription prescription = createPrescriptionObj();

        Assertions.assertNotNull(prescription.getId());
    }

    @Test
    void findByPatientId() {
        Prescription prescription = createPrescriptionObj();
        long patientId = prescription.getPatient().getId();

        List<Prescription> prescriptions = PRESCRIPTION_DAO.findByPatientId(patientId);

        Assertions.assertTrue(!prescriptions.isEmpty());
    }

    @Test
    void deleteByPatientId() {
        Prescription prescription = createPrescriptionObj();
        long patientId = prescription.getPatient().getId();

        boolean isDeleted = PRESCRIPTION_DAO.deleteByPatientId(patientId);

        Assertions.assertTrue(isDeleted);
    }

    static Prescription createPrescriptionObj() {
        return PRESCRIPTION_DAO.create(new Prescription()
            .setPatient(createPatientObj())
            .setDoctor(createDoctorObj())
            .setMedication(createMedicationObj()));
    }
}
