package com.demo.modules_campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.modules_campus.pojo.Student;
import com.demo.modules_campus.pojo.Teacher;
import com.demo.modules_campus.service.TeacherService;
import com.demo.modules_campus.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author admin
* @description 针对表【tb_teacher】的数据库操作Service实现
* @createDate 2023-03-19 15:38:08
*/
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService{

  @Resource
  private TeacherMapper teacherMapper;

  @Override
  public Teacher selectAdminByUsernameAndPassword(String username, String password) {
    return teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getName, username).eq(Teacher::getPassword, password));
  }

  @Override
  public Teacher selectTeacherById(Long userId) {
    return teacherMapper.selectOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getId, userId));
  }
}




