package my.self.bsmg.service.impl;

import lombok.extern.log4j.Log4j2;
import my.self.bsmg.service.CheckCodeService;
import my.self.bsmg.util.RedisUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Log4j2
public class CheckCodeServiceImpl implements CheckCodeService {
    @Resource
    private RedisUtil redisUtil;

    public boolean isCanSend(String phoneNumber) {
        return !redisUtil.hasKey(phoneNumber);
    }

    public boolean sendCheckCode(String phoneNumber) {
        log.info("SEND_CHECK_CODE|{}|{}", "1234", phoneNumber);
        return redisUtil.set("loginCheck|" + phoneNumber, "1234", 60);
    }

    public String getCheckCode(String phoneNumber) {
        String checkCode = (String) redisUtil.get(phoneNumber);
        log.info("GET_CHECK_CODE|{}|{}", "loginCheck|" + phoneNumber, checkCode);
        return checkCode;
    }
}
