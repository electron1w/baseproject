package top.zhwen.service.impl;

import com.github.pagehelper.PageHelper;
import top.zhwen.dao.OperlogsMapper;
import top.zhwen.domain.Operlogs;
import top.zhwen.domain.OperlogsExample;
import top.zhwen.domain.OperlogsExample.Criteria;
import top.zhwen.service.OperLogsService;
import top.zhwen.tools.LongUtil;
import top.zhwen.tools.StringUtil;
import top.zhwen.vo.OperlogsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Zhw on 2017/11/1.
 */
@Service
public class OperLogsServiceImpl implements OperLogsService {
    @Autowired
    private OperlogsMapper operlogsMapper;

    @Override
    public List<OperlogsVO> getOperLogsByOrderId(Long orderId) {
        return operlogsMapper.selectByOrderId(orderId);
    }

    @Override
    public List<Operlogs> queryLogsList(Operlogs operlogs, Integer page, Integer rows, Date startTime, Date endTime) {
        if (page != null && rows != null) {
            PageHelper.startPage(page, rows);
        }
        OperlogsExample example = new OperlogsExample();
        Criteria criteria = example.createCriteria();
        proSearchParam(operlogs, criteria);
        if (startTime != null && endTime != null) {
            criteria.andOpertimeBetween(startTime,endTime);
        }
        return operlogsMapper.selectByExample(example);
    }

    @Override
    public int addOrderLog(String title, String content, String orderStatus, Long orderId) {
        Operlogs operlogs = new Operlogs();
        operlogs.setTypes(2);
        operlogs.setOpertime(new Date());
        operlogs.setOrderid(orderId);
        operlogs.setContent(title + "|" + content + "|" + orderStatus);
        return operlogsMapper.insertSelective(operlogs);
    }

    @Override
    public int addOrderLogByOrderIdArray(String title, String content, String orderStatus, String[] orderIds) {
        Operlogs operlogs = new Operlogs();
        operlogs.setTypes(2);
        operlogs.setOpertime(new Date());
        operlogs.setContent(title + "|" + content + "|" + orderStatus);
        List<Operlogs> operlogsList=new ArrayList<>();
        for(String s:orderIds){
            Long s1=LongUtil.parseLong(s);
            if(s1==null){
                continue;
            }
            operlogs.setOrderid(s1);
            operlogsList.add(operlogs);
        }
        return operlogsMapper.insertByIds(operlogsList);
    }

    @Override
    public int addOrderExamineLog(String title, String content, Integer type, Long orderId) {
        Operlogs operlogs = new Operlogs();
        operlogs.setTypes(type);
        operlogs.setOpertime(new Date());
        operlogs.setOrderid(orderId);
        operlogs.setContent(title + "|" + content);
        return operlogsMapper.insertSelective(operlogs);
    }

    @Override
    public int addLog(String loginname, String content,String ip) {
        Operlogs log = new Operlogs();
        log.setContent(content);
        log.setIp(ip);
        log.setLoginname(loginname);
        log.setOpertime(new Date());
        log.setTypes(0);//后台操作
        return operlogsMapper.insertSelective(log);
    }

    /**
     * 处理查询条件
     */
    private void proSearchParam(Operlogs operlogs, Criteria criteria) {
        if (operlogs != null) {
            if (operlogs.getId() != null) {
                criteria.andIdEqualTo(operlogs.getId());
            }
            if (operlogs.getTypes() != null) {
                criteria.andTypesEqualTo(operlogs.getTypes());
            }
            if (!StringUtil.isNull(operlogs.getContent())) {
                criteria.andContentLike("%" + operlogs.getContent() + "%");
            }
            if (!StringUtil.isNull(operlogs.getLoginname())) {
                criteria.andLoginnameLike("%" + operlogs.getLoginname() + "%");
            }
            if (!StringUtil.isNull(operlogs.getIp())) {
                criteria.andIpLike("%" + operlogs.getIp() + "%");
            }
        }
    }
}
