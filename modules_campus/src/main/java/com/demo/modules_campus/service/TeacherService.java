package com.demo.modules_campus.service;

import com.demo.modules_campus.pojo.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【tb_teacher】的数据库操作Service
* @createDate 2023-03-19 15:38:08
*/
public interface TeacherService extends IService<Teacher> {

  Teacher selectAdminByUsernameAndPassword(String username, String password);

  Teacher selectTeacherById(Long userId);
}
