package my.self.bsmg.dao;

import java.util.List;
import java.util.Map;

import my.self.bsmg.bean.Role;
import my.self.bsmg.bean.User;
import my.self.bsmg.bean.UserExample;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Integer userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    /**
     * 查询用户的角色列表
     *
     * @param userId
     * @return
     */
    List<Role> selectRoleListByUserId(Integer userId);

}