package cn.edu.jlu.controller;

import cn.edu.jlu.dto.StudentDTO;
import cn.edu.jlu.dto.StudentUpdateForm;
import cn.edu.jlu.entity.Course;
import cn.edu.jlu.entity.Student;
import cn.edu.jlu.entity.StudentCourse;
import cn.edu.jlu.service.CourseService;
import cn.edu.jlu.service.StudentCourseService;
import cn.edu.jlu.service.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
public class StudentController {
	private final StudentService studentService;
	private final CourseService courseService;
	private final StudentCourseService studentCourseService;

	public StudentController(
			StudentService studentService,
			CourseService courseService,
			StudentCourseService studentCourseService
	) {
		this.studentService = studentService;
		this.courseService = courseService;
		this.studentCourseService = studentCourseService;
	}

	// 显示登录页面
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		// 前端通过key(即attributeName:"studentDTO"), 与后端对象绑定
		model.addAttribute("studentDTO", new StudentDTO());
		return "student/login";
	}

	// 处理登录提交
	@PostMapping("/login")
	public String loginSubmit(
			@Valid @ModelAttribute("studentDTO") StudentDTO studentDTO,
			BindingResult bindingResult,
			Model model,
			HttpSession session
	) {
		if(bindingResult.hasErrors()) {
//			model.addAttribute("loginError", "学号或密码不能为空");
			return "student/login";
		}

		// controller层不关心如何查询学生实体, 只需要接受结果(Student对象 或 null)
		Student student = studentService.validateLogin(studentDTO);
		if (student == null) {
			model.addAttribute("loginError", "学号或密码错误");
			return "student/login";
		}

		session.setAttribute("student", student);
		return "redirect:/student/dashboard";
	}

	// 显示学生主页
	@GetMapping("/dashboard")
	public String showDashboard(Model model, HttpSession session) {
		// 用session存放持久数据(如:登录后的用户信息), 用model存放临时数据(如:错误提示)
		Student student = (Student) session.getAttribute("student");
		if (student == null) {
			return "redirect:/student/login";
		}

		// student显示个人信息
		model.addAttribute("student", student);

		/* Q: 为什么需要model.containsAttribute("updateForm")判断
		 * A: 当用户提交表单失败(如校验错误)后, 会重定向到"/dashboard", 因此需要保留已填写的数据, 避免覆盖用户输入
		 * Q: 为什么要对updateForm赋值?
		 * A: 用户在填写表单时无需再覆写已经显示在[个人信息]上的属性
		 */
		if (!model.containsAttribute("updateForm")) {
			StudentUpdateForm updateForm = new StudentUpdateForm();
			updateForm.setAge(student.getAge());
			updateForm.setSemester(student.getSemester());
			updateForm.setMajor(student.getMajor());
			model.addAttribute("updateForm", updateForm);
		}
		return "student/dashboard";
	}

	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("student"); // 清除会话
		return "redirect:/student/login";
	}

	@PostMapping("/updateInfo")
	public String updateInfo(
			@Valid @ModelAttribute("updateForm") StudentUpdateForm studentUpdateForm,
			BindingResult bindingResult,
			HttpSession session,
			RedirectAttributes redirectAttributes
	) {
		Student sessionStudent = (Student) session.getAttribute("student");
		if(sessionStudent == null) {
			return "redirect:/student/login";
		}

		if(bindingResult.hasErrors()) {
			return "redirect:/student/dashboard";
		}

		String studentId = sessionStudent.getStudentId();
		Student updatedStudent = studentService.updateStudentInfo(studentId, studentUpdateForm);

		if(updatedStudent == null) {
			redirectAttributes.addFlashAttribute("updateError", "信息修改失败");
		} else {
			redirectAttributes.addFlashAttribute("updateSuccess", "信息修改成功");
			session.setAttribute("student", updatedStudent);
		}
		return "redirect:/student/dashboard";
	}

	/*
	 *课程页面应该展示什么呢?
	 * 1, 展示所有符合自己年级的课程(课程名, 授课老师, 教师, 成绩...)
	 * 2, 如果有自己选的课, 那么成绩栏应该为"分数/未公布", 否则显示"-"
	 * 3,  应该有选课和退课按钮, 选课之后可以退课
	 * 4, 什么时候不能退课? 出成绩之后不能退
	 * 5, 对于管理员而言, 如果一门课的成绩都出了, 那么就可以选择结课了, 需要显示结课
	 */
	@GetMapping("/courses")
	public String showCourses(HttpSession session, Model model) {
		Student student = (Student) session.getAttribute("student");
		if (student == null) return "redirect:/student/login";

		// 获取符合年级的课程
		List<Course> courses = courseService.findBySemester(student.getSemester());

		// 获取已选课程及成绩（包含课程信息的预加载）
		List<StudentCourse> enrolledCourses = studentCourseService.getEnrolledCoursesWithDetails(student.getStudentId());

		// 创建课程ID到成绩的快速查找Map
		Map<String, BigDecimal> gradeMap = enrolledCourses.stream()
				.filter(sc -> sc.getCourse() != null)
				.filter(sc -> sc.getCourse().getCourseId() != null)
				.collect(Collectors.toMap(
						sc -> sc.getCourse().getCourseId(),
						sc -> sc.getGrade() != null ? sc.getGrade() : BigDecimal.ZERO,
						(existing, replacement) -> existing
				));

		// 新增：提取已选课程ID列表
		List<String> enrolledCourseIds = enrolledCourses.stream()
				.filter(sc -> sc.getCourse() != null)
				.map(sc -> sc.getCourse().getCourseId())
				.filter(Objects::nonNull)
				.collect(Collectors.toList());

		model.addAttribute("courses", courses);
		model.addAttribute("gradeMap", gradeMap);
		model.addAttribute("enrolledCourseIds", enrolledCourseIds); // 新增关键行
		return "student/courses";
	}

	@PostMapping("/enroll")
	public String enrollCourse(
			@RequestParam String courseId,
			HttpSession session,
			RedirectAttributes redirectAttributes
	) {
		Student student = (Student) session.getAttribute("student");
		if (student == null) return "redirect:/student/login";

		try {
			studentService.enrollStudentInCourse(student.getStudentId(), courseId);
			redirectAttributes.addFlashAttribute("success", "选课成功！");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "选课失败：" + e.getMessage());
		}
		return "redirect:/student/courses";
	}

	@PostMapping("/drop")
	public String dropCourse(
			@RequestParam String courseId,
			HttpSession session,
			RedirectAttributes redirectAttributes
	) {
		Student student = (Student) session.getAttribute("student");
		if (student == null) return "redirect:/student/login";

		try {
			studentService.dropCourse(student.getStudentId(), courseId);
			redirectAttributes.addFlashAttribute("success", "退课成功！");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "退课失败：" + e.getMessage());
		}
		return "redirect:/student/courses";
	}
}
