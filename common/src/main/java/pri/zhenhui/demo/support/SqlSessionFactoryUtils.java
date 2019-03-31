package pri.zhenhui.demo.support;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class SqlSessionFactoryUtils {

    public static SqlSessionFactory build() throws Exception {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("conf/mybatis-config.xml");
        if (inputStream == null) {
            throw new Exception("conf/mybatis-config.xml not found, please config it!");
        }

        return new SqlSessionFactoryBuilder().build(inputStream);
    }

}
