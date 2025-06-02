package fr.esic.mastering.dto;



import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ConvocationDTO {
    private Long userId;
    private String title;
    private String location;
    private LocalDateTime dateTime;
    private String additionalInfo;
}