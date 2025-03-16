package cn.edu.jlu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
public class Student {
	@Id
	@Column(name = "student_id", length = 20)
	private String studentId;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 100)
	private String password;

	@Column(length = 10)
	private String gender;

	@Min(value = 10, message = "年龄不能小于10岁")
	@Max(value = 100, message = "年龄不能超过100岁")
	private Integer age;

	@Column(length = 20)
	private String grade;

	@Column(length = 100)
	private String major;

	@CreatedDate
	@Column(name = "create_time", updatable = false)
	private LocalDateTime createTime;

	@LastModifiedDate
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StudentCourse> courses = new ArrayList<>();
}
