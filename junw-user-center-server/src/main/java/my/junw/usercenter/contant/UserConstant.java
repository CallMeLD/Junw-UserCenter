package my.junw.usercenter.contant;

/**
 * 常量类
 */
public interface UserConstant {

    /**
     * 盐值，混淆密码
     */
     String SALT = "junw";

    /**
     * 用户登录态
     */
    String USER_LOGIN_STATE = "junw-userCenter-login";

    // 权限
    // 管理员
    int ADMIN_ROLE = 1;
    // 普通用户
    int REGULA_ROLE = 0;



}
