package cn.edu.jlu.repository;

import cn.edu.jlu.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
	// 根据工号查询教师
	Teacher findByTeacherId(String teacherId);
}
