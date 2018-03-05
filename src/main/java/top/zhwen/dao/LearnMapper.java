package top.zhwen.dao;

import top.zhwen.domain.LearnResouce;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by zhw on 2017/8/1.
 */

@Mapper
public interface LearnMapper {
    int add(LearnResouce learnResouce);

    int update(LearnResouce learnResouce);

    int deleteByIds(String[] ids);

    LearnResouce queryLearnResouceById(Long id);

    List<LearnResouce> queryLearnResouceList(Map<String, Object> params);
}
