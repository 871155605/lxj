package my.self.bsmg.controller;

import lombok.extern.log4j.Log4j2;
import my.self.bsmg.entity.GlobalResponse;
import my.self.bsmg.service.CheckCodeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/checkCode")
@Log4j2
public class CheckCodeController {
    @Resource
    private CheckCodeService checkCodeService;

    @PostMapping("/getCheckCode")
    public GlobalResponse getCheckCode(@RequestBody String phoneNumber) {
        log.info("GET_CHECK_CODE|{}", phoneNumber);
        //消除末尾多余的等号
        String[] split = phoneNumber.split("=");
        phoneNumber = split[0];
        if (checkCodeService.isCanSend(phoneNumber)) {
            if (checkCodeService.sendCheckCode(phoneNumber)) {
                return GlobalResponse.of("验证码已发送，60S内有效");
            }
            return GlobalResponse.of(-1, "验证码发送失败，请重试");
        }
        return GlobalResponse.of(-1, "验证码已发送，请稍后再试");
    }
}
