package com.geocodinglocationservices.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegNumberResponse {

    private String cat_name;
    private String reg_no;
    private String name_full;
    private String date_registered;

    private String address;

}