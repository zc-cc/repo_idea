package com.lagou.controller;

import com.github.pagehelper.PageInfo;
import com.lagou.domain.ResponseResult;
import com.lagou.domain.Role;
import com.lagou.domain.User;
import com.lagou.domain.UserVo;
import com.lagou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /*
        用户分页&多条件组合查询
     */
    @RequestMapping("/findAllUserByPage")
    public ResponseResult findAllUserByPage(@RequestBody UserVo userVo) {
        PageInfo pageInfo = userService.findAllUserByPage(userVo);

        ResponseResult responseResult = new ResponseResult(true, 200, "分页多条件查询成功", pageInfo);
        return responseResult;
    }

    /*
        用户登录
     */
    @RequestMapping("/login")
    public ResponseResult login(User user, HttpServletRequest request) {

        User user1 = userService.login(user);

        if (user1 != null) {

            //保存用户id及access_token到session中
            HttpSession session = request.getSession();
            String access_token = UUID.randomUUID().toString();
            session.setAttribute("access_token", access_token);
            session.setAttribute("user_id", user1.getId());

            //将查询到信息响应到前台
            HashMap<String, Object> map = new HashMap<>();
            map.put("access_token", access_token);
            map.put("user_id", user1.getId());

            //将查询出来的user，页存到map中
            map.put("user", user1);

            ResponseResult responseResult = new ResponseResult(true, 200, "登录成功", map);
            return responseResult;
        } else {
            ResponseResult responseResult = new ResponseResult(true, 400, "用户名密码错误", null);
            return responseResult;
        }
    }

    /*
        角色回显（回显）
     */
    @RequestMapping("/findUserRoleById")
    public ResponseResult findUserRoleById(Integer id) {
        List<Role> list = userService.findUserRelationRoleById(id);
        ResponseResult responseResult = new ResponseResult(true, 200, "分配角色回显成功", list);
        return responseResult;
    }

    /*
        分配角色
     */
    @RequestMapping("/userContextRole")
    public ResponseResult userContextRole(@RequestBody UserVo userVo) {

        userService.userContextRole(userVo);

        ResponseResult responseResult = new ResponseResult(true, 200, "分配角色成功", null);
        return responseResult;
    }

    /*
        获取用户权限，进行菜单动态展示
     */
    @RequestMapping("/getUserPermissions")
    public ResponseResult getUserPermissions(HttpServletRequest request) {

        // 1.获取请求头中的token
        String header_token = request.getHeader("Authorization");

        // 2.获取session中token
        String session_token = (String) request.getSession().getAttribute("access_token");

        // 3.判断token是否一致
        if (header_token.equals(session_token)) {
            // 获取用户id
            Integer user_id = (Integer) request.getSession().getAttribute("user_id");
            System.out.println("user_id:" + user_id);
            // 调用service，进行菜单信息查询
            ResponseResult responseResult = userService.getUserPermission(user_id);

            return responseResult;
        } else {
            ResponseResult responseResult = new ResponseResult(false, 400, "获取菜单信息失败", null);
            return responseResult;
        }

    }

}
