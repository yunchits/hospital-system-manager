package jdbc.dao;

import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import com.solvd.hospital.dao.BillDAO;
import com.solvd.hospital.dao.impl.JDBCBillDAOImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.LocalDate;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JDBCBillDAOImplTest {

    private BillDAO billDAO;

    @BeforeAll
    void setUp() {
        billDAO = new JDBCBillDAOImpl();
    }

    @Test
    void createBill() {
        Bill bill = new Bill()
            .setPatientId(1L)
            .setAmount(100.0)
            .setBillingDate(LocalDate.now())
            .setPaymentStatus(PaymentStatus.PAID);

        Bill createdBill = billDAO.create(bill);

        Assertions.assertNotNull(createdBill.getId());
    }

    @Test
    void findByPatientId() {
        List<Bill> bills = billDAO.findByPatientId(1L);

        Assertions.assertFalse(bills.isEmpty());
    }

    @Test
    void findByPatientIdAndBillingDate() {
        LocalDate date = LocalDate.now();

        List<Bill> bills = billDAO.findByPatientIdAndBillingDate(1L, date);

        Assertions.assertFalse(bills.isEmpty());
    }

    @Test
    void findByPatientIdAndPaymentStatus() {
        PaymentStatus status = PaymentStatus.PAID;

        List<Bill> bills = billDAO.findByPatientIdAndPaymentStatus(1L, status);

        Assertions.assertFalse(bills.isEmpty());
    }

    @Test
    void updateBill() {
        Bill bill = new Bill()
            .setPatientId(1L)
            .setAmount(150.0)
            .setBillingDate(LocalDate.now())
            .setPaymentStatus(PaymentStatus.PAID);
        Bill createdBill = billDAO.create(bill);


        createdBill.setAmount(200.0);
        Bill updatedBill = billDAO.update(createdBill);

        Assertions.assertEquals(200.0, updatedBill.getAmount());
    }

    @Test
    void deleteBill() {
        Bill bill = new Bill()
            .setPatientId(1L)
            .setAmount(300.0)
            .setBillingDate(LocalDate.now())
            .setPaymentStatus(PaymentStatus.PAID);
        Bill createdBill = billDAO.create(bill);

        boolean isDeleted = billDAO.delete(createdBill.getId());

        Assertions.assertTrue(isDeleted);
    }
}
