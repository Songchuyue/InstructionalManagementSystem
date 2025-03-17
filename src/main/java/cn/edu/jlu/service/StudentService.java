package cn.edu.jlu.service;

import cn.edu.jlu.dto.StudentDTO;
import cn.edu.jlu.entity.Student;

public interface StudentService {
	/**
	 * 验证学生登录
	 * @param studentDTO 登录信息（学号、密码）
	 * @return 成功返回 Student 对象，失败返回 null
	 */
	Student validateLogin(StudentDTO studentDTO);

	Student updateStudentInfo(Student student);

	void enrollStudentInCourse(String studentId, String courseId) throws Exception;

	void dropCourse(String studentId, String courseId) throws Exception;
}
