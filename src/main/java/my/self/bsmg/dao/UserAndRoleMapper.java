package my.self.bsmg.dao;

import java.util.List;
import my.self.bsmg.bean.UserAndRole;
import my.self.bsmg.bean.UserAndRoleExample;
import org.apache.ibatis.annotations.Param;

public interface UserAndRoleMapper {
    long countByExample(UserAndRoleExample example);

    int deleteByExample(UserAndRoleExample example);

    int deleteByPrimaryKey(Integer userRoleId);

    int insert(UserAndRole record);

    int insertSelective(UserAndRole record);

    List<UserAndRole> selectByExample(UserAndRoleExample example);

    UserAndRole selectByPrimaryKey(Integer userRoleId);

    int updateByExampleSelective(@Param("record") UserAndRole record, @Param("example") UserAndRoleExample example);

    int updateByExample(@Param("record") UserAndRole record, @Param("example") UserAndRoleExample example);

    int updateByPrimaryKeySelective(UserAndRole record);

    int updateByPrimaryKey(UserAndRole record);
}