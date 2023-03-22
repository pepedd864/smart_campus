package com.demo.modules_campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.modules_campus.pojo.Grade;
import com.demo.modules_campus.service.GradeService;
import com.demo.modules_campus.utils.Result;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "年级管理")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

  @Resource
  private GradeService gradeService;

  /***
   * 获取年级列表
   * @param pn  当前页码
   * @param size  每页显示的条数
   * @param gradeName 年级名称
   * @return Page 对象
   */
  @ApiOperation(value = "获取年级列表", notes = "获取年级列表")
  @RequestMapping("/getGrades/{pn}/{size}")
  public Result<Object> getGrades(@ApiParam("当前页码") @PathVariable("pn") Integer pn,
                                  @ApiParam("页码大小") @PathVariable("size") Integer size,
                                  @ApiParam("模糊查询条件") String gradeName) {
    Page<Grade> page = gradeService.page(new Page<>(pn, size), new LambdaQueryWrapper<Grade>()
        .like(StrUtil.isNotBlank(gradeName), Grade::getName, gradeName).orderByAsc(Grade::getId));
    return Result.ok(page);
  }

  /***
   * 单条删除和批量删除
   * @param ids 请求体中待删除的年级集合
   * @return 返回结果
   */

  @DeleteMapping("/deleteGrade")
  public Result<Object> deleteGrade(@RequestBody List<Integer> ids) {
    // 长度为1时删除单条
    if (ids.size() == 1)
      gradeService.removeById(ids.get(0));
    else
      gradeService.removeByIds(ids);
    return Result.ok();
  }

  /***
   * 添加或修改年级
   * @param grade 请求体中的年级对象
   * @return 返回结果
   */

  @PostMapping("/saveOrUpdateGrade")
  public Result<Object> saveOrUpdateGrade(@RequestBody Grade grade) {
    // 判断请求体中是否有id
    if (grade.getId() == null)
      gradeService.save(grade);
    else
      gradeService.updateById(grade);

    return Result.ok();
  }

  /***
   * 获取所有年级名称
   * @return
   */

  @GetMapping("/getGrades")
  public Result<Object> getGrades() {
    List<Grade> grades = gradeService.list();
    return Result.ok(grades);
  }

}
