package cn.edu.jlu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class GradeManagementDTO {
	private String studentId;
	private String studentName;
	private BigDecimal currentGrade;
}
