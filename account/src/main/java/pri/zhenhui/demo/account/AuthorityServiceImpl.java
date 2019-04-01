package pri.zhenhui.demo.account;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.account.domain.Authority;
import pri.zhenhui.demo.account.domain.Role;
import pri.zhenhui.demo.account.mapper.AuthorityMapper;
import pri.zhenhui.demo.account.mapper.RoleMapper;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class AuthorityServiceImpl implements AuthorityService {

    private final Vertx vertx;

    private final SqlSessionFactory sqlSessionFactory;

    AuthorityServiceImpl(Vertx vertx, SqlSessionFactory sqlSessionFactory) {
        this.vertx = vertx;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public void queryRoles(List<String> roles, Handler<AsyncResult<List<Role>>> resultHandler) {
        if (roles == null || roles.isEmpty()) {
            resultHandler.handle(Future.succeededFuture(new ArrayList<>()));
            return;
        }

        vertx.<List<Role>>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                RoleMapper roleMapper = session.getMapper(RoleMapper.class);
                List<Role> result = roleMapper.selectByTitles(roles);
                future.complete(result);
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryUserRoles(Long userId, Handler<AsyncResult<List<Role>>> resultHandler) {
        vertx.<List<Role>>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                RoleMapper roleMapper = session.getMapper(RoleMapper.class);
                List<Role> roles = roleMapper.selectByUser(userId);
                future.complete(roles);
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryAuthorities(List<String> authorities, Handler<AsyncResult<List<Authority>>> resultHandler) {
        if (authorities == null || authorities.isEmpty()) {
            resultHandler.handle(Future.succeededFuture(new ArrayList<>()));
            return;
        }

        vertx.<List<Authority>>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                List<Authority> result = authorityMapper.selectByTitles(authorities);
                future.complete(result);
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);
    }

    @Override
    public void queryUserAuthorities(Long userId, Handler<AsyncResult<List<Authority>>> resultHandler) {
        vertx.<List<Authority>>executeBlocking(future -> {
            try (SqlSession session = sqlSessionFactory.openSession()) {
                RoleMapper roleMapper = session.getMapper(RoleMapper.class);
                List<Role> roles = roleMapper.selectByUser(userId);
                if (roles.isEmpty()) {
                    future.complete(new ArrayList<>());
                } else {
                    AuthorityMapper authorityMapper = session.getMapper(AuthorityMapper.class);
                    List<Authority> authorities = authorityMapper.selectByRole(roles.stream()
                            .map(Role::getId)
                            .collect(toList()));
                    future.complete(authorities);
                }
            } catch (Exception e) {
                future.fail(e);
            }
        }, resultHandler);
    }
}
