package cn.edu.jlu.repository;

import cn.edu.jlu.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourse.StudentCourseId> {
	boolean existsByStudent_StudentIdAndCourse_CourseId(String studentId, String courseId);

	List<StudentCourse> findByStudent_StudentId(String studentId);

	void deleteById(StudentCourse.StudentCourseId id);
}
