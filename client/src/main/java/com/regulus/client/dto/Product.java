package com.regulus.client.dto;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Product {

    private String id;
    private String name;
    private Double price;
    private Date createAt;

}
