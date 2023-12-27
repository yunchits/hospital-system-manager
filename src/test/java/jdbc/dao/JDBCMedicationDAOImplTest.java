package jdbc.dao;

import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.dao.MedicationDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCMedicationDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCMedicationDAOImplTest {

    private static final MedicationDAO MEDICATION_DAO = new JDBCMedicationDAOImpl();

    @Test
    void createMedication() {
        Medication medication = createMedicationObj();

        Assertions.assertNotNull(medication.getId());
    }

    @Test
    void findAllMedications() {
        List<Medication> medications = MEDICATION_DAO.findAll();

        Assertions.assertFalse(medications.isEmpty());
    }

    @Test
    void findMedicationById() {
        Medication medication = createMedicationObj();
        Optional<Medication> foundMedication = MEDICATION_DAO.findById(medication.getId());

        Assertions.assertTrue(foundMedication.isPresent());
        Assertions.assertEquals(medication.getId(), foundMedication.get().getId());
    }

    @Test
    void updateMedication() {
        Medication medication = createMedicationObj();
        medication.setName("Updated Medication");
        medication.setDescription("Updated Description");

        Medication updatedMedication = MEDICATION_DAO.update(medication);

        Assertions.assertEquals("Updated Medication", updatedMedication.getName());
        Assertions.assertEquals("Updated Description", updatedMedication.getDescription());
    }

    @Test
    void deleteMedication() {
        Medication medication = createMedicationObj();
        boolean isDeleted = MEDICATION_DAO.delete(medication.getId());

        Assertions.assertTrue(isDeleted);
    }

    static Medication createMedicationObj() {
        String uniqueMedicationName = "Medication Test " + System.currentTimeMillis();
        return MEDICATION_DAO.create(new Medication()
            .setName(uniqueMedicationName)
            .setDescription("Test Description"));
    }
}
