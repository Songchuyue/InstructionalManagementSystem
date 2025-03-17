package cn.edu.jlu.service.impl;

import cn.edu.jlu.entity.Course;
import cn.edu.jlu.repository.CourseRepository;
import cn.edu.jlu.service.CourseService;
import org.springframework.stereotype.Service;
import java.util.List;

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
}
