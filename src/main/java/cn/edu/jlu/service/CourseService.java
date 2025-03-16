package cn.edu.jlu.service;

import cn.edu.jlu.entity.Course;
import java.util.List;

public interface CourseService {
	List<Course> findByTeacherId(String teacherId);
}
