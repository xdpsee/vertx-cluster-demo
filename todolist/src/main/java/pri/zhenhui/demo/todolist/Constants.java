package pri.zhenhui.demo.todolist;

final class Constants {

    static final String SQL_CREATE_TODOLIST_TABLE = "create table if not exists todolist(id varchar(32) not null primary key, title varchar(256) not null, status varchar(16) not null);";

    static final String SQL_SELECT_ALL_TODOLIST = "select * from todolist";

    static final String SQL_INSERT_TODOLIST = "insert into todolist (id,title,status) values(?,?,?);";

    static final String SQL_REMOVE_TODOLIST = "delete from todolist where id = ? limit 1";

    static final String SQL_UPDATE_TODOLIST = "update todolist set status = ? where id = ?";
}

