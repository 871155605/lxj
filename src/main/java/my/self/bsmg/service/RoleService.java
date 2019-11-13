package my.self.bsmg.service;

import com.github.pagehelper.PageInfo;
import my.self.bsmg.bean.Permission;
import my.self.bsmg.bean.User;
import my.self.bsmg.entity.RoleQueryReq;

import java.util.List;

public interface RoleService {

    PageInfo<User> selectUserList(RoleQueryReq queryReq);

    List<Permission> selectPermissionListByRoleId(Integer roleId);

    List<User> selectUserListByRoleId(Integer roleId);
}
