package com.geocodinglocationservices.controllers;

import com.geocodinglocationservices.Service.ReportService;
import com.geocodinglocationservices.models.MedicationDetail;
import com.geocodinglocationservices.payload.response.Report.MostSoldMedicineDTO;
import com.geocodinglocationservices.payload.response.Report.OrderQuantityByDayDTO;
import com.geocodinglocationservices.payload.response.Report.OrderQuantityByMonthDTO;
import com.geocodinglocationservices.payload.response.Report.OrderReportDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
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

    @GetMapping("/top-medicines/month")
    public List<MostSoldMedicineDTO> getTop5Medicines() {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Get the previous month and year
        YearMonth lastMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonthValue()).minusMonths(1);
        int month = lastMonth.getMonthValue();
        int year = lastMonth.getYear();

        return reportService.getTop5MostSoldMedicines(month, year);
    }

    @GetMapping("/order-quantities-by-month")
    public List<OrderQuantityByDayDTO> getOrderQuantitiesByMonth(
            @RequestParam int month,
            @RequestParam int year) {
        return reportService.getOrderQuantitiesByMonth(month, year);
    }
    @GetMapping("/order-quantities-month")
    public List<OrderQuantityByMonthDTO> getOrderQuantitiesByLast12Months() {
        LocalDate currentDate = LocalDate.now();
        LocalDate startDate = currentDate.minusMonths(12).withDayOfMonth(1);

        // Convert LocalDate to LocalDateTime at the start of the day
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = currentDate.atStartOfDay();

        return reportService.getOrderQuantitiesByLast12Months(startDateTime, endDateTime);
    }




}
