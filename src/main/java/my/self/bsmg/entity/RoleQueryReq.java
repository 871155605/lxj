package my.self.bsmg.entity;

import lombok.Data;

@Data
public class RoleQueryReq {
    String roleName;
    Integer page = 1;
    Integer limit = 5;

    @Override
    public String toString() {
        return "roleName=" + roleName +
                " page=" + page +
                " limit=" + limit;
    }
}
