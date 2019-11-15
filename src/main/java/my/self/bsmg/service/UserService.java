package my.self.bsmg.service;

import com.github.pagehelper.PageInfo;
import my.self.bsmg.bean.Role;
import my.self.bsmg.bean.User;
import my.self.bsmg.entity.UserQueryReq;

import java.util.List;
import java.util.Map;

public interface UserService {
    /**
     * 通过用户名查询用户
     *
     * @param username String
     * @return User
     */
    User selectUserByUserName(String username);

    /**
     * 通过UserId查询指定用户
     *
     * @param userId Integer
     * @return User
     */
    User selectUserByUserId(Integer userId);

    /**
     * 分页+模糊+多条件查询
     *
     * @return PageInfo
     */
    PageInfo<User> selectUserList(UserQueryReq userQueryReq);

    /**
     * 通过userId查询角色列表
     *
     * @param userId Integer
     * @return List<Role>
     */
    List<Role> selectRoleListByUserId(Integer userId);

    /**
     * 通过userId删除用户
     *
     * @param userId Integer
     * @return boolean
     */
    boolean deleteUserByUserId(Integer userId);

    /**
     * 添加用户
     *
     * @param user User
     * @return boolean
     */
    boolean insertUser(User user);

    /**
     * 根据手机号查询用户
     *
     * @param phoneNumber String
     * @return User
     */
    User selectUserByPhoneNumber(String phoneNumber);

    /**
     * 初始化一个加密的密码
     *
     * @param map Map<String, String>
     * @return String
     */
    String initNewPassword(Map<String, String> map);
}
