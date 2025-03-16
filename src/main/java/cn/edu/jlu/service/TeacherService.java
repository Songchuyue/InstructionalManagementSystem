package cn.edu.jlu.service;

import cn.edu.jlu.dto.TeacherDTO;
import cn.edu.jlu.entity.Teacher;

public interface TeacherService {
	/**
	 * 验证教师登录
	 * @param teacherDTO 登录信息（工号、密码）
	 * @return 成功返回 Teacher 对象，失败返回 null
	 */
	Teacher validateLogin(TeacherDTO teacherDTO);

	Teacher updateTeacherInfo(Teacher teacher);
}
