package cn.edu.jlu.controller;

import cn.edu.jlu.dto.TeacherDTO;
import cn.edu.jlu.dto.TeacherUpdateForm;
import cn.edu.jlu.entity.Teacher;
import cn.edu.jlu.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
	private final TeacherService teacherService;

	public TeacherController(TeacherService teacherService) {
		this.teacherService = teacherService;
	}

	// 显示登录页面
	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("teacherDTO", new TeacherDTO());
		return "teacher/login";
	}

	// 处理登录提交
	@PostMapping("/login")
	public String loginSubmit(
			@ModelAttribute("teacherDTO") TeacherDTO teacherDTO,
			Model model,
			HttpSession session
	) {
		Teacher teacher = teacherService.validateLogin(teacherDTO);
		if (teacher == null) {
			model.addAttribute("error", "工号或密码错误");
			return "teacher/login";
		}
		session.setAttribute("teacher", teacher); // 存储会话信息
		return "redirect:/teacher/dashboard";     // 跳转到教师主页
	}

	// 显示教师主页（补充数据绑定）
	@GetMapping("/dashboard")
	public String showDashboard(Model model, HttpSession session) {
		Teacher teacher = (Teacher) session.getAttribute("teacher");
		if (teacher == null) {
			return "redirect:/teacher/login";
		}
		model.addAttribute("teacher", teacher);

		// 初始化表单对象
		if (!model.containsAttribute("updateForm")) {
			TeacherUpdateForm updateForm = new TeacherUpdateForm();
			updateForm.setAge(teacher.getAge());
			updateForm.setGender(teacher.getGender());
			updateForm.setEmail(teacher.getEmail());
			model.addAttribute("updateForm", updateForm);
		}
		return "teacher/dashboard";
	}

	// 处理信息更新
	@PostMapping("/updateInfo")
	public String updateInfo(
			@Valid @ModelAttribute("updateForm") TeacherUpdateForm updateForm,
			BindingResult bindingResult,
			HttpSession session,
			Model model
	) {
		Teacher sessionTeacher = (Teacher) session.getAttribute("teacher");
		if (sessionTeacher == null) {
			return "redirect:/teacher/login";
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("teacher", sessionTeacher);
			return "teacher/dashboard";
		}

		// 更新字段（仅允许修改指定字段）
		if (updateForm.getAge() != null) {
			sessionTeacher.setAge(updateForm.getAge());
		}
		if (updateForm.getGender() != null && !updateForm.getGender().isEmpty()) {
			sessionTeacher.setGender(updateForm.getGender());
		}
		if (updateForm.getEmail() != null && !updateForm.getEmail().isEmpty()) {
			sessionTeacher.setEmail(updateForm.getEmail());
		}
		if (updateForm.getNewPassword() != null && !updateForm.getNewPassword().isEmpty()) {
			sessionTeacher.setPassword(updateForm.getNewPassword());
		}

		// 调用服务层保存
		Teacher updatedTeacher = teacherService.updateTeacherInfo(sessionTeacher);
		if (updatedTeacher == null) {
			model.addAttribute("updateError", "信息更新失败");
			return "teacher/dashboard";
		}

		session.setAttribute("teacher", updatedTeacher);
		model.addAttribute("updateSuccess", "信息已成功更新！");
		return "redirect:/teacher/dashboard";
	}
}