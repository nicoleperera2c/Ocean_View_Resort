package com.oceanview.dao.interfaces;

import com.oceanview.exception.DAOException;
import com.oceanview.model.Payment;
import java.util.List;

public interface IPaymentDAO {
    
    int createPayment(Payment payment) throws DAOException;
    
    Payment findById(int paymentId) throws DAOException;
    
    List<Payment> findByReservationId(int reservationId) throws DAOException;
    
    boolean updatePaymentStatus(int paymentId, Payment.PaymentStatus status) throws DAOException;
}
