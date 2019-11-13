package my.self.bsmg.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Role implements Serializable {
    private Integer roleId;

    private String name;

    private String title;

    private String description;

    private Date ctime;

    private Long orders;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Long getOrders() {
        return orders;
    }

    public void setOrders(Long orders) {
        this.orders = orders;
    }

    @JsonIgnore
    private List<User> userList;//一个角色对应多个用户

    @JsonIgnore
    private List<Permission> permissionList;//一个角色对应多个权限

    public List<User> getUserList() {//get对应序列化
        return userList;
    }

    public void setUserList(List<User> userList) {//set对应反序列化 反序列（deserialization，即json数据转换为对象）
        this.userList = userList;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                ", ctime=" + ctime +
                ", userList=" + getUserId(userList) +
                ", permissionList=" + getPermissionId(permissionList) +
                '}';
    }

    private String getPermissionId(List<Permission> list) {
        if (list.isEmpty()) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (Permission permission : list) {
            sb.append(permission.getPermissionId());
            sb.append(" ");
        }
        return sb.toString();
    }

    private String getUserId(List<User> list) {
        if (list.isEmpty()) {
            return "null";
        }
        StringBuilder sb = new StringBuilder();
        for (User user : list) {
            sb.append(user.getUserId());
            sb.append(" ");
        }
        return sb.toString();
    }
}