package my.self.bsmg.dao;

import java.util.List;
import my.self.bsmg.bean.RoleAndPermission;
import my.self.bsmg.bean.RoleAndPermissionExample;
import org.apache.ibatis.annotations.Param;

public interface RoleAndPermissionMapper {
    long countByExample(RoleAndPermissionExample example);

    int deleteByExample(RoleAndPermissionExample example);

    int deleteByPrimaryKey(Integer rolePermissionId);

    int insert(RoleAndPermission record);

    int insertSelective(RoleAndPermission record);

    List<RoleAndPermission> selectByExample(RoleAndPermissionExample example);

    RoleAndPermission selectByPrimaryKey(Integer rolePermissionId);

    int updateByExampleSelective(@Param("record") RoleAndPermission record, @Param("example") RoleAndPermissionExample example);

    int updateByExample(@Param("record") RoleAndPermission record, @Param("example") RoleAndPermissionExample example);

    int updateByPrimaryKeySelective(RoleAndPermission record);

    int updateByPrimaryKey(RoleAndPermission record);
}