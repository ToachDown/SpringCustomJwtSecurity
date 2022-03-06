package com.example.jwtcleanspring.model;

import com.example.jwtcleanspring.model.enums.Taste;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

@Data
@AllArgsConstructor
@Builder
public class Food {

    private String name;
    private List<Taste> taste;
    private BigDecimal price;
    private String country;

}
