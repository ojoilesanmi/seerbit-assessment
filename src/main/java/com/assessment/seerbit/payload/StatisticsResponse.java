package com.assessment.seerbit.payload;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class StatisticsResponse {
    private BigDecimal sum;
    private BigDecimal avg;
    private BigDecimal max;
    private BigDecimal min;
    private long count;
    public String message;

    public StatisticsResponse(String message) {
        this.message = message;
    }

    public StatisticsResponse(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, long count){
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }
}
