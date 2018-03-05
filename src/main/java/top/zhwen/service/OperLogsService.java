package top.zhwen.service;

import top.zhwen.domain.Operlogs;
import top.zhwen.vo.OperlogsVO;

import java.util.Date;
import java.util.List;

/**
 * Created by Zhw on 2017/11/1.
 */
public interface OperLogsService {
    /**
     * 根据orderId获取订单log operTime DESC
     * */
    List<OperlogsVO> getOperLogsByOrderId(Long orderId);

    List<Operlogs> queryLogsList(Operlogs operlogs, Integer page, Integer rows, Date startTime, Date endTime);
    /**
     *  用户申请进度日志
     *  @param title 开始放款  借款审核通过 等订单状态中文
     *  @param content 请尽快确认借款,过期无法确认 等详细说明
     *  @param orderStatus    订单状态 0:放弃 1 :成功 2:失败
     * */
    int addOrderLog(String title,String content,String orderStatus,Long orderId);

    int addOrderLogByOrderIdArray(String title, String content, String orderStatus, String[] orderIds);

    int addOrderExamineLog(String title,String content,Integer type,Long orderId);

    int addLog(String loginname, String content,String ip);

}
