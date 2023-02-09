package net.springboot.synpulse8challenges.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class ResponseObject {
    private LocalDate timestamp;
    private String status;
    private String code;
    private List<String> message;
    private Object obj;
}
