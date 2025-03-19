package cn.edu.jlu.service.impl;

import cn.edu.jlu.dto.StudentDTO;
import cn.edu.jlu.dto.StudentUpdateForm;
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
//		Student student = studentRepository.findByStudentId(studentDTO.getStudentId());
		Student student = studentRepository.findById(studentDTO.getStudentId()).orElse(null);
		if (student == null || !student.getPassword().equals(studentDTO.getPassword())) {
			return null;
		}
		return student;
	}

	@Override
	@Transactional
	public Student updateStudentInfo(String studentId, StudentUpdateForm studentUpdateForm) {
		if (studentUpdateForm == null) return null;

		/* findById返回<Optional>Student, orElse(null)表示若查询到结果则返回Student对象, 否则返回null
		 * 从数据库加载托管实体(必须通过findById或类似方法从数据库加载), 其修改会自动同步到数据库
		 */
		Student managedStudent = studentRepository.findById(studentId).orElse(null);
		if (managedStudent == null) return null;

		managedStudent.setAge(studentUpdateForm.getAge());
		managedStudent.setSemester(studentUpdateForm.getSemester());
		managedStudent.setMajor(studentUpdateForm.getMajor());

		if (studentUpdateForm.getNewPassword() != null && !studentUpdateForm.getNewPassword().isEmpty()) {
			managedStudent.setPassword(studentUpdateForm.getNewPassword());
		}

		managedStudent.setUpdateTime(LocalDateTime.now());

//		return studentRepository.save(managedStudent);
		return managedStudent;
	}

	@Override
	@Transactional
	public void enrollStudentInCourse(String studentId, String courseId) throws Exception {
		// 其实和==null和多大区别, 除了检查空字符串, 还判断一下纯空格
		if (StringUtils.isBlank(studentId) || StringUtils.isBlank(courseId)) {
			throw new IllegalArgumentException("学生ID和课程ID不能为空");
		}

		/*
		 * 虽然前端已经对选课有了限制, 只有课程存在才提供选课按钮, 但是后端须始终认为"前端不可信"
		 * SELECT * FROM students WHERE student_id = ?
		 */
		Student student = studentRepository.findById(studentId)
				.orElseThrow(() -> new Exception("学生不存在，ID：" + studentId));

		// SELECT * FROM courses WHERE course_id = ?
		Course course = courseRepository.findById(courseId)
				.orElseThrow(() -> new Exception("课程不存在，ID：" + courseId));

		//SELECT COUNT(*) > 0 FROM student_courses WHERE student_id = ? AND course_id = ?
		if (studentCourseRepository.existsByStudent_StudentIdAndCourse_CourseId(studentId, courseId)) {
			throw new Exception("重复选课：学生[" + studentId + "] 课程[" + courseId + "]");
		}

		// 创建选课记录
		StudentCourse studentCourse = new StudentCourse();
		studentCourse.setStudentId(studentId);
		studentCourse.setCourseId(courseId);
		studentCourse.setStudent(student);
		studentCourse.setCourse(course);
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
