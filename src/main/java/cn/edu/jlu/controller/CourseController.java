package cn.edu.jlu.controller;

import cn.edu.jlu.dto.CourseDTO;
import cn.edu.jlu.entity.Course;
import cn.edu.jlu.entity.Teacher;
import cn.edu.jlu.service.CourseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
	private final CourseService courseService;

	public CourseController(CourseService courseService) {
		this.courseService = courseService;
	}

	@GetMapping
	public List<CourseDTO> getTeachingCourses(HttpSession session) {
		Teacher teacher = (Teacher) session.getAttribute("teacher");
		if (teacher == null) return Collections.emptyList();
		return courseService.findByTeacherId(teacher.getTeacherId())
				.stream()
				.map(CourseDTO::from)
				.toList();
	}
}
