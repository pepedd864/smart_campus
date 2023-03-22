package com.demo.modules_campus.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.modules_campus.pojo.Clazz;
import com.demo.modules_campus.service.ClazzService;
import com.demo.modules_campus.service.GradeService;
import com.demo.modules_campus.utils.Result;
import com.github.xiaoymin.knife4j.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ClazzsController
 * @Description TODO
 * @Date 2023/3/22 8:47
 * @Created by admin
 */
@Api(tags = "班级管理")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzsController {

  @Resource
  private ClazzService clazzService;
  @Resource
  private GradeService gradeService;

  /***
   * 获取班级列表
   * @param pn  当前页码
   * @param size  每页显示的条数
   * @param clazzName 班级名称
   * @return Page 对象
   */
  @ApiOperation(value = "获取班级列表", notes = "获取班级列表")
  @RequestMapping("/getClazzsByOpr/{pn}/{size}")
  public Result<Object> getGrades(@ApiParam("当前页码") @PathVariable("pn") Integer pn,
                                  @ApiParam("页码大小") @PathVariable("size") Integer size,
                                  @ApiParam("模糊查询条件") String clazzName) {
    Page<Clazz> page = clazzService.page(new Page<>(pn, size), new LambdaQueryWrapper<Clazz>()
        .like(StrUtil.isNotBlank(clazzName), Clazz::getName, clazzName).orderByAsc(Clazz::getId));
    return Result.ok(page);
  }

  /***
   * 单条删除和批量删除
   * @param ids 请求体中待删除的班级集合
   * @return 返回结果
   */

  @DeleteMapping("/deleteGrade")
  public Result<Object> deleteGrade(@RequestBody List<Integer> ids) {
    // 长度为1时删除单条
    if (ids.size() == 1)
      clazzService.removeById(ids.get(0));
    else
      clazzService.removeByIds(ids);
    return Result.ok();
  }

  /***
   * 添加或修改班级
   * @param clazz 请求体中的班级对象
   * @return 返回结果
   */

  @PostMapping("/saveOrUpdateGrade")
  public Result<Object> saveOrUpdateGrade(@RequestBody Clazz clazz) {
    // 判断请求体中是否有id
    if (clazz.getId() == null)
      clazzService.save(clazz);
    else
      clazzService.updateById(clazz);

    return Result.ok();
  }

  /***
   * 获取所有班级名称
   * @return
   */

  @GetMapping("/getClazzs")
  public Result<Object> getClazzs() {
    List<Clazz> clazzs = clazzService.list();
    return Result.ok(clazzs);
  }

}
