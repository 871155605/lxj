package my.self.bsmg.dao;

import java.util.List;

import my.self.bsmg.bean.Permission;
import my.self.bsmg.bean.Role;
import my.self.bsmg.bean.RoleExample;
import my.self.bsmg.bean.User;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    long countByExample(RoleExample example);

    int deleteByExample(RoleExample example);

    int deleteByPrimaryKey(Integer roleId);

    int insert(Role record);

    int insertSelective(Role record);

    List<Role> selectByExample(RoleExample example);

    Role selectByPrimaryKey(Integer roleId);

    int updateByExampleSelective(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByExample(@Param("record") Role record, @Param("example") RoleExample example);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    List<Permission> selectPermissionListByRoleId(Integer roleId);

    List<User> selectUserListByRoleId(Integer roleId);
}