package com.imperionite.cp1.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyCutoffDTO {
    private LocalDate startDate;
    private LocalDate endDate;
}
