package com.lagou.controller;

import com.lagou.domain.ResourceCategory;
import com.lagou.domain.ResponseResult;
import com.lagou.service.ResourceCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ResourceCategory")
public class ResourceCategoryController {

    @Autowired
    private ResourceCategoryService resourceCategoryService;

    /*
        查询所有资源分类
     */
    @RequestMapping("/findAllResourceCategory")
    public ResponseResult findAllResourceCategory() {
        List<ResourceCategory> allResourceCategory = resourceCategoryService.findAllResourceCategory();

        ResponseResult responseResult = new ResponseResult(true, 200, "查询所有资源分类信息成功", allResourceCategory);
        return responseResult;
    }

    /*
        添加&修改资源分类
     */
    @RequestMapping("/saveOrUpdateResourceCategory")
    public ResponseResult saveOrUpdateResourceCategory(@RequestBody ResourceCategory resourceCategory) {
        ResponseResult responseResult;

        //判断是否携带了章节ID
        if (resourceCategory.getId() == null) {
            //新增
            resourceCategoryService.saveResourceCategory(resourceCategory);
            responseResult = new ResponseResult(true, 200, "新增资源分类成功", null);
            return responseResult;
        } else {
            //修改
            resourceCategoryService.updateResourceCategory(resourceCategory);
            responseResult = new ResponseResult(true, 200, "修改资源分类成功", null);
            return responseResult;
        }
    }

    /*
        删除资源分类
     */
    @RequestMapping("/deleteResourceCategory")
    public ResponseResult deleteResourceCategory(Integer id){
        resourceCategoryService.deleteResourceCategory(id);
        ResponseResult responseResult = new ResponseResult(true, 200, "删除资源分类成功", null);
        return responseResult;
    }


}
