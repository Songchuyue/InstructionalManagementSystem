package cn.edu.jlu.dto;

import cn.edu.jlu.entity.Course;

public class CourseWithEnrollment {
	private String courseId;
	private String courseName;
	private Integer status;
	private Integer credit;
	private String semester;
	private String classroom;
	private Long studentCount;

	// 全参构造函数
	public CourseWithEnrollment(String courseId, String courseName,Integer status, Integer credit,
								String semester, String classroom, Long studentCount) {
		this.courseId = courseId;
		this.courseName = courseName;
		this.status = status;
		this.credit = credit;
		this.semester = semester;
		this.classroom = classroom;
		this.studentCount = studentCount;
	}

	// Getter 方法
	public String getCourseId() { return courseId; }
	public String getCourseName() { return courseName; }
	public Integer getStatus() { return status; }
	public Integer getCredit() { return credit; }
	public String getSemester() { return semester; }
	public String getClassroom() { return classroom; }
	public Long getStudentCount() { return studentCount; }
}
