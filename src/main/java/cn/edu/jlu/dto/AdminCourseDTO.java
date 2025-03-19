package cn.edu.jlu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCourseDTO {
	private String courseId;
	private String courseName;
	private Integer status;
	private String teacherName;
	private String teacherEmail;
	private Integer credit;
	private String semester;
	private String classroom;
	private Long studentCount;
	private Boolean allGraded;
}