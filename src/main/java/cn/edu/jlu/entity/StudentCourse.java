package cn.edu.jlu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "student_courses")
@Data
@IdClass(StudentCourse.StudentCourseId.class)
public class StudentCourse {
	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "course_id", nullable = false)
	private Course course;

	@Column(precision = 5, scale = 2)
	private BigDecimal grade;

	// 联合主键类
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class StudentCourseId implements Serializable {
		private String student;
		private String course;
	}
}