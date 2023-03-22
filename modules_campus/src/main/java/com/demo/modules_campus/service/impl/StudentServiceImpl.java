package com.demo.modules_campus.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.modules_campus.pojo.Student;
import com.demo.modules_campus.service.StudentService;
import com.demo.modules_campus.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author admin
* @description 针对表【tb_student】的数据库操作Service实现
* @createDate 2023-03-19 15:38:05
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

  @Resource
  private StudentMapper studentMapper;

  @Override
  public Student selectAdminByUsernameAndPassword(String username, String password) {
    return studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getName, username).eq(Student::getPassword, password));
  }

  @Override
  public Student selectStudentById(Long userId) {
    return studentMapper.selectOne(new LambdaQueryWrapper<Student>().eq(Student::getId, userId));
  }
}





