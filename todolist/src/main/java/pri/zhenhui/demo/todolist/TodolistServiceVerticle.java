package pri.zhenhui.demo.todolist;

import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.support.DBUtils;
import pri.zhenhui.demo.support.SqlSessionFactoryUtils;

public class TodolistServiceVerticle extends AbstractMicroServiceVerticle<TodolistService> {


    private SqlSessionFactory sqlSessionFactory;

    public TodolistServiceVerticle() {
        super(TodolistService.SERVICE_NAME, TodolistService.SERVICE_ADDRESS, TodolistService.class);
    }

    @Override
    public void start() throws Exception {
        sqlSessionFactory = SqlSessionFactoryUtils.build();

        DBUtils.initDatabase(sqlSessionFactory);
    }

    @Override
    protected TodolistService serviceImpl() {
        return new TodolistServiceImpl(vertx, sqlSessionFactory);
    }

}




