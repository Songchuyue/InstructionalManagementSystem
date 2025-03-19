package cn.edu.jlu.repository;

import cn.edu.jlu.dto.AdminCourseDTO;
import cn.edu.jlu.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, String> {
	List<Course> findByTeacher_TeacherId(String teacherId);

	/*
	 *SELECT
	 *     c.course_id,
	 *     c.course_name,
	 *     c.teacher_id,
	 *     c.credit,
	 *     c.semester,
	 *     c.classroom,
	 *     t.teacher_id,
	 *     t.name,
	 *     t.password,
	 *     t.gender,
	 *     t.age,
	 *     t.email,
	 *     t.create_time,
	 *     t.update_time
	 * FROM
	 *     courses c
	 * INNER JOIN
	 *     teachers t ON c.teacher_id = t.teacher_id
	 * WHERE
	 *     c.semester = ?;
	 */
	@Query("SELECT c FROM Course c JOIN FETCH c.teacher WHERE c.semester = :semester")
	List<Course> findBySemesterWithTeacher(@Param("semester") String semester);

	@Query("SELECT c.courseId, c.courseName, c.status, c.credit, c.semester, c.classroom, COUNT(sc.student) " +
			"FROM Course c " +
			"LEFT JOIN c.studentCourses sc " +
			"WHERE c.teacher.teacherId = :teacherId " +
			"GROUP BY c.courseId, c.courseName, c.status, c.credit, c.semester, c.classroom")
	List<Object[]> findCoursesWithStudentCount(@Param("teacherId") String teacherId);

//	@Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.courseId = :courseId AND c.teacher.teacherId = :teacherId")
//	boolean existsByCourseIdAndTeacher(@Param("courseId") String courseId,
//									   @Param("teacherId") String teacherId);

	@Query("SELECT c FROM Course c WHERE c.courseId = ?1 AND c.teacher.teacherId = ?2")
	Course findByCourseIdAndTeacher_TeacherId(String courseId, String teacherId);

	@Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.courseId = ?1 AND c.teacher.teacherId = ?2")
	boolean existsByCourseIdAndTeacher_TeacherId(String courseId, String teacherId);

	@Query("SELECT c FROM Course c JOIN FETCH c.teacher")
	List<Course> findAllWithTeacher();

	@Query("SELECT new cn.edu.jlu.dto.AdminCourseDTO( " +
			"c.courseId, c.courseName, c.status, t.name, t.email, " +
			"c.credit, c.semester, c.classroom, " +
			"COUNT(sc.studentId), " +
			"SUM(CASE WHEN sc.grade IS NULL THEN 1 ELSE 0 END) = 0) " +
			"FROM Course c " +
			"JOIN c.teacher t " +
			"LEFT JOIN c.studentCourses sc " +
			"GROUP BY c.courseId, c.courseName, c.status, t.name, t.email, " +
			"c.credit, c.semester, c.classroom")
	List<AdminCourseDTO> findAdminCourseData();
}
