package cn.edu.jlu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "teachers")
@Data
public class Teacher {
	@Id
	@Column(name = "teacher_id", length = 20)
	private String teacherId;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 100)
	private String password;

	@Column(length = 10)
	private String gender;

	@Min(value = 18, message = "教师年龄不能小于18岁")
	@Max(value = 100, message = "教师年龄不能超过100岁")
	private Integer age;

	@Email(message = "邮箱格式不正确")
	@Column(length = 100)
	private String email;

	@CreatedDate
	@Column(name = "create_time", updatable = false)
	private LocalDateTime createTime;

	@LastModifiedDate
	@Column(name = "update_time")
	private LocalDateTime updateTime;
}