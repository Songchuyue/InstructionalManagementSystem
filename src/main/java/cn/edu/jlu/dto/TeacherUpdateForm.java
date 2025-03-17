package cn.edu.jlu.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TeacherUpdateForm {
	@Min(value = 18, message = "年龄不能小于18岁")
	@Max(value = 70, message = "年龄不能超过70岁")
	private Integer age;

	@Pattern(regexp = "男|女", message = "性别只能是男或女")
	private String gender;

	@Email(message = "邮箱格式不正确")
	private String email;

	private String newPassword; // 新密码（可选）
}
