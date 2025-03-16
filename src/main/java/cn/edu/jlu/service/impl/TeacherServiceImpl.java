package cn.edu.jlu.service.impl;

import cn.edu.jlu.dto.TeacherDTO;
import cn.edu.jlu.entity.Teacher;
import cn.edu.jlu.repository.TeacherRepository;
import cn.edu.jlu.service.TeacherService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TeacherServiceImpl implements TeacherService {
	private final TeacherRepository teacherRepository;

	public TeacherServiceImpl(TeacherRepository teacherRepository) {
		this.teacherRepository = teacherRepository;
	}

	@Override
	public Teacher validateLogin(TeacherDTO teacherDTO) {
		Teacher teacher = teacherRepository.findByTeacherId(teacherDTO.getTeacherId());
		if (teacher == null || !teacher.getPassword().equals(teacherDTO.getPassword())) {
			return null;
		}
		return teacher;
	}

	@Override
	public Teacher updateTeacherInfo(Teacher teacherFromSession) {
		if (teacherFromSession == null) return null;

		Teacher managedTeacher = teacherRepository.findById(teacherFromSession.getTeacherId()).orElse(null);
		if (managedTeacher == null) return null;

		// 更新可修改字段
		if (teacherFromSession.getAge() != null) {
			managedTeacher.setAge(teacherFromSession.getAge());
		}
		if (teacherFromSession.getGender() != null) {
			managedTeacher.setGender(teacherFromSession.getGender());
		}
		if (teacherFromSession.getEmail() != null) {
			managedTeacher.setEmail(teacherFromSession.getEmail());
		}
		if (teacherFromSession.getPassword() != null) {
			managedTeacher.setPassword(teacherFromSession.getPassword());
		}

		// 显式触发更新（即使字段值未变）
		managedTeacher.setUpdateTime(LocalDateTime.now()); // 临时手动设置，验证问题

		return teacherRepository.save(managedTeacher);
	}
}
