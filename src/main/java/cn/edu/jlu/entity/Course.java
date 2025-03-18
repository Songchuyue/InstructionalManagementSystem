package cn.edu.jlu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", nullable = false)
	private Teacher teacher;

	private Integer credit;

	@Column(length = 20)
	private String semester;

	@Column(length = 100)
	private String classroom;

	@Column(nullable = false, columnDefinition = "TINYINT DEFAULT 0")
	private Integer status;

	@OneToMany(
			mappedBy = "course",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<StudentCourse> studentCourses = new ArrayList<>();

	@JsonProperty("teacherId")  // 直接暴露教师ID到JSON
	public String getTeacherId() {
		return teacher != null ? teacher.getTeacherId() : null;
	}

	@Transient
	public String getTeacherName() {
		return teacher != null ? teacher.getName() : "未知教师";
	}

	@Transient
	public String getTeacherEmail() {
		return teacher != null ? teacher.getEmail() : "无邮箱";
	}
}
