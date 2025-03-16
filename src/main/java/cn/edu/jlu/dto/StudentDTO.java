package cn.edu.jlu.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class StudentDTO {
	@NotEmpty(message = "学号不能为空")
	private String studentId;

	@NotEmpty(message = "密码不能为空")
	private String password;
}
