package cn.edu.jlu.repository;

import cn.edu.jlu.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
	// 根据学号查询学生: SELECT * FROM student WHERE student_id = ?;
	// Student findByStudentId(String studentId);
}
