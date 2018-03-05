package top.zhwen.controller;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import top.zhwen.domain.LearnResouce;
import top.zhwen.service.LearnService;
import top.zhwen.tools.ServletUtil;
import top.zhwen.tools.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试信息页面
 * Created by zhw on 2017/8/1.
 */
@Controller
@RequestMapping("/learn")
public class LearnController {

    @Autowired
    private LearnService learnService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping("")
    @RequiresPermissions("learn:all")
    public String learn() {
        return "learn-resource";
    }


    @RequestMapping(value = "/queryLeanList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public void queryLearnList(HttpServletRequest request, HttpServletResponse response) {
        String page = request.getParameter("page"); // 取得当前页数,注意这是jqgrid自身的参数
        String rows = request.getParameter("rows"); // 取得每页显示行数，,注意这是jqgrid自身的参数
        String author = request.getParameter("author");
        String title = request.getParameter("title");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page", page);
        params.put("rows", rows);
        params.put("author", author);
        params.put("title", title);
        List<LearnResouce> learnList = learnService.queryLearnResouceList(params);
        PageInfo<LearnResouce> pageInfo = new PageInfo<LearnResouce>(learnList);

        JSONObject jo = new JSONObject();
        jo.put("rows", learnList);
        jo.put("total", pageInfo.getPages());//总页数
        jo.put("records", pageInfo.getTotal());//查询出的总记录数
        ServletUtil.createSuccessResponse(200, jo, response);
    }

    /**
     * 新添标题
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    //@RequiresPermissions("learn:add")//权限管理;
    public void addLearn(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        String author = request.getParameter("author");
        String title = request.getParameter("title");
        String url = request.getParameter("url");
        String message = checkInfo(author, title, url);
        if (!StringUtil.isNull(message)) {
            result.put("message", message);
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        LearnResouce learnResouce = new LearnResouce();
        learnResouce.setAuthor(author);
        learnResouce.setTitle(title);
        learnResouce.setUrl(url);
        int index = learnService.add(learnResouce);
        if (index > 0) {
            result.put("message", "信息添加成功!");
            result.put("flag", true);
        } else {
            result.put("message", "信息添加失败!");
            result.put("flag", false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    /**
     * 修改信息
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    //@RequiresPermissions("learn:update")//权限管理;
    public void updateLearn(HttpServletRequest request, HttpServletResponse response) {
        JSONObject result = new JSONObject();
        String id = request.getParameter("id");
        LearnResouce learnResouce = learnService.queryLearnResouceById(Long.valueOf(id));
        String author = request.getParameter("author");
        String title = request.getParameter("title");
        String url = request.getParameter("url");
        String message = checkInfo(author, title, url);
        if (!StringUtil.isNull(message)) {
            result.put("message", message);
            result.put("flag", false);
            ServletUtil.createSuccessResponse(200, result, response);
            return;
        }
        learnResouce.setAuthor(author);
        learnResouce.setTitle(title);
        learnResouce.setUrl(url);
        int index = learnService.update(learnResouce);
        if (index > 0) {
            result.put("message", "信息修改成功!");
            result.put("flag", true);
        } else {
            result.put("message", "信息修改失败!");
            result.put("flag", false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    /**
     * 删除标题
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    //@RequiresPermissions("learn:delete")//权限管理;
    public void deleteUser(HttpServletRequest request, HttpServletResponse response) {
        String ids = request.getParameter("ids");
        JSONObject result = new JSONObject();
        //删除操作
        int index = learnService.deleteByIds(ids.split(","));
        if (index > 0) {
            result.put("message", "信息删除成功!");
            result.put("flag", true);
        } else {
            result.put("message", "信息删除失败!");
            result.put("flag", false);
        }
        ServletUtil.createSuccessResponse(200, result, response);
    }

    private String checkInfo(String author, String title, String url) {
        if (StringUtil.isNull(author)) {
            return  "名称不能为空!";
        }
        if (StringUtil.isNull(title)) {
            return "标题不能为空!";
        }
        if (StringUtil.isNull(url)) {
            return "地址不能为空!";
        }
        return null;
    }

}