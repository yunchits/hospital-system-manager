package jdbc.reposipories;

import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.entities.PatientDiagnosis;
import com.solvd.hospital.repositories.PatientDiagnosisRepository;
import com.solvd.hospital.repositories.impl.PatientDiagnosisRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static jdbc.reposipories.DiagnosisRepositoryImplTest.createDiagnosisObj;
import static jdbc.reposipories.PatientRepositoryImplTest.createPatientObj;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PatientDiagnosisRepositoryImplTest {

    private static final PatientDiagnosisRepository diagnosisRepository = new PatientDiagnosisRepositoryImpl();

    @Test
    void createPatientDiagnosis() {
        PatientDiagnosis patientDiagnosis = createPatientDiagnosisObj();

        Assertions.assertNotNull(patientDiagnosis.getDiagnosis());
    }

    @Test
    void findAllByPatientId() {
        PatientDiagnosis patientDiagnosis = createPatientDiagnosisObj();

        List<PatientDiagnosis> patientDiagnoses = diagnosisRepository.findAllByPatientId(patientDiagnosis.getPatientId());

        Assertions.assertNotNull(patientDiagnoses);
        Assertions.assertFalse(patientDiagnoses.isEmpty());
        Assertions.assertTrue(patientDiagnoses.stream()
            .anyMatch(pd -> pd.getPatientId() == patientDiagnosis.getPatientId()));
    }

    @Test
    void deletePatientDiagnosis() {
        PatientDiagnosis patientDiagnosis = createPatientDiagnosisObj();

        boolean result = diagnosisRepository.delete(patientDiagnosis.getPatientId(), patientDiagnosis.getDiagnosis().getId());

        Assertions.assertTrue(result);

        List<PatientDiagnosis> remainingDiagnoses = diagnosisRepository.findAllByPatientId(patientDiagnosis.getPatientId());

        Assertions.assertTrue(remainingDiagnoses.isEmpty());
    }

    static PatientDiagnosis createPatientDiagnosisObj() {
        return diagnosisRepository.create(new PatientDiagnosis()
            .setPatientId(createPatientObj().getId())
            .setDiagnosis(createDiagnosisObj()));
    }
}
