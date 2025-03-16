package cn.edu.jlu.service.impl;

import cn.edu.jlu.dto.StudentDTO;
import cn.edu.jlu.entity.Student;
import cn.edu.jlu.repository.StudentRepository;
import cn.edu.jlu.service.StudentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service  // 关键注解：标记为 Spring 管理的 Bean
public class StudentServiceImpl implements StudentService {
	private final StudentRepository studentRepository;

	// 构造函数注入依赖
	public StudentServiceImpl(StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@Override
	public Student validateLogin(StudentDTO studentDTO) {
		Student student = studentRepository.findByStudentId(studentDTO.getStudentId());
		if (student == null || !student.getPassword().equals(studentDTO.getPassword())) {
			return null;
		}
		return student;
	}

	@Override
	public Student updateStudentInfo(Student studentFromSession) {
		if (studentFromSession == null) return null;

		// 从数据库加载托管实体
		Student managedStudent = studentRepository.findById(studentFromSession.getStudentId()).orElse(null);
		if (managedStudent == null) return null;

		// 仅更新有变化的字段（避免 Hibernate 认为无变化而跳过更新）
		if (studentFromSession.getAge() != null) {
			managedStudent.setAge(studentFromSession.getAge());
		}
		if (studentFromSession.getGrade() != null) {
			managedStudent.setGrade(studentFromSession.getGrade());
		}
		if (studentFromSession.getMajor() != null) {
			managedStudent.setMajor(studentFromSession.getMajor());
		}
		if (studentFromSession.getPassword() != null) {
			managedStudent.setPassword(studentFromSession.getPassword());
		}

		// 显式触发更新（即使字段值未变）
		managedStudent.setUpdateTime(LocalDateTime.now()); // 临时手动设置，验证问题

		return studentRepository.save(managedStudent);
	}
}
