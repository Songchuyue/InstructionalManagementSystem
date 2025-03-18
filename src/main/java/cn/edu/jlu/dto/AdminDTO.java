package cn.edu.jlu.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class AdminDTO {
	@NotEmpty(message = "管理员ID不能为空")
	private String adminId;

	@NotEmpty(message = "密码不能为空")
	private String password;
}
