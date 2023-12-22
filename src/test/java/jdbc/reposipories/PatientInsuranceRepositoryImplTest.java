package jdbc.reposipories;

import com.solvd.hospital.entities.patient.Insurance;
import com.solvd.hospital.entities.patient.InsuranceType;
import com.solvd.hospital.entities.patient.Patient;
import com.solvd.hospital.repositories.PatientInsuranceRepository;
import com.solvd.hospital.repositories.impl.PatientInsuranceRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.Optional;

import static jdbc.reposipories.PatientRepositoryImplTest.createPatientObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatientInsuranceRepositoryImplTest {


    private static final PatientInsuranceRepository insuranceRepository = new PatientInsuranceRepositoryImpl();

    @Test
    void createInsurance() {
        Insurance insurance = createInsuranceObject();

        Assertions.assertNotNull(insurance);
        Assertions.assertNotNull(insurance.getPatientId());
    }

    @Test
    void findById() {
        Insurance insurance = createInsuranceObject();
        Optional<Insurance> foundInsurance = insuranceRepository.findById(insurance.getPatientId());

        Assertions.assertTrue(foundInsurance.isPresent());
        Assertions.assertEquals(insurance.getPatientId(), foundInsurance.get().getPatientId());
    }

    @Test
    void updateInsurance() {
        Insurance insurance = createInsuranceObject();

        insurance.setPolicyNumber("UpdatedPolicy123");
        insurance.setCoverageAmount(1500.0);
        insurance.setInsuranceProvider("UpdatedProvider");

        Insurance updatedInsurance = insuranceRepository.update(insurance);

        Assertions.assertNotNull(updatedInsurance);
        Assertions.assertEquals("UpdatedPolicy123", updatedInsurance.getPolicyNumber());
        Assertions.assertEquals(1500.0, updatedInsurance.getCoverageAmount());
        Assertions.assertEquals("UpdatedProvider", updatedInsurance.getInsuranceProvider());
    }

    @Test
    void deleteInsurance() {
        Insurance insurance = createInsuranceObject();

        boolean isDeleted = insuranceRepository.delete(insurance.getPatientId());

        Assertions.assertTrue(isDeleted);
    }

    private Insurance createInsuranceObject() {
        Patient patient = createPatientObj();
        return insuranceRepository.create(new Insurance()
            .setPatientId(patient.getId())
            .setPolicyNumber("Policy123")
            .setExpirationDate(LocalDate.now().plusYears(1))
            .setCoverageAmount(1000.0)
            .setType(InsuranceType.HEALTH)
            .setInsuranceProvider("ProviderXYZ"));
    }
}
