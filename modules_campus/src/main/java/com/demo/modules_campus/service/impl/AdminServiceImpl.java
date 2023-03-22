package com.demo.modules_campus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.modules_campus.pojo.Admin;
import com.demo.modules_campus.service.AdminService;
import com.demo.modules_campus.mapper.AdminMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author admin
* @description 针对表【tb_admin】的数据库操作Service实现
* @createDate 2023-03-19 15:36:09
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

  @Resource
  private AdminMapper adminMapper;

  @Override
  public Admin selectAdminByUsernameAndPassword(String username, String password) {
    return adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getName, username).eq(Admin::getPassword, password));
  }

  @Override
  public Admin selectAdminById(Long userId) {
    return adminMapper.selectOne(new LambdaQueryWrapper<Admin>().eq(Admin::getId, userId));
  }
}




