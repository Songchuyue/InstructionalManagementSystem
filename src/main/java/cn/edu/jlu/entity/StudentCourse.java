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
	// 基本类型主键字段
	@Id
	@Column(name = "student_id", length = 20)
	private String studentId;

	@Id
	@Column(name = "course_id", length = 20)
	private String courseId;

	// 关联实体（非主键字段）
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "student_id",
			referencedColumnName = "student_id",
			insertable = false,
			updatable = false
	)
	private Student student;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
			name = "course_id",
			referencedColumnName = "course_id",
			insertable = false,
			updatable = false
	)
	private Course course;

	// 其他业务字段
	@Column(precision = 5, scale = 2)
	private BigDecimal grade;

	// 联合主键类
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class StudentCourseId implements Serializable {
		private String studentId;
		private String courseId;
	}
}
