package my.self.bsmg.service;

public interface CheckCodeService {
    public boolean isCanSend(String phoneNumber);

    public boolean sendCheckCode(String phoneNumber);

    public String getCheckCode(String phoneNumber);
}
