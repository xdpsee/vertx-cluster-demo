package pri.zhenhui.demo.account;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.reactivex.core.AbstractVerticle;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.enums.AuthorityType;
import pri.zhenhui.demo.account.domain.enums.RoleType;
import pri.zhenhui.demo.account.mapper.AuthorityMapper;
import pri.zhenhui.demo.support.DBUtils;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainVerticle extends AbstractVerticle {

    private static Map<RoleType, List<AuthorityType>> ROLE_AUTHORITIES = new HashMap<>();
    static {
        ROLE_AUTHORITIES.put(RoleType.USER, Arrays.asList(
           AuthorityType.TODOLIST_CREATE
        ));

        ROLE_AUTHORITIES.put(RoleType.ADMIN, Arrays.asList(
                AuthorityType.USER_CREATE,
                AuthorityType.USER_VIEW,
                AuthorityType.USER_EDIT,
                AuthorityType.USER_DELETE
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
                    session.commit();
                } catch (Exception e) {
                    session.rollback();
                } finally {
                    session.close();
                }

                emitter.onSuccess("success");
            } catch (Exception e) {
                emitter.onError(e);
            }
        }).ignoreElement()
                .andThen(vertx.rxDeployVerticle(new AccountServiceVerticle()))
                .ignoreElement()
                .andThen(vertx.rxDeployVerticle(new AuthorityServiceVerticle()))
                .ignoreElement();

    }
}

