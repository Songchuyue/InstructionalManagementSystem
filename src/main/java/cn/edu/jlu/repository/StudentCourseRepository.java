package cn.edu.jlu.repository;

import cn.edu.jlu.entity.StudentCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentCourseRepository extends JpaRepository<StudentCourse, StudentCourse.StudentCourseId> {
	List<StudentCourse> findByCourse_CourseId(String courseId);

	StudentCourse findByStudent_StudentIdAndCourse_CourseId(String studentId, String courseId);

	boolean existsByStudent_StudentIdAndCourse_CourseId(String studentId, String courseId);

//	List<StudentCourse> findByStudent_StudentId(String studentId);

	@Query("SELECT sc FROM StudentCourse sc " +
			"LEFT JOIN FETCH sc.course " +  // 使用LEFT JOIN防止课程不存在时报错
			"WHERE sc.student.studentId = :studentId")
	List<StudentCourse> findByStudentWithCourses(@Param("studentId") String studentId);

	@Query("SELECT COUNT(sc) FROM StudentCourse sc " +
			"WHERE sc.courseId = :courseId AND sc.grade IS NULL")
	long countByCourseIdAndGradeIsNull(@Param("courseId") String courseId);
}
