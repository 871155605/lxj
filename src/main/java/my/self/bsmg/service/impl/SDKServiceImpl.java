package my.self.bsmg.service.impl;

import my.self.bsmg.service.SDKService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SDKServiceImpl implements SDKService {
    private final String alySendSmsSignName = "";
    private final String alySendSmsTemplateCode = "";

    @Override
    public boolean alySendSms(String phoneNumber) {
        return false;
    }

    private String getSmsCode() {
        Random random = new Random();
        StringBuilder smsCode = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            smsCode.append(random.nextInt(10));
        }
        return smsCode.toString();
    }
}
