package my.self.bsmg.entity;

import lombok.Data;

@Data
public class UserQueryReq {
    String realName;
    Integer sex;
    Integer locked;
    Integer pageNum = 1;
    Integer limit = 5;

    @Override
    public String toString() {
        return "realName=" + realName +
                " sex=" + sex +
                " locked=" + locked +
                " page=" + pageNum +
                " limit=" + limit;
    }
}
