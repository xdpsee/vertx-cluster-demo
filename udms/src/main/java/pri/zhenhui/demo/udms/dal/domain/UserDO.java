package pri.zhenhui.demo.udms.dal.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class UserDO implements Serializable {

    private static final long serialVersionUID = 2686703412297767320L;

    private Long id;

    private Long parentId = 0L;

    private String username;

    private String password;

    private String avatar;

    private String phone;

    private String email;

    private Date createAt;

    private Date updateAt;

}

