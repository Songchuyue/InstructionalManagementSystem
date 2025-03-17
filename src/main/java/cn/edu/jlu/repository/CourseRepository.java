package cn.edu.jlu.repository;

import cn.edu.jlu.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {
	List<Course> findByTeacher_TeacherId(String teacherId);

	// 新增带教师信息的查询
	@Query("SELECT c FROM Course c JOIN FETCH c.teacher WHERE c.semester = :semester")
	List<Course> findBySemesterWithTeacher(@Param("semester") String semester);
}
