package cn.edu.jlu.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class StudentUpdateForm {
	@NotNull(message = "年龄不能为空")
	@Min(value = 1, message = "年龄不合法")
	@Max(value = 150, message = "年龄不合法")
	private Integer age;

	@NotNull(message = "年级不能为空")
	@Pattern(regexp = "大一|大二|大三|大四", message = "非法年级值")
	private String semester;

	@NotNull(message = "专业不能为空")
	@Size(min = 2, max = 100, message = "专业名称2-100字符")
	private String major;

	private String newPassword;
}
