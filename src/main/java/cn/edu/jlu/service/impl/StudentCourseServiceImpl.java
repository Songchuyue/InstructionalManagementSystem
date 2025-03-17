package cn.edu.jlu.service.impl;

import cn.edu.jlu.dto.GradeManagementDTO;
import cn.edu.jlu.dto.GradeUpdateForm;
import cn.edu.jlu.entity.StudentCourse;
import cn.edu.jlu.repository.StudentCourseRepository;
import cn.edu.jlu.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

	private final StudentCourseRepository studentCourseRepository;

	@Autowired
	public StudentCourseServiceImpl(StudentCourseRepository studentCourseRepository) {
		this.studentCourseRepository = studentCourseRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public List<GradeManagementDTO> getStudentsWithGrades(String courseId) {
		return studentCourseRepository.findByCourse_CourseId(courseId)
				.stream()
				.map(sc -> new GradeManagementDTO(
						sc.getStudent().getStudentId(),
						sc.getStudent().getName(),
						sc.getGrade()
				))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void batchUpdateGrades(GradeUpdateForm form) throws Exception {
		for (GradeUpdateForm.GradeEntry entry : form.getGrades()) {
			StudentCourse sc = studentCourseRepository.findByStudent_StudentIdAndCourse_CourseId(
					entry.getStudentId(), form.getCourseId()
			);
			if (sc == null) {
				throw new Exception("找不到学生选课记录: " + entry.getStudentId());
			}
			sc.setGrade(entry.getNewGrade());
			studentCourseRepository.save(sc);
		}
	}
}
