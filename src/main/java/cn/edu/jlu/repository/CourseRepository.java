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

	@Query("SELECT c.courseId, c.courseName, c.credit, c.semester, c.classroom, COUNT(sc.student) " +
			"FROM Course c " +
			"LEFT JOIN c.studentCourses sc " +
			"WHERE c.teacher.teacherId = :teacherId " +
			"GROUP BY c.courseId, c.courseName, c.credit, c.semester, c.classroom")
	List<Object[]> findCoursesWithStudentCount(@Param("teacherId") String teacherId);

	@Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.courseId = :courseId AND c.teacher.teacherId = :teacherId")
	boolean existsByCourseIdAndTeacher(@Param("courseId") String courseId,
									   @Param("teacherId") String teacherId);

	@Query("SELECT c FROM Course c WHERE c.courseId = ?1 AND c.teacher.teacherId = ?2")
	Course findByCourseIdAndTeacher_TeacherId(String courseId, String teacherId);

	@Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.courseId = ?1 AND c.teacher.teacherId = ?2")
	boolean existsByCourseIdAndTeacher_TeacherId(String courseId, String teacherId);
}
