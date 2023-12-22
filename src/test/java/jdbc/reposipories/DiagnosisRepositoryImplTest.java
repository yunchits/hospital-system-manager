package jdbc.reposipories;

import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.repositories.DiagnosisRepository;
import com.solvd.hospital.repositories.impl.DiagnosisRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DiagnosisRepositoryImplTest {

    private static final DiagnosisRepository diagnosisRepository = new DiagnosisRepositoryImpl();

    @Test
    void createDiagnosis() {
        Diagnosis diagnosis = createDiagnosisObj();

        Assertions.assertNotNull(diagnosis.getId());
    }

    @Test
    void findById() {
        Diagnosis createdDiagnosis = createDiagnosisObj();

        Optional<Diagnosis> foundDiagnosis = diagnosisRepository.findById(createdDiagnosis.getId());

        Assertions.assertTrue(foundDiagnosis.isPresent());
    }

    @Test
    void findByDiagnosisName() {
        Diagnosis diagnosis = createDiagnosisObj();

        Optional<Diagnosis> foundDiagnosis = diagnosisRepository.findByDiagnosisName(diagnosis.getDiagnosisName());

        Assertions.assertTrue(foundDiagnosis.isPresent());
        Assertions.assertEquals(diagnosis.getDiagnosisName(), foundDiagnosis.get().getDiagnosisName());
        Assertions.assertEquals(diagnosis.getDiagnosisDescription(), foundDiagnosis.get().getDiagnosisDescription());
    }

    @Test
    void deleteDiagnosis() {
        Diagnosis diagnosis = createDiagnosisObj();

        boolean result = diagnosisRepository.delete(diagnosis.getId());

        Assertions.assertTrue(result);

        Optional<Diagnosis> deletedDiagnosis = diagnosisRepository.findById(diagnosis.getId());

        Assertions.assertFalse(deletedDiagnosis.isPresent());
    }

    static Diagnosis createDiagnosisObj() {
        String uniqueDiagnosisName = "Diagnosis Test " + System.currentTimeMillis();
        return diagnosisRepository.create(new Diagnosis()
            .setDiagnosisName(uniqueDiagnosisName)
            .setDiagnosisDescription("TestDescription"));
    }

}
