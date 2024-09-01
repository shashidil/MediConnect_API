package com.geocodinglocationservices.Service.impl;

import com.geocodinglocationservices.Service.ReportService;
import com.geocodinglocationservices.models.MedicationDetail;
import com.geocodinglocationservices.models.Order;
import com.geocodinglocationservices.payload.response.Report.MostSoldMedicineDTO;
import com.geocodinglocationservices.payload.response.Report.OrderQuantityByDayDTO;
import com.geocodinglocationservices.payload.response.Report.OrderReportDto;
import com.geocodinglocationservices.repository.InvoiceRepository;
import com.geocodinglocationservices.repository.OrderRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private InvoiceRepository invoiceRepository;

    private ModelMapper modelMapper = new ModelMapper();
    @Override
    public List<OrderReportDto> getOrdersWithinDateRange(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Order> byOrderDateBetween = orderRepo.findByOrderDateBetween(fromDate, toDate);
        return modelMapper.map(byOrderDateBetween,new TypeToken<List<OrderReportDto>>() {}.getType());
    }

    @Override
    public List<MostSoldMedicineDTO> getTop5MostSoldMedicines(int month, int year) {
        return invoiceRepository.findTop5MostSoldMedicines(month, year);
    }

    @Override
    public List<OrderQuantityByDayDTO> getOrderQuantitiesByMonth(int month, int year) {
        return orderRepo.findOrderQuantitiesByMonth(month, year);
    }

    @Override
    public List<OrderQuantityByDayDTO> getOrderQuantitiesByLast12Months(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepo.findOrderQuantitiesByLast12Months(startDate, endDate);
    }


}
