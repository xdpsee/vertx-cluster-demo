package pri.zhenhui.demo.todolist;

import org.apache.ibatis.session.SqlSessionFactory;
import pri.zhenhui.demo.support.AbstractMicroServiceVerticle;
import pri.zhenhui.demo.support.DBUtils;
import pri.zhenhui.demo.support.SqlSessionFactoryLoader;

public class TodolistServiceVerticle extends AbstractMicroServiceVerticle<TodolistService> {



    public TodolistServiceVerticle() {
        super(TodolistService.SERVICE_NAME, TodolistService.SERVICE_ADDRESS, TodolistService.class);
    }

    @Override
    public void start() throws Exception {
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryLoader.load();
        DBUtils.initDatabase(sqlSessionFactory);
    }

    @Override
    protected TodolistService serviceImpl() {
        return new TodolistServiceImpl(vertx.getOrCreateContext());
    }

}




