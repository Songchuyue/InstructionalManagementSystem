package cn.edu.jlu.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TeacherDTO {
	@NotEmpty(message = "工号不能为空")
	private String teacherId;

	@NotEmpty(message = "密码不能为空")
	private String password;
}
