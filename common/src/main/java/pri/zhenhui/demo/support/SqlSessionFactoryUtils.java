package pri.zhenhui.demo.support;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class SqlSessionFactoryUtils {

    private static SqlSessionFactory sqlSessionFactory = null;

    public synchronized static SqlSessionFactory build() {

        if (sqlSessionFactory != null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("conf/mybatis-config.xml");
            if (inputStream == null) {
                throw new RuntimeException("conf/mybatis-config.xml not found, please config it!");
            }

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }

        return sqlSessionFactory;
    }

}
