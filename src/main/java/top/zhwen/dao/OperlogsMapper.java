package top.zhwen.dao;

import top.zhwen.domain.Operlogs;
import top.zhwen.domain.OperlogsExample;
import top.zhwen.vo.OperlogsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
@Mapper @Component
public interface OperlogsMapper {
    long countByExample(OperlogsExample example);

    int deleteByExample(OperlogsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Operlogs record);

    int insertSelective(Operlogs record);

    int insertByIds(@Param("list") List<Operlogs> operlogsList);

    List<Operlogs> selectByExample(OperlogsExample example);

    List<OperlogsVO> selectByOrderId(Long orderId);

    Operlogs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Operlogs record, @Param("example") OperlogsExample example);

    int updateByExample(@Param("record") Operlogs record, @Param("example") OperlogsExample example);

    int updateByPrimaryKeySelective(Operlogs record);

    int updateByPrimaryKey(Operlogs record);
}