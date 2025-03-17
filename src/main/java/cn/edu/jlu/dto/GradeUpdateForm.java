package cn.edu.jlu.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class GradeUpdateForm {
	private String courseId;
	private List<GradeEntry> grades;

	@Data
	public static class GradeEntry {
		private String studentId;
		@DecimalMin("0.00")
		@DecimalMax("100.00")
		private BigDecimal newGrade;
	}
}
