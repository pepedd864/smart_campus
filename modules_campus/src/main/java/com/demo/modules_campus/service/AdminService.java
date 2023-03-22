package com.demo.modules_campus.service;

import com.demo.modules_campus.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author admin
* @description 针对表【tb_admin】的数据库操作Service
* @createDate 2023-03-19 15:36:09
*/
public interface AdminService extends IService<Admin> {

  Admin selectAdminByUsernameAndPassword(String username, String password);

  Admin selectAdminById(Long userId);
}
