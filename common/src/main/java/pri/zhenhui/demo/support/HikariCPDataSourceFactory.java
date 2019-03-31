package pri.zhenhui.demo.support;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Properties;

public class HikariCPDataSourceFactory extends UnpooledDataSourceFactory {

    @Override
    public DataSource getDataSource() {

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classloader.getResourceAsStream("conf/hikari.properties");
        if (inputStream == null) {
            throw new RuntimeException("conf/hikari.properties not found, please config it!");
        }

        try {
            Properties properties = new Properties();
            properties.load(inputStream);

            HikariConfig config = new HikariConfig(properties);
            return new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (Throwable e){
                // ignore
            }
        }
    }
}
