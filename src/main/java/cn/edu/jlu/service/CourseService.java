package cn.edu.jlu.service;

import cn.edu.jlu.dto.AdminCourseDTO;
import cn.edu.jlu.dto.CourseWithEnrollment;
import cn.edu.jlu.entity.Course;
import java.util.List;

public interface CourseService {
	List<Course> findByTeacherId(String teacherId);

	List<Course> findBySemester(String semester);

	List<CourseWithEnrollment> findCoursesWithEnrollment(String teacherId);

	Course validateTeacherCourse(String teacherId, String courseId);

	boolean existsByCourseIdAndTeacher(String courseId, String teacherId);

	List<Course> findAllWithTeacher();

	void closeCourse(String courseId) throws Exception;

	List<AdminCourseDTO> findAdminCourseData();

	void validateCourseClosable(String courseId) throws Exception;
}
