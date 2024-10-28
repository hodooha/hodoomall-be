package com.hodoo.hodoomall.order.model.dto;

import com.hodoo.hodoomall.user.model.dto.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QueryDTO {

    private String id;
    private String orderNum;
    private int page = 1;
    private int pageSize = 5;
    private User user;
    private String status;
    private String sortBy;
    private String productName;

}
