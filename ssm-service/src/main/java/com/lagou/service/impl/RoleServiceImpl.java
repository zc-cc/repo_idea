package com.lagou.service.impl;

import com.lagou.dao.RoleMapper;
import com.lagou.domain.*;
import com.lagou.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<Role> findAllRole(Role role) {
        List<Role> allRole = roleMapper.findAllRole(role);
        return allRole;
    }

    @Override
    public List<Integer> findMenuByRoleId(Integer roleId) {
        List<Integer> menuByRoleId = roleMapper.findMenuByRoleId(roleId);

        return menuByRoleId;
    }

    @Override
    public void roleContextMenu(RoleMenuVo roleMenuVo) {

        //1.清空中间表的关联关系
        roleMapper.deleteRoleContextMenu(roleMenuVo.getRoleId());

        //2.为角色分配菜单

        for (Integer mid : roleMenuVo.getMenuIdList()) {

            Role_menu_relation role_menu_relation = new Role_menu_relation();
            role_menu_relation.setMenuId(mid);
            role_menu_relation.setRoleId(roleMenuVo.getRoleId());

            //封装数据
            Date date = new Date();
            role_menu_relation.setCreatedTime(date);
            role_menu_relation.setUpdatedTime(date);

            role_menu_relation.setCreatedBy("system");
            role_menu_relation.setUpdatedby("system");

            roleMapper.roleContextMenu(role_menu_relation);
        }
    }

    @Override
    public void deleteRole(Integer roleId) {

        //调用根据roleId清空中间表关联关系
        roleMapper.deleteRoleContextMenu(roleId);

        roleMapper.deleteRole(roleId);
    }

    @Override
    public ResponseResult findResourceListByRoleId(Integer roleId) {
        //调用mapper
        List<ResourceCategory> categoryList = roleMapper.findResourceCategoryById(roleId);
        List<Resource> resourceList = roleMapper.findResourceById(roleId);

        //封装数据
        for (ResourceCategory category : categoryList) {
            ArrayList<Resource> list = new ArrayList<>();
            for (Resource resource : resourceList) {
                if (resource.getCategoryId()==category.getId()){
                    list.add(resource);
                }
            }
            category.setResourceList(list);
        }

        ResponseResult responseResult = new ResponseResult(true, 200, "获取当前角色拥有的资源信息成功", categoryList);
        return responseResult;
    }

    @Override
    public void roleContextResource(RoleResourceVo roleResourceVo) {
        // 1.清空中间表的关联关系
        roleMapper.deleteRoleContextResource(roleResourceVo.getRoleId());

        // 2.为角色分配资源
        for (Integer rId : roleResourceVo.getResourceIdList()) {
            RoleResourceRelation resourceRelation = new RoleResourceRelation();
            resourceRelation.setResourceId(rId);
            resourceRelation.setRoleId(roleResourceVo.getRoleId());

            //封装数据
            Date date = new Date();
            resourceRelation.setCreatedTime(date);
            resourceRelation.setUpdatedTime(date);

            resourceRelation.setCreatedBy("system");
            resourceRelation.setUpdatedBy("system");
            roleMapper.roleContextResource(resourceRelation);
        }
    }
}
