package jdbc.dao;

import com.solvd.hospital.entities.Diagnosis;
import com.solvd.hospital.dao.DiagnosisDAO;
import com.solvd.hospital.dao.jdbc.impl.JDBCDiagnosisDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCDiagnosisDAOImplTest {

    private static final DiagnosisDAO diagnosisRepository = new JDBCDiagnosisDAOImpl();

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

        Optional<Diagnosis> foundDiagnosis = diagnosisRepository.findByDiagnosisName(diagnosis.getName());

        Assertions.assertTrue(foundDiagnosis.isPresent());
        Assertions.assertEquals(diagnosis.getName(), foundDiagnosis.get().getName());
        Assertions.assertEquals(diagnosis.getDescription(), foundDiagnosis.get().getDescription());
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
            .setName(uniqueDiagnosisName)
            .setDescription("TestDescription"));
    }

}
