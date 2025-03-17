package cn.edu.jlu.service;

import cn.edu.jlu.dto.GradeManagementDTO;
import cn.edu.jlu.dto.GradeUpdateForm;
import java.util.List;

public interface StudentCourseService {
	List<GradeManagementDTO> getStudentsWithGrades(String courseId);
	void batchUpdateGrades(GradeUpdateForm form) throws Exception;
}
