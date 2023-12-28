package com.solvd.hospital.dao.mybatis.mappers;

import com.solvd.hospital.entities.bill.Bill;
import com.solvd.hospital.entities.bill.PaymentStatus;
import org.apache.ibatis.annotations.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillMapper {
    @Insert("INSERT INTO bills (patient_id, amount, billing_date, payment_status) " +
            "VALUES (#{patientId}, #{amount}, #{billingDate}, #{paymentStatus})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void create(Bill bill);

    @Select("SELECT * FROM bills WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "billingDate", column = "billing_date"),
            @Result(property = "paymentStatus", column = "payment_status", javaType = PaymentStatus.class),
    })
    Optional<Bill> findById(long id);

    @Select("SELECT * FROM bills")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "billingDate", column = "billing_date"),
            @Result(property = "paymentStatus", column = "payment_status", javaType = PaymentStatus.class),
    })
    List<Bill> findAll();

    @Select("SELECT * FROM bills WHERE patient_id = #{patientId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "billingDate", column = "billing_date"),
            @Result(property = "paymentStatus", column = "payment_status", javaType = PaymentStatus.class),
    })
    List<Bill> findByPatientId(long patientId);

    @Select("SELECT * FROM bills WHERE patient_id = #{patientId} AND billing_date = #{date}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "billingDate", column = "billing_date"),
            @Result(property = "paymentStatus", column = "payment_status", javaType = PaymentStatus.class),
    })
    List<Bill> findByPatientIdAndBillingDate(@Param("patientId") long patientId, @Param("date") LocalDate date);

    @Select("SELECT * FROM bills WHERE patient_id = #{patientId} AND payment_status = #{status}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "patientId", column = "patient_id"),
            @Result(property = "amount", column = "amount"),
            @Result(property = "billingDate", column = "billing_date"),
            @Result(property = "paymentStatus", column = "payment_status", javaType = PaymentStatus.class),
    })
    List<Bill> findByPatientIdAndPaymentStatus(@Param("patientId") long patientId, @Param("status") PaymentStatus status);

    @Update("UPDATE bills SET patient_id = #{patientId}, amount = #{amount}, " +
            "billing_date = #{billingDate}, payment_status = #{paymentStatus} WHERE id = #{id}")
    void update(Bill bill);

    @Delete("DELETE FROM bills WHERE id = #{id}")
    void delete(long id);
}
