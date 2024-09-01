package com.geocodinglocationservices.payload.response.Report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderQuantityByMonthDTO {
    private int year;
    private int month;
    private long orderCount;
}
