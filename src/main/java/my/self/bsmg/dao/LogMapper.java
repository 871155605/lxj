package my.self.bsmg.dao;

import java.util.List;
import my.self.bsmg.bean.Log;
import my.self.bsmg.bean.LogExample;
import my.self.bsmg.bean.LogWithBLOBs;
import org.apache.ibatis.annotations.Param;

public interface LogMapper {
    long countByExample(LogExample example);

    int deleteByExample(LogExample example);

    int deleteByPrimaryKey(Integer logId);

    int insert(LogWithBLOBs record);

    int insertSelective(LogWithBLOBs record);

    List<LogWithBLOBs> selectByExampleWithBLOBs(LogExample example);

    List<Log> selectByExample(LogExample example);

    LogWithBLOBs selectByPrimaryKey(Integer logId);

    int updateByExampleSelective(@Param("record") LogWithBLOBs record, @Param("example") LogExample example);

    int updateByExampleWithBLOBs(@Param("record") LogWithBLOBs record, @Param("example") LogExample example);

    int updateByExample(@Param("record") Log record, @Param("example") LogExample example);

    int updateByPrimaryKeySelective(LogWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LogWithBLOBs record);

    int updateByPrimaryKey(Log record);
}