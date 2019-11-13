package my.self.bsmg.mgr;

import lombok.extern.log4j.Log4j2;
import my.self.bsmg.mgr.tool.LRUMap;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Log4j2
@Repository
@EnableScheduling
public class LoginRestrictionMgr {
    private static final int MAX_CACHE = 100;
    private LRUMap<String, Integer> loginRestrictionMap = new LRUMap<>();

    @PostConstruct
    protected void init() {
        loginRestrictionMap.setMaxEntries(MAX_CACHE);
    }

    public Integer getLoginRestrictionTimes(String username) {
        return loginRestrictionMap.get(username);
    }

    public void setLoginRestrictionTimes(String username, Integer times) {
        loginRestrictionMap.put(username, times);
    }

    public void removeUserLoginRestriction(String username) {
        loginRestrictionMap.remove(username);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void clearLoginRestrictionMap() {
        int size = loginRestrictionMap.size();
        loginRestrictionMap.clear();
        log.info("CLEAR_LOGIN_RESTRICTION_MAP_SUCCESS|{}", size);
    }
}
