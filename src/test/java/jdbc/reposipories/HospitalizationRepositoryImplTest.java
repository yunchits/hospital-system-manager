package jdbc.reposipories;

import com.solvd.hospital.entities.Hospitalization;
import com.solvd.hospital.repositories.HospitalizationRepository;
import com.solvd.hospital.repositories.impl.HospitalizationRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;

import static jdbc.reposipories.PatientRepositoryImplTest.createPatientObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HospitalizationRepositoryImplTest {

    private static final HospitalizationRepository hospitalizationRepository = new HospitalizationRepositoryImpl();

    @Test
    void createHospitalization() {
        Hospitalization hospitalization = createHospitalizationObject();

        Assertions.assertNotNull(hospitalization.getId());
    }

    @Test
    void findByPatientId() {
        Hospitalization hospitalization = createHospitalizationObject();
        List<Hospitalization> hospitalizations = hospitalizationRepository.findByPatientId(hospitalization.getPatient().getId());

        Assertions.assertFalse(hospitalizations.isEmpty());
    }

    @Test
    void deleteHospitalization() {
        Hospitalization hospitalization = createHospitalizationObject();

        boolean result = hospitalizationRepository.delete(hospitalization.getId());

        Assertions.assertTrue(result);

        List<Hospitalization> deletedHospitalizations = hospitalizationRepository.findByPatientId(hospitalization.getPatient().getId());
        Assertions.assertTrue(deletedHospitalizations.isEmpty());
    }

    private Hospitalization createHospitalizationObject() {
        return hospitalizationRepository.create(new Hospitalization()
            .setPatient(createPatientObj())
            .setAdmissionDate(LocalDate.now())
            .setDischargeDate(LocalDate.now().plusDays(7)));
    }
}
