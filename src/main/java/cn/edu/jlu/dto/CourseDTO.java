package cn.edu.jlu.dto;

import cn.edu.jlu.entity.Course;

// CourseDTO.java
public record CourseDTO(
		String courseId,
		String courseName,
		Integer credit,
		String semester,
		String classroom,
		String teacherId  // 只返回需要的教师ID
) {
	public static CourseDTO from(Course course) {
		return new CourseDTO(
				course.getCourseId(),
				course.getCourseName(),
				course.getCredit(),
				course.getSemester(),
				course.getClassroom(),
				course.getTeacher().getTeacherId()
		);
	}
}
