package my.self.bsmg.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import my.self.bsmg.bean.Role;
import my.self.bsmg.bean.User;
import my.self.bsmg.bean.UserAndRoleExample;
import my.self.bsmg.bean.UserExample;
import my.self.bsmg.dao.UserAndRoleMapper;
import my.self.bsmg.dao.UserMapper;
import my.self.bsmg.entity.UserQueryReq;
import my.self.bsmg.service.UserService;
import my.self.bsmg.util.MD5Util;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Log4j2
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserAndRoleMapper userAndRoleMapper;

    /**
     * 通过用户名查询用户
     *
     * @param username String
     * @return User
     */
    @Override
    public User selectUserByUserName(String username) {
        try {
            if (username != null && !"".equals(username) && !"null".equals(username)) {
                UserExample userExample = new UserExample();
                UserExample.Criteria criteria = userExample.createCriteria();
                criteria.andUsernameEqualTo(username);
                List<User> userList = userMapper.selectByExample(userExample);
                if (userList != null && userList.size() > 0) {
                    User user = userList.get(0);
                    log.info("SELECT_USER_BY_USERNAME_SUCCESS|{}|{}", username, user.getRealname());
                    return user;
                }
            }
            log.info("PLAYER_NOT_FOUND_BY_USERNAME|{}", username);
        } catch (Exception e) {
            log.error("SELECT_USER_BY_USERNAME_ERROR|{}|{}", username, e.getMessage());
        }
        return null;
    }

    /**
     * 通过UserId查询指定用户
     *
     * @param userId Integer
     * @return User
     */
    @Override
    public User selectUserByUserId(Integer userId) {
        try {
            User user = userMapper.selectByPrimaryKey(userId);
            log.info("SELECT_USER_BY_USER_ID_SUCCESS|{}|{}", userId, user == null ? "This user does not exist" : user.getRealname());
            return user;
        } catch (Exception e) {
            log.error("SELECT_USER_BY_USER_ID_ERROR|{}|{}", userId, e.getMessage());
        }
        return null;
    }

    /**
     * 分页+模糊+多条件查询
     *
     * @return PageInfo
     */
    @Override
    public PageInfo<User> selectUserList(UserQueryReq queryReq) {
        try {
            PageHelper.startPage(queryReq.getPageNum(), queryReq.getLimit());
            List<User> userList = userMapper.selectByExample(createPageQueryExample(queryReq.getRealName(), queryReq.getSex(), queryReq.getLocked()));
            PageInfo<User> pageInfo = new PageInfo<>(userList);
            log.info("SELECT_USER_LIST_SUCCESS |{}| TOTAL={}", queryReq.toString(), pageInfo.getTotal());
            return pageInfo;
        } catch (Exception e) {
            log.error("SELECT_USER_LIST_ERROR |{}|{}", queryReq.toString(), e.getMessage());
        }
        return null;
    }

    /**
     * 通过userId查询角色列表
     *
     * @param userId Integer
     * @return List<Role>
     */
    @Override
    public List<Role> selectRoleListByUserId(Integer userId) {
        try {
            List<Role> roleList = userMapper.selectRoleListByUserId(userId);
            log.info("SELECT_ROLE_LIST_BY_USER_ID_SUCCESS|{}", roleList.size());
            return roleList;
        } catch (Exception e) {
            log.error("SELECT_ROLE_LIST_BY_USER_ID_ERROR|{}", e.getMessage());
        }
        return null;
    }

    /**
     * 通过userId删除用户
     *
     * @param userId Integer
     * @return boolean
     */
    @Override
    public boolean deleteUserByUserId(Integer userId) {
        try {
            UserAndRoleExample userAndRoleExample = new UserAndRoleExample();
            userAndRoleExample.createCriteria().andUserIdEqualTo(userId);
            userAndRoleMapper.deleteByExample(userAndRoleExample);
            userMapper.deleteByPrimaryKey(userId);
            log.info("DELETE_USER_BY_USER_ID_SUCCESS|{}", userId);
            return true;
        } catch (Exception e) {
            log.error("DELETE_USER_BY_USER_ID_ERROR|{}|{}", userId, e.getMessage());
        }
        return false;
    }

    /**
     * 添加用户
     * insertSelective 只插入有值的字段
     *
     * @param user User
     * @return boolean
     */
    @Override
    public boolean insertUser(User user) {
        try {
            String password = MD5Util.generate(user.getPassword());
            user.setPassword(password);
            int insert = userMapper.insertSelective(user);
            log.info("INSERT_USER_SELECTIVE_SUCCESS|{}|{}", insert, user.getUsername());
            return true;
        } catch (Exception e) {
            log.error("INSERT_USER_SELECTIVE_ERROR|{}", e.getMessage());
        }
        return false;
    }

    /**
     * 根据手机号查询用户
     *
     * @param phoneNumber String
     * @return User
     */
    @Override
    public User selectUserByPhoneNumber(String phoneNumber) {
        try {
            UserExample example = new UserExample();
            UserExample.Criteria criteria = example.createCriteria();
            criteria.andPhoneEqualTo(phoneNumber);
            List<User> userList = userMapper.selectByExample(example);
            if (userList != null && userList.size() > 0) {
                User user = userList.get(0);
                log.info("SELECT_USER_BY_PHONE_NUMBER_SUCCESS |{}|{}", user.getUserId(), phoneNumber);
                return user;
            }
            log.info("PLAYER_NOT_FOUND_BY_PHONE_NUMBER|{}", phoneNumber);
        } catch (Exception e) {
            log.error("SELECT_USER_BY_PHONE_NUMBER_ERROR |{}|{}", phoneNumber, e.getMessage());
        }
        return null;
    }

    private final static String INIT_PASSWORD = "89ee89338bb516894125d4876c69bfe5";

    @Override
    public String initNewPassword(Map<String, String> map) {
        String password = map.get("newPassword");
        String inItPassword = map.get("initPassword");
        try {
            if (INIT_PASSWORD.equals(inItPassword)) {
                return MD5Util.generate(password);
            }
        } catch (Exception e) {
            log.error("INIT_NEW_PASSWORD_ERROR|{}", e.getMessage());
        }
        return null;
    }

    /**
     * 多条件模糊查询Example构造
     *
     * @param realName String
     * @param sex      Integer
     * @param locked   Integer
     * @return UserExample
     */
    private UserExample createPageQueryExample(String realName, Integer sex, Integer locked) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (realName != null && !"".equals(realName) && !"null".equals(realName)) {
            criteria.andRealnameLike("%" + realName + "%");
        }
        if (sex != null) {
            criteria.andSexEqualTo(sex.byteValue());
        }
        if (locked != null) {
            criteria.andLockedEqualTo(locked.byteValue());
        }
        return userExample;
    }
}
