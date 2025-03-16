package cn.edu.jlu.repository;

import cn.edu.jlu.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {
	List<Course> findByTeacher_TeacherId(String teacherId);
}
