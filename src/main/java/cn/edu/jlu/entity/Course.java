package cn.edu.jlu.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
public class Course {
	@Id
	@Column(name = "course_id", length = 20)
	private String courseId;

	@Column(name = "course_name", nullable = false, length = 100)
	private String courseName;

	@Column
	private Integer credit;

	@Column(length = 20)
	private String semester;

	@Column(length = 100)
	private String classroom;

	// 关联教师（多对一）
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", nullable = false)
	private Teacher teacher;

	// 关联选课记录（一对多）
	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StudentCourse> studentCourses = new ArrayList<>();
}