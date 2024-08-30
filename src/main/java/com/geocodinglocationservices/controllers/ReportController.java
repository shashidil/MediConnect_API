package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.ReportService;
import com.geocodinglocationservices.models.MedicationDetail;
import com.geocodinglocationservices.payload.response.Report.MostSoldMedicineDTO;
import com.geocodinglocationservices.payload.response.Report.OrderQuantityByDayDTO;
import com.geocodinglocationservices.payload.response.Report.OrderReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;
    @GetMapping("/orders-by-date-range")
    public List<OrderReportDto> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        return reportService.getOrdersWithinDateRange(fromDate, toDate);
    }

    @GetMapping("/top-medicines")
    public List<MostSoldMedicineDTO> getTop5Medicines(
            @RequestParam int month,
            @RequestParam int year) {
        return reportService.getTop5MostSoldMedicines(month, year);
    }

    @GetMapping("/order-quantities-by-month")
    public List<OrderQuantityByDayDTO> getOrderQuantitiesByMonth(
            @RequestParam int month,
            @RequestParam int year) {
        return reportService.getOrderQuantitiesByMonth(month, year);
    }



}
