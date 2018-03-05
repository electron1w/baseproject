package top.zhwen.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import top.zhwen.domain.Operlogs;
import top.zhwen.service.OperLogsService;
import top.zhwen.tools.ServletUtil;
import top.zhwen.tools.StringUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Zhw on 2017/11/7.
 * 操作日志相关
 */
@Controller
@RequestMapping("/logs")
public class LogsController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private OperLogsService operLogsService;

    @RequestMapping("")
    @RequiresPermissions("logs:all")//权限管理;
    public String operLogsList() {
        return "logs-list";
    }

    @PostMapping("logsQueryList")
    public void logsList(Integer page, Integer rows, Operlogs operlogs, String startTime, String endTime, HttpServletResponse response) throws ParseException {
        if (operlogs.getTypes() == null) {
            operlogs.setTypes(0);
        }
        Date startTimed = null;
        Date endTimed = null;
        if (!StringUtil.isNull(startTime) && !StringUtil.isNull(endTime)) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            startTimed = df.parse(startTime);
            endTimed = df.parse(endTime);
        }
        List<Operlogs> operlogsList = operLogsService.queryLogsList(operlogs, page, rows, startTimed, endTimed);
        PageInfo<Operlogs> pageInfo = new PageInfo<>(operlogsList);
        JSONObject jo = new JSONObject();
        jo.put("rows", operlogsList);
        jo.put("total", pageInfo.getPages());//总页数
        jo.put("records", pageInfo.getTotal());//查询出的总记录数
        ServletUtil.createSuccessResponse(200, jo, response);
    }
}
