package com.geocodinglocationservices.Service;

import com.geocodinglocationservices.models.MedicationDetail;
import com.geocodinglocationservices.payload.response.Report.MostSoldMedicineDTO;
import com.geocodinglocationservices.payload.response.Report.OrderQuantityByDayDTO;
import com.geocodinglocationservices.payload.response.Report.OrderQuantityByMonthDTO;
import com.geocodinglocationservices.payload.response.Report.OrderReportDto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {
    List<OrderReportDto> getOrdersWithinDateRange(LocalDateTime fromDate, LocalDateTime toDate);

    List<MostSoldMedicineDTO> getTop5MostSoldMedicines(int month, int year);

    List<OrderQuantityByDayDTO> getOrderQuantitiesByMonth(int month, int year);


    List<OrderQuantityByMonthDTO> getOrderQuantitiesByLast12Months(LocalDateTime startDate, LocalDateTime currentDate);
}
