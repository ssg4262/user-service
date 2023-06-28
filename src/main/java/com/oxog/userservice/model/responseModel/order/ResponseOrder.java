package com.oxog.userservice.model.responseModel.order;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseOrder {
    private String productId; // 상품아이디
    private Integer Qty; //수량
    private Integer unitPrice; // 개당가격
    private Integer totalPrice; // 총 가격
    private Date createdAt;

    private String orderId;// 주문자 아이디
}
