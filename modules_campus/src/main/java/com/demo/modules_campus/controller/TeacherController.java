package com.demo.modules_campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.modules_campus.pojo.Clazz;
import com.demo.modules_campus.pojo.Teacher;
import com.demo.modules_campus.service.GradeService;
import com.demo.modules_campus.service.TeacherService;
import com.demo.modules_campus.utils.Result;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName TeacherController
 * @Description TODO
 * @Date 2023/3/22 8:59
 * @Created by admin
 */
@Api(tags = "教师管理")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

  @Resource
  private TeacherService teacherService;
  @Resource
  private GradeService gradeService;

  /***
   * 获取班级列表
   * @param pn  当前页码
   * @param size  每页显示的条数
   * @param teacherName 班级名称
   * @return Page 对象
   */
  @ApiOperation(value = "获取班级列表", notes = "获取班级列表")
  @RequestMapping("/getTeachers/{pn}/{size}")
  public Result<Object> getGrades(@ApiParam("当前页码") @PathVariable("pn") Integer pn,
                                  @ApiParam("页码大小") @PathVariable("size") Integer size,
                                  @ApiParam("模糊查询条件") String teacherName) {
    Page<Teacher> page = teacherService.page(new Page<>(pn, size), new LambdaQueryWrapper<Teacher>()
        .like(StrUtil.isNotBlank(teacherName), Teacher::getName, teacherName).orderByAsc(Teacher::getId));
    return Result.ok(page);
  }

  /***
   * 单条删除和批量删除
   * @param ids 请求体中待删除的班级集合
   * @return 返回结果
   */

  @DeleteMapping("/deleteTeacher")
  public Result<Object> deleteGrade(@RequestBody List<Integer> ids) {
    // 长度为1时删除单条
    if (ids.size() == 1)
      teacherService.removeById(ids.get(0));
    else
      teacherService.removeByIds(ids);
    return Result.ok();
  }

  /***
   * 添加或修改班级
   * @param teacher 请求体中的班级对象
   * @return 返回结果
   */

  @PostMapping("/saveOrUpdateTeacher")
  public Result<Object> saveOrUpdateGrade(@RequestBody Teacher teacher) {
    // 判断请求体中是否有id
    if (teacher.getId() == null)
      teacherService.save(teacher);
    else
      teacherService.updateById(teacher);

    return Result.ok();
  }

  /***
   * 获取所有教师名称
   * @return
   */

  @GetMapping("/getTeachers")
  public Result<Object> getTeachers() {
    List<Teacher> teachers = teacherService.list();
    return Result.ok(teachers);
  }

}
