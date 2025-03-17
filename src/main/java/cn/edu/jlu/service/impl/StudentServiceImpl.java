package cn.edu.jlu.service.impl;

import cn.edu.jlu.dto.StudentDTO;
import cn.edu.jlu.entity.Course;
import cn.edu.jlu.entity.Student;
import cn.edu.jlu.entity.StudentCourse;
import cn.edu.jlu.repository.CourseRepository;
import cn.edu.jlu.repository.StudentCourseRepository;
import cn.edu.jlu.repository.StudentRepository;
import cn.edu.jlu.service.StudentService;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service  // 关键注解：标记为 Spring 管理的 Bean
public class StudentServiceImpl implements StudentService {
	private final StudentRepository studentRepository;
	private final StudentCourseRepository studentCourseRepository;
	private final CourseRepository courseRepository;

	public StudentServiceImpl(
			StudentRepository studentRepository,
			StudentCourseRepository studentCourseRepository,
			CourseRepository courseRepository
	) {
		this.studentRepository = studentRepository;
		this.studentCourseRepository = studentCourseRepository;
		this.courseRepository = courseRepository;
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
		if (studentFromSession.getSemester() != null) {
			managedStudent.setSemester(studentFromSession.getSemester());
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

	@Override
	@Transactional
	public void enrollStudentInCourse(String studentId, String courseId) throws Exception {
		// 加强参数校验（新增）
		if (StringUtils.isBlank(studentId) || StringUtils.isBlank(courseId)) {
			throw new IllegalArgumentException("学生ID和课程ID不能为空");
		}

		// 获取课程实体（新增异常信息细化）
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("课程不存在，ID：" + courseId));

		if (studentCourseRepository.existsByStudent_StudentIdAndCourse_CourseId(studentId, courseId)) {
			throw new Exception("重复选课：学生[" + studentId + "] 课程[" + courseId + "]");
		}

		// 获取学生实体（新增异常信息细化）
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new Exception("学生不存在，ID：" + studentId));

		// 创建选课记录（关键修改）
		StudentCourse studentCourse = new StudentCourse();
		studentCourse.setStudentId(studentId);  // 显式设置主键字段
		studentCourse.setCourseId(courseId);     // 显式设置主键字段
		studentCourse.setStudent(student);      // 保持关联关系
		studentCourse.setCourse(course);        // 保持关联关系
		studentCourse.setGrade(null);

		studentCourseRepository.save(studentCourse);
	}

	@Override
	@Transactional
	public void dropCourse(String studentId, String courseId) throws Exception {
		// 检查是否存在选课记录
		if (!studentCourseRepository.existsByStudent_StudentIdAndCourse_CourseId(studentId, courseId)) {
			throw new Exception("未找到该选课记录");
		}

		// 直接通过复合主键删除
		studentCourseRepository.deleteById(
				new StudentCourse.StudentCourseId(studentId, courseId)
		);
	}
}
