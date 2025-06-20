package fr.esic.mastering.dto;



import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ConvocationDTO {
    private String email;
    private String title;
    private String lieu;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dateTime;
    private String additionalInfo;
}