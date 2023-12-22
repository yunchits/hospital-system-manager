package jdbc.reposipories;

import com.solvd.hospital.entities.Prescription;
import com.solvd.hospital.repositories.PrescriptionRepository;
import com.solvd.hospital.repositories.impl.PrescriptionRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static jdbc.reposipories.DoctorRepositoryImplTest.createDoctorObj;
import static jdbc.reposipories.MedicationRepositoryImplTest.createMedicationObj;
import static jdbc.reposipories.PatientRepositoryImplTest.createPatientObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrescriptionRepositoryImplTest {

    private static final PrescriptionRepository prescriptionRepository = new PrescriptionRepositoryImpl();

    @Test
    void createPrescription() {
        Prescription prescription = createPrescriptionObj();

        Assertions.assertNotNull(prescription.getId());
    }

    @Test
    void findByPatientId() {
        Prescription prescription = createPrescriptionObj();
        long patientId = prescription.getPatient().getId();

        List<Prescription> prescriptions = prescriptionRepository.findByPatientId(patientId);

        Assertions.assertTrue(!prescriptions.isEmpty());
    }

    @Test
    void deleteByPatientId() {
        Prescription prescription = createPrescriptionObj();
        long patientId = prescription.getPatient().getId();

        boolean isDeleted = prescriptionRepository.deleteByPatientId(patientId);

        Assertions.assertTrue(isDeleted);
    }

    static Prescription createPrescriptionObj() {
        return prescriptionRepository.create(new Prescription()
            .setPatient(createPatientObj())
            .setDoctor(createDoctorObj())
            .setMedication(createMedicationObj()));
    }
}
