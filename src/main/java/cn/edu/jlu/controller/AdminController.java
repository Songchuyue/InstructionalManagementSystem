package cn.edu.jlu.controller;

import cn.edu.jlu.dto.AdminCourseDTO;
import cn.edu.jlu.dto.AdminDTO;
import cn.edu.jlu.entity.Admin;
import cn.edu.jlu.entity.Course;
import cn.edu.jlu.entity.Teacher;
import cn.edu.jlu.repository.CourseRepository;
import cn.edu.jlu.repository.TeacherRepository;
import cn.edu.jlu.service.AdminService;
import cn.edu.jlu.service.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
	private final AdminService adminService;
	private final CourseService courseService;
	private final CourseRepository courseRepository;
	private final TeacherRepository teacherRepository;

	public AdminController(AdminService adminService, CourseService courseService, CourseRepository courseRepository, TeacherRepository teacherRepository) {
		this.adminService = adminService;
		this.courseService = courseService;
		this.courseRepository = courseRepository;
		this.teacherRepository = teacherRepository;
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("adminDTO", new AdminDTO());
		return "admin/login";
	}

	@PostMapping("/login")
	public String loginSubmit(
			@ModelAttribute("adminDTO") AdminDTO adminDTO,
			Model model,
			HttpSession session
	) {
		Admin admin = adminService.validateLogin(adminDTO);
		if (admin == null) {
			model.addAttribute("error", "管理员ID或密码错误");
			return "admin/login";
		}
		session.setAttribute("admin", admin); // 存储管理员对象到会话
		return "redirect:/admin/dashboard";   // 跳转到管理主页
	}

	@GetMapping("/dashboard")
	public String showDashboard(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) return "redirect:/admin/login";

		model.addAttribute("admin", admin); // 传递管理员信息到页面
		return "admin/dashboard";
	}

	@PostMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("admin");
		return "redirect:/admin/login";
	}

	@GetMapping("/courses")
	public String showAllCourses(HttpSession session, Model model) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) return "redirect:/admin/login";

		List<AdminCourseDTO> courses = courseService.findAdminCourseData();
		model.addAttribute("courses", courses);
		return "admin/courses";
	}

	@PostMapping("/close-course")
	public String closeCourse(
			@RequestParam String courseId,
			RedirectAttributes redirectAttributes
	) {
		try {
			// 添加校验逻辑
			courseService.validateCourseClosable(courseId);
			courseService.closeCourse(courseId);
			redirectAttributes.addFlashAttribute("success", "课程已结课");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "操作失败: " + e.getMessage());
		}
		return "redirect:/admin/courses";
	}

	@GetMapping("/courses/add")
	public String showAddCourseForm(HttpSession session) {
		Admin admin = (Admin) session.getAttribute("admin");
		if (admin == null) return "redirect:/admin/login";
		return "admin/add-course";
	}

	// 处理添加课程提交
	@PostMapping("/courses/add")
	public String addCourse(
			@RequestParam String courseId,
			@RequestParam String courseName,
			@RequestParam String teacherId,
			@RequestParam Integer credit,
			@RequestParam String semester,
			@RequestParam String classroom,
			RedirectAttributes redirectAttributes
	) {
		try {
			// 检查教师是否存在
			if (!teacherRepository.existsById(teacherId)) {
				throw new IllegalArgumentException("教师ID不存在");
			}

			// 创建课程对象
			Course course = new Course();
			course.setCourseId(courseId);
			course.setCourseName(courseName);
			course.setTeacher(new Teacher(teacherId)); // 通过ID关联教师
			course.setCredit(credit);
			course.setSemester(semester);
			course.setClassroom(classroom);
			course.setStatus(0); // 默认未结课

			courseRepository.save(course);
			redirectAttributes.addFlashAttribute("success", "课程添加成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "添加失败: " + e.getMessage());
		}
		return "redirect:/admin/courses";
	}
}
