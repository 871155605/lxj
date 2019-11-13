package my.self.bsmg.service.impl;

import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import my.self.bsmg.bean.Permission;
import my.self.bsmg.bean.RoleExample;
import my.self.bsmg.bean.User;
import my.self.bsmg.dao.RoleMapper;
import my.self.bsmg.entity.RoleQueryReq;
import my.self.bsmg.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class RoleServiceImpl implements RoleService {
    private static Logger logger = LoggerFactory.getLogger("info");
    @Resource
    private RoleMapper roleMapper;

    @Override
    public PageInfo<User> selectUserList(RoleQueryReq queryReq) {
        return null;
    }

    @Override
    public List<Permission> selectPermissionListByRoleId(Integer roleId) {
        try {
            List<Permission> permissionList = roleMapper.selectPermissionListByRoleId(roleId);
            log.info("SELECT_PERMISSION_LIST_BY_ROLE_ID_SUCCESS|{}", permissionList.size());
            return permissionList;
        } catch (Exception e) {
            log.error("SELECT_PERMISSION_LIST_BY_ROLE_ID_ERROR|{}", e.getMessage());
        }
        return null;
    }

    @Override
    public List<User> selectUserListByRoleId(Integer roleId) {
        try {
            if (roleId != null) {
                List<User> userList = roleMapper.selectUserListByRoleId(roleId);
                logger.info("SELECT_USER_LIST_BY_ROLE_SUCCESS|{}TOTAL|{}", roleId, userList.size());
                return userList;
            }
        } catch (Exception e) {
            logger.error("SELECT_USER_LIST_BY_ROLE_ID_ERROR|{}|{}", roleId, e.getMessage());
        }
        return null;
    }

    private RoleExample createPageQueryExample(String roleName) {
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        if (roleName != null && !"".equals(roleName) && !"null".equals(roleName)) {
            criteria.andNameLike("%" + roleName + "%");
        }
        return roleExample;
    }
}
