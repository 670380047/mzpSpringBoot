package com.example.mzpspringboot.controller;/**
 * @version: java version 1.7+
 * @Author : mzp
 * @Time : 2019/4/10 14:44
 * @File : TestController
 * @Software: IntelliJ IDEA 2019.3.15
 */

import com.example.mzpspringboot.dao.IUserInfoDao;
import com.example.mzpspringboot.model.UserInfo;
import com.example.mzpspringboot.service.CheckUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @Author maozp3
 * @Description:
 * @Date: 2019/4/10 14:44
 */
@Controller
@RequestMapping
public class TestController {
    @Autowired
    CheckUserService checkUserService;
    @Autowired
    IUserInfoDao userInfoDao;



    @RequestMapping("index")
    @ResponseBody
    public UserInfo index(){
        System.out.println("进入index");
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername("毛宗鹏");
        userInfo.setMyAge(24);
        userInfo.setPassword("123");
        return userInfo;
    }

    //	默认访问页面
    @RequestMapping("/welcome")
    public String hello(HttpServletRequest request, Map map){
        map.put("username",request.getParameter("username"));
        map.put("password",request.getParameter("password"));
        if(checkUserService.checkUser(map)){
            System.out.println("进入主页。。。。。。");
            //jpa
            //List<UserInfo> userInfoList = userInfoDao.findAll();
            //mybatis
            List<UserInfo> userInfoList = checkUserService.getAll();
            //这里只是为了和分页共用一个也而多封了一层map,并且key就是"list"
            Map<String,List<UserInfo>> mapList = new HashMap<>();
            mapList.put("list", userInfoList);
            map.put("userInfoList", mapList);   //未完成，未向页面传值
            map.put("message","登陆成功");
            return "main";
        }
        map.put("message","用户名密码不正确");
        return "login";

    }

    @RequestMapping("insert")
    public String insert(UserInfo userInfo){
        checkUserService.insertUserInfo(userInfo);
        return "redirect:getAll";
    }

    @RequestMapping("update")
    public String update(UserInfo userInfo){
        checkUserService.updateUserInfo(userInfo);
        return "redirect:getAll";
    }






    @RequestMapping("/selectAll")
    public String selectAll(ModelMap modelMap){
        //获取所有用户信息
        List<UserInfo> userInfoList =  checkUserService.selectAll();
        modelMap.addAttribute("userInfoList", userInfoList);
        modelMap.addAttribute("message", "欢迎进入MyBatis， 注解方式！");
        return "main";
    }


    @RequestMapping("/getAll")
    public String getAll(ModelMap modelMap, @RequestParam(value = "start",defaultValue = "2") Integer start,
                        @RequestParam(value = "size",defaultValue = "3") int size){
        //设置分页
        PageHelper.startPage(start, size);
        //获取所有用户信息(因为pageHelper的作用，这里就会返回分页的内容了)
        List<UserInfo> userInfoList =  checkUserService.getAll();
        //根据返回的集合，创建PageInfo对象
        PageInfo<UserInfo> pageInfo = new PageInfo<>(userInfoList);
        System.out.println("pageInfo.startRow="+pageInfo.getStartRow());
        //将pageInfo对象扔进Model中
        modelMap.addAttribute("userInfoList", pageInfo);
        modelMap.addAttribute("message", "欢迎进入MyBatis，xml方式！");
        return "main";
    }



    @RequestMapping("/exception")
    @ResponseBody
    public String testDeployment() throws Exception {
        if(true){
            throw new Exception("测试：数据发生异常了");
        }
        return "Spring Boot热部署";
    }
}
