package my.junw.usercenter.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户
 * </p>
 *
 * @author ld
 * @since 2024-06-21
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("user")
public class UserEO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户昵称
     */
    @TableField("username")
    private String username;

    /**
     * 账号
     */
    @TableField("userAccount")
    private String userAccount;

    /**
     * 用户头像
     */
    @TableField("avatarUrl")
    private String avatarUrl;

    /**
     * 性别
     */
    @TableField("gender")
    private Integer gender;

    /**
     * 密码
     */
    @TableField("userPassword")
    private String userPassword;

    /**
     * 电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 用户状态 0-正常
     */
    @TableField("userStatus")
    private Integer userStatus;

    /**
     * 创建时间
     */
    @TableField("createTime")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("updateTime")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    @TableField("isDelete")
    private Integer isDelete;

    /**
     * 用户角色（0-普通用户；1-管理员）
     */
    @TableField("userRole")
    private Integer userRole;

    public static final String ID = "id";

    public static final String USERNAME = "username";

    public static final String USERACCOUNT = "userAccount";

    public static final String AVATARURL = "avatarUrl";

    public static final String GENDER = "gender";

    public static final String USERPASSWORD = "userPassword";

    public static final String PHONE = "phone";

    public static final String EMAIL = "email";

    public static final String USERSTATUS = "userStatus";

    public static final String CREATETIME = "createTime";

    public static final String UPDATETIME = "updateTime";

    public static final String ISDELETE = "isDelete";

    public static final String USERROLE = "userRole";
}
