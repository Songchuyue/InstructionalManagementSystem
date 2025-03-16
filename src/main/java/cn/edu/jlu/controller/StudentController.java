package cn.edu.jlu.controller;

import cn.edu.jlu.dto.StudentDTO;
import cn.edu.jlu.dto.StudentUpdateForm;
import cn.edu.jlu.entity.Student;
import cn.edu.jlu.service.StudentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/student")
public class StudentController {
	private final StudentService studentService;

	public StudentController(StudentService studentService) {
		this.studentService = studentService;
	}

	// 显示登录页面
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("studentDTO", new StudentDTO());
		return "student/login";
	}

	// 处理登录提交
	@PostMapping("/login")
	public String loginSubmit(
			@ModelAttribute("studentDTO") StudentDTO studentDTO,
			Model model,
			HttpSession session
	) {
		Student student = studentService.validateLogin(studentDTO);
		if (student == null) {
			model.addAttribute("error", "学号或密码错误");
			return "student/login";
		}
		session.setAttribute("student", student); // 存储会话信息
		return "redirect:/student/dashboard";     // 跳转到学生主页
	}

	// 显示学生主页
	@GetMapping("/dashboard")
	public String showDashboard(Model model, HttpSession session) {
		Student student = (Student) session.getAttribute("student");
		if (student == null) {
			return "redirect:/student/login";
		}
		model.addAttribute("student", student);

		// 初始化表单对象（用当前学生数据填充）
		if (!model.containsAttribute("updateForm")) {
			StudentUpdateForm updateForm = new StudentUpdateForm();
			updateForm.setAge(student.getAge());      // 初始化年龄
			updateForm.setGrade(student.getGrade());  // 初始化年级
			updateForm.setMajor(student.getMajor());  // 初始化专业
			model.addAttribute("updateForm", updateForm);
		}
		return "student/dashboard";
	}

	@PostMapping("/updateInfo")
	public String updateInfo(
			@Valid @ModelAttribute("updateForm") StudentUpdateForm updateForm, // 绑定表单对象
			BindingResult bindingResult, // 必须紧跟 @Valid 参数
			HttpSession session,
			Model model
	) {
		Student sessionStudent = (Student) session.getAttribute("student");
		if (sessionStudent == null) {
			return "redirect:/student/login";
		}

		// 校验失败处理
		if (bindingResult.hasErrors()) {
			model.addAttribute("student", sessionStudent);
			return "student/dashboard";
		}

		// 更新字段
		if (updateForm.getAge() != null) { // 用户填写了年龄
			sessionStudent.setAge(updateForm.getAge());
		}
		if (updateForm.getGrade() != null && !updateForm.getGrade().isEmpty()) { // 用户填写了年级
			sessionStudent.setGrade(updateForm.getGrade());
		}
		if (updateForm.getMajor() != null && !updateForm.getMajor().isEmpty()) { // 用户填写了专业
			sessionStudent.setMajor(updateForm.getMajor());
		}

		if (updateForm.getNewPassword() != null && !updateForm.getNewPassword().isEmpty()) {
			sessionStudent.setPassword(updateForm.getNewPassword());
		}

		// 保存到数据库
		Student updatedStudent = studentService.updateStudentInfo(sessionStudent);
		if (updatedStudent == null) {
			model.addAttribute("updateError", "信息更新失败");
			return "student/dashboard";
		}

		// 更新 Session 数据
		session.setAttribute("student", updatedStudent);
		model.addAttribute("updateSuccess", "信息修改成功！");
		return "redirect:/student/dashboard";
	}
}
