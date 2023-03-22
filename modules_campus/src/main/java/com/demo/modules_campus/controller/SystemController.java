package com.demo.modules_campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.demo.modules_campus.pojo.Admin;
import com.demo.modules_campus.pojo.LoginForm;
import com.demo.modules_campus.pojo.Student;
import com.demo.modules_campus.pojo.Teacher;
import com.demo.modules_campus.service.AdminService;
import com.demo.modules_campus.service.StudentService;
import com.demo.modules_campus.service.TeacherService;
import com.demo.modules_campus.utils.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

  @Resource
  private AdminService adminService;

  @Resource
  private StudentService studentService;

  @Resource
  private TeacherService teacherService;

  /***
   * 获取验证码图片,返回给浏览器
   * @param session session
   * @param response response
   * @throws IOException IOException
   */
  @RequestMapping("/getVerifiCodeImage")
  public void getVerifiCodeImage(HttpSession session, HttpServletResponse response) throws IOException {
    // 获取验证码图片
    BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
    // 获取验证码图片中的值
    String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
    // 将验证码中的值保存到session中 用于登录时校验
    session.setAttribute("code", verifiCode);
    // 将验证码图片响应给客户端
    ImageIO.write(verifiCodeImage, "jpg", response.getOutputStream());
  }

  /***
   * 登录 进行验证码的校验 以及用户名密码的校验
   * 登录成功时生成的token返回给客户端
   * @param loginForm 生成的登录表单
   * @param session session
   * @return return
   */
  @PostMapping("/login")
  public Result<Object> login(@RequestBody LoginForm loginForm, HttpSession session) {
    // 获取用户输入的验证码
    String code = (String) session.getAttribute("code");
    // 判断是否失效
    if (code == null || "".equals(code)) {
      return Result.fail().message("验证码失效,请重新获取");
    }
    // 获取用户输入的验证码
    String userInputCode = loginForm.getVerifiCode();
    // 判断验证码是否正确
    if (!code.equalsIgnoreCase(userInputCode)) {
      return Result.fail().message("验证码错误");
    }
    // 销毁session中的验证码
    session.removeAttribute("code");
    // 判断用户类型
    Integer userType = loginForm.getUserType();
    // 获取用户输入的用户名和密码
    String username = loginForm.getUsername();
    String password = MD5.encrypt(loginForm.getPassword());
    Map<String, Object> map = new LinkedHashMap<>();

    if (userType == 1) {
      Admin admin = adminService.selectAdminByUsernameAndPassword(username, password);
      if (admin != null) {
        // 登录成功后需要根据用户id和用户类型生成Token
        String token = JwtHelper.createToken(admin.getId().longValue(), userType);
        map.put("token", token);
        return Result.ok(map);
      }
      return Result.fail().message("用户名或密码错误");
    } else if (userType == 2) {
      Student student = studentService.selectAdminByUsernameAndPassword(username, password);
      if (student != null) {
        String token = JwtHelper.createToken(student.getId().longValue(), userType);
        map.put("token", token);
        return Result.ok(map);
      }
      return Result.fail().message("用户名或密码错误");
    } else if (userType == 3) {
      Teacher teacher = teacherService.selectAdminByUsernameAndPassword(username, password);
      if (teacher != null) {
        String token = JwtHelper.createToken(teacher.getId().longValue(), userType);
        map.put("token", token);
        return Result.ok(map);
      }
      return Result.fail().message("用户名或密码错误");
    }
    return null;
  }

  /***
   * 根据token获取用户信息
   * @param token
   * @return
   */

  @GetMapping("/getInfo")
  public Result<Object> getInfo(@RequestHeader("token") String token) {
    // 先判断token是否有效
    if (JwtHelper.isExpiration(token)) {
      return Result.build(null, ResultCodeEnum.CODE_ERROR);
    }
    // 解析token 获取用户id和用户类型 返回给浏览器
    Long userId = JwtHelper.getUserId(token);
    Integer userType = JwtHelper.getUserType(token);
    Map<String, Object> map = new LinkedHashMap<>();
    map.put("userType", userType);
    if (userType == 1) {
      Admin admin = adminService.selectAdminById(userId);
      map.put("user", admin);
    } else if (userType == 2) {
      Student student = studentService.selectStudentById(userId);
      map.put("user", student);
    } else if (userType == 3) {
      Teacher teacher = teacherService.selectTeacherById(userId);
      map.put("user", teacher);
    }
    return Result.ok(map);
  }

  /***
   * 上传图片
   * @param multipartFile 图片
   * @return 图片的相对路径
   * @throws IOException  IOException
   */
  @PostMapping("/headerImgUpload")
  public Result<Object> headerImgUpload(@RequestPart("multipartFile") MultipartFile multipartFile) throws IOException {
    // 获取文件名
    String originalFilename = multipartFile.getOriginalFilename();
    // 断言文件名不为空
    assert originalFilename != null;
    // 获得文件格式
    String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
    // 为了避免文件名重复,使用uuid作为文件名
    String fileName = UUID.randomUUID() + suffix;
    // 保存路径
    String filePath = "C:/Users/admin/Desktop/workspace/IDEA-Workplace/campus/modules_campus/src/main/resources/static/static/upload/" + fileName;
    // 保存文件
    multipartFile.transferTo(new File(filePath));
    return Result.ok("/upload/".concat(fileName));
  }

  /***
   * 修改密码
   * @param oldPwd  旧密码
   * @param newPwd  新密码
   * @param token   token
   * @return  return
   */

  @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
  public Result<Object> updatePwd(@PathVariable("oldPwd") String oldPwd,
                                  @PathVariable("newPwd") String newPwd,
                                  @RequestHeader("token") String token) {
    // 先判断token是否有效
    if (JwtHelper.isExpiration(token)) {
      return Result.build(null, ResultCodeEnum.CODE_ERROR);
    }
    // 先对密码进行加密
    oldPwd = MD5.encrypt(oldPwd);

    // 解析token 获取用户id和用户类型 查询用户原密码是否正确
    Long userId = JwtHelper.getUserId(token);
    Integer userType = JwtHelper.getUserType(token);
    assert userType != null;
    if (userType == 1) {
      Admin admin = adminService.selectAdminById(userId);
      if (!admin.getPassword().equals(oldPwd)) {
        return Result.fail().message("原密码错误");
      }
      // 密码正确,修改密码
      admin.setPassword(MD5.encrypt(newPwd));
      adminService.update(admin, new LambdaQueryWrapper<Admin>().eq(Admin::getId, userId));
    } else if (userType == 2) {
      Student student = studentService.selectStudentById(userId);
      if (!student.getPassword().equals(oldPwd)) {
        return Result.fail().message("原密码错误");
      }
      // 密码正确,修改密码
      student.setPassword(MD5.encrypt(newPwd));
      studentService.update(student, new LambdaQueryWrapper<Student>().eq(Student::getId, userId));
    } else {
      Teacher teacher = teacherService.selectTeacherById(userId);
      if (!teacher.getPassword().equals(oldPwd)) {
        return Result.fail().message("原密码错误");
      }
      // 密码正确,修改密码
      teacher.setPassword(MD5.encrypt(newPwd));
      teacherService.update(teacher, new LambdaQueryWrapper<Teacher>().eq(Teacher::getId, userId));
    }
    return Result.ok();
  }
}
