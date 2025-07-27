package com.ticketbooking.service;

import com.ticketbooking.dao.TicketTypeDAO;
import com.ticketbooking.model.TicketType;

import java.sql.SQLException;
import java.util.List;

public class TicketTypeService {
    private TicketTypeDAO ticketTypeDAO;

    public TicketTypeService() throws SQLException {
        this.ticketTypeDAO = new TicketTypeDAO();
    }

    public boolean addTicketType(TicketType ticketType) throws SQLException {
        return ticketTypeDAO.addTicketType(ticketType);
    }

    public boolean updateTicketType(TicketType ticketType) throws SQLException {
        return ticketTypeDAO.updateTicketType(ticketType);
    }

    public List<TicketType> getTicketTypesByEventId(int eventId) throws SQLException {
        return ticketTypeDAO.getTicketTypesByEventId(eventId);
    }

    public boolean deleteTicketType(int ticketTypeId) throws SQLException {
        return ticketTypeDAO.deleteTicketType(ticketTypeId);
    }

    public boolean updateAvailableQuantity(int ticketTypeId, int quantity) throws SQLException {
        return ticketTypeDAO.updateAvailableQuantity(ticketTypeId, quantity);
    }
} 