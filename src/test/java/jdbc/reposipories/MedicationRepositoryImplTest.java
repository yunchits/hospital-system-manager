package jdbc.reposipories;

import com.solvd.hospital.entities.Medication;
import com.solvd.hospital.repositories.MedicationRepository;
import com.solvd.hospital.repositories.impl.MedicationRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MedicationRepositoryImplTest {

    private static final MedicationRepository medicationRepository = new MedicationRepositoryImpl();

    @Test
    void createMedication() {
        Medication medication = createMedicationObj();

        Assertions.assertNotNull(medication.getId());
    }

    @Test
    void findAllMedications() {
        List<Medication> medications = medicationRepository.findAll();

        Assertions.assertFalse(medications.isEmpty());
    }

    @Test
    void findMedicationById() {
        Medication medication = createMedicationObj();
        Optional<Medication> foundMedication = medicationRepository.findById(medication.getId());

        Assertions.assertTrue(foundMedication.isPresent());
        Assertions.assertEquals(medication.getId(), foundMedication.get().getId());
    }

    @Test
    void updateMedication() {
        Medication medication = createMedicationObj();
        medication.setMedicationName("Updated Medication");
        medication.setMedicationDescription("Updated Description");

        Medication updatedMedication = medicationRepository.update(medication);

        Assertions.assertEquals("Updated Medication", updatedMedication.getMedicationName());
        Assertions.assertEquals("Updated Description", updatedMedication.getMedicationDescription());
    }

    @Test
    void deleteMedication() {
        Medication medication = createMedicationObj();
        boolean isDeleted = medicationRepository.delete(medication.getId());

        Assertions.assertTrue(isDeleted);
    }

    static Medication createMedicationObj() {
        String uniqueMedicationName = "Medication Test " + System.currentTimeMillis();
        return medicationRepository.create(new Medication()
            .setMedicationName(uniqueMedicationName)
            .setMedicationDescription("Test Description"));
    }
}
