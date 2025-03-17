package cn.edu.jlu.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class StudentUpdateForm {
	@Min(value = 10, message = "年龄不能小于10岁")
	@Max(value = 100, message = "年龄不能超过100岁")
	private Integer age;

	private String semester;

	private String major;

	private String newPassword;
}
