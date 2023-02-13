package net.springboot.synpulse8challenges.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ResponseObject {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss" )
    private LocalDateTime timestamp;
    private String status;
    private String code;
    private List<String> message;
    private Object obj;
}
