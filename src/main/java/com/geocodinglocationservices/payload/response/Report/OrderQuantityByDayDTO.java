package com.geocodinglocationservices.payload.response.Report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderQuantityByDayDTO {
    private int day;
    private long quantity;
}
