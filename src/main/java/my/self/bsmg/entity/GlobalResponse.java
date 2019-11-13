package my.self.bsmg.entity;

import lombok.Data;

/**
 * 全局响应，统一格式
 */
@Data
public class GlobalResponse {
    private int code;
    private String message;
    private Object data;

    private GlobalResponse(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static GlobalResponse of(Object data) {
        return new GlobalResponse(0, "successful", data);
    }

    public static GlobalResponse of(int code, String message) {
        return new GlobalResponse(code, message, null);
    }
}
