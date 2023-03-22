package com.demo.modules_campus.service;

import com.demo.modules_campus.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【tb_student】的数据库操作Service
* @createDate 2023-03-19 15:38:05
*/
public interface StudentService extends IService<Student> {

  Student selectAdminByUsernameAndPassword(String username, String password);

  Student selectStudentById(Long userId);
}
