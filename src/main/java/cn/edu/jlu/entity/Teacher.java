package cn.edu.jlu.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

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

	// 关联教授的课程（一对多）
	@OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Course> courses = new ArrayList<>();
}