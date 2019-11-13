package my.self.bsmg.config.datasource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;


//配置Mybatis(单数据源时使用)
@Configuration
@MapperScan("my.self.bsmg.dao")
public class MybatisConfig {
    @Autowired
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sessionFactory() throws IOException {
        SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setTypeAliasesPackage("my.self.bsmg.bean");
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:my/self/bsmg/mapper/*Mapper.xml");
        sessionFactoryBean.setMapperLocations(resources);
        return sessionFactoryBean;
    }
}
