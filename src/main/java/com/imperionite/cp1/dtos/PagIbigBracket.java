package com.imperionite.cp1.dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class PagIbigBracket {
    private BigDecimal salaryCap;
    private BigDecimal contributionRate;
}