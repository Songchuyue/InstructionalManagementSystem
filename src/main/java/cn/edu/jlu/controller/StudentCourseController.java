package cn.edu.jlu.controller;

import cn.edu.jlu.dto.GradeUpdateForm;
import cn.edu.jlu.entity.Course;
import cn.edu.jlu.entity.Teacher;
import cn.edu.jlu.service.CourseService;
import cn.edu.jlu.service.StudentCourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/course")
public class StudentCourseController {

	private final StudentCourseService studentCourseService;
	private final CourseService courseService;

	public StudentCourseController(StudentCourseService studentCourseService,
								   CourseService courseService) {
		this.studentCourseService = studentCourseService;
		this.courseService = courseService;
	}

	@GetMapping("/grade-management")
	public String showGradeManagement(
			@RequestParam String courseId,
			HttpSession session,
			Model model
	) {
		Teacher teacher = (Teacher) session.getAttribute("teacher");
		if (teacher == null) return "redirect:/teacher/login";

		// 验证教师是否教授该课程
		Course course = courseService.validateTeacherCourse(teacher.getTeacherId(), courseId);
		if (course == null) return "redirect:/teacher/teaching-courses";

		model.addAttribute("students", studentCourseService.getStudentsWithGrades(courseId));
		model.addAttribute("course", course);
		return "teacher/grade-management";
	}

	@PostMapping("/update-grades")
	public String updateGrades(
			@ModelAttribute GradeUpdateForm form,
			HttpSession session,
			RedirectAttributes redirectAttributes
	) {
		Teacher teacher = (Teacher) session.getAttribute("teacher");
		if (teacher == null) return "redirect:/teacher/login";

		try {
			// 二次验证课程权限
			if (!courseService.existsByCourseIdAndTeacher(form.getCourseId(), teacher.getTeacherId())) {
				throw new Exception("无权限操作该课程");
			}

			studentCourseService.batchUpdateGrades(form);
			redirectAttributes.addFlashAttribute("success", "成绩更新成功");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "更新失败: " + e.getMessage());
		}
		return "redirect:/teacher/grade-management?courseId=" + form.getCourseId();
	}
}
