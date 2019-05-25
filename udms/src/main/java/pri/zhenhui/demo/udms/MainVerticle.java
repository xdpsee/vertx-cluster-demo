package pri.zhenhui.demo.udms;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mindrot.jbcrypt.BCrypt;
import pri.zhenhui.demo.support.db.DBUtils;
import pri.zhenhui.demo.support.db.mybatis.SqlSessionFactoryLoader;
import pri.zhenhui.demo.udms.dal.domain.UserDO;
import pri.zhenhui.demo.udms.dal.mapper.AuthorityMapper;
import pri.zhenhui.demo.udms.dal.mapper.UserMapper;
import pri.zhenhui.demo.udms.domain.enums.AuthorityType;
import pri.zhenhui.demo.udms.domain.enums.RoleType;
import pri.zhenhui.demo.udms.verticles.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhenhui
 */
public class MainVerticle extends AbstractVerticle {

    public static Map<RoleType, List<AuthorityType>> ROLE_AUTHORITIES = new HashMap<>();

    static {
        ROLE_AUTHORITIES.put(RoleType.USER, Arrays.asList(
                AuthorityType.TODOLIST_CREATE,
                AuthorityType.TODOLIST_VIEW,
                AuthorityType.TODOLIST_EDIT,
                AuthorityType.TODOLIST_DELETE
        ));

        ROLE_AUTHORITIES.put(RoleType.ADMIN, Arrays.asList(
                AuthorityType.USER_CREATE,
                AuthorityType.USER_VIEW,
                AuthorityType.USER_EDIT,
                AuthorityType.USER_DELETE,

                AuthorityType.TODOLIST_CREATE,
                AuthorityType.TODOLIST_VIEW,
                AuthorityType.TODOLIST_EDIT,
                AuthorityType.TODOLIST_DELETE


        ));
    }

    @Override
    public Completable rxStart() {

        return Single.create(emitter -> {
            try {
                SqlSessionFactory sqlSessionFactory = SqlSessionFactoryLoader.load();
                DBUtils.initDatabase(sqlSessionFactory);

                final SqlSession session = sqlSessionFactory.openSession();
                try {
                    AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                    ROLE_AUTHORITIES.forEach((role, authorities) -> {
                        int rows = authorityMapper.insertRoleAuthorities(role.id, authorities.stream().map(a -> a.id).collect(Collectors.toList()));
                        System.out.println("insertRoleAuthorities => " + rows);
                    });

                    UserMapper userMapper = session.getMapper(UserMapper.class);
                    UserDO userDO = new UserDO();
                    userDO.setParentId(0L);
                    userDO.setUsername("zhcen");
                    userDO.setPassword(BCrypt.hashpw("123456", BCrypt.gensalt()));
                    userMapper.insert(userDO);

                    session.commit();
                } catch (Exception e) {
                    session.rollback();
                } finally {
                    session.close();
                }

                emitter.onSuccess(true);
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).ignoreElement()
                .andThen(vertx.rxDeployVerticle(new AccountServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new AuthorityServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new UserGroupServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new UserDeviceServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new DeviceGroupServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new UserTokenServiceVerticle()))
                .ignoreElement();

    }
}

