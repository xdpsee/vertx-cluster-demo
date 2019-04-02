package pri.zhenhui.demo.support;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hsqldb.cmdline.SqlFile;

import java.io.File;
import java.net.URL;
import java.sql.Connection;

public class DBUtils {

    public static void initDatabase(SqlSessionFactory sqlSessionFactory) throws Exception {

        SqlSession session = sqlSessionFactory.openSession();
        Connection connection = null;

        try {
            connection = session.getConnection();

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL url = classLoader.getResource("schema/db.schema.sql");

            SqlFile sqlFile = new SqlFile(new File(url.getFile()));
            sqlFile.setConnection(connection);
            sqlFile.execute();

            session.commit();

        } finally {
            if (connection != null) {
                connection.close();
            }
            session.close();
        }

    }

}
