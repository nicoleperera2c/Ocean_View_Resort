package com.oceanview.dao;

import com.oceanview.dao.impl.*;
import com.oceanview.dao.interfaces.*;

public class DAOFactory {
    
    private static DAOFactory instance;
    
    private DAOFactory() {
    }
    
    public static synchronized DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }
    
    public IUserDAO getUserDAO() {
        return new UserDAOImpl();
    }
    
    public IGuestDAO getGuestDAO() {
        return new GuestDAOImpl();
    }
    
    public IRoomDAO getRoomDAO() {
        return new RoomDAOImpl();
    }
    
    public IReservationDAO getReservationDAO() {
        return new ReservationDAOImpl();
    }
    
    public IPaymentDAO getPaymentDAO() {
        return new PaymentDAOImpl();
    }
}
