package cn.edu.jlu.service.impl;

import cn.edu.jlu.dto.CourseWithEnrollment;
import cn.edu.jlu.entity.Course;
import cn.edu.jlu.repository.CourseRepository;
import cn.edu.jlu.service.CourseService;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
	private final CourseRepository courseRepository;

	public CourseServiceImpl(CourseRepository courseRepository) {
		this.courseRepository = courseRepository;
	}

	@Override
	public List<Course> findByTeacherId(String teacherId) {
		return courseRepository.findByTeacher_TeacherId(teacherId);
	}

	@Override
	public List<Course> findBySemester(String semester) {
		return courseRepository.findBySemesterWithTeacher(semester);
	}

	@Override
	public List<CourseWithEnrollment> findCoursesWithEnrollment(String teacherId) {
		List<Object[]> results = courseRepository.findCoursesWithStudentCount(teacherId);

		return results.stream().map(result -> {
			String courseId = (String) result[0];
			String courseName = (String) result[1];
			Integer status =  (Integer) result[2];
			Integer credit = (Integer) result[3];
			String semester = (String) result[4];
			String classroom = (String) result[5];
			Long count = (Long) result[6];

			return new CourseWithEnrollment(
					courseId, courseName, status, credit, semester, classroom, count
			);
		}).collect(Collectors.toList());
	}

	@Override
	public Course validateTeacherCourse(String teacherId, String courseId) {
		return courseRepository.findByCourseIdAndTeacher_TeacherId(courseId, teacherId);
	}

	@Override
	public boolean existsByCourseIdAndTeacher(String courseId, String teacherId) {
		return courseRepository.existsByCourseIdAndTeacher_TeacherId(courseId, teacherId);
	}

	@Override
	public List<Course> findAllWithTeacher() {
		return courseRepository.findAllWithTeacher();
	}

	@Override
	@Transactional
	public void closeCourse(String courseId) throws Exception {
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("课程不存在"));
		if (course.getStatus() == 1) {
			throw new Exception("课程已结课");
		}
		course.setStatus(1);
		courseRepository.save(course);
	}
}
