package my.junw.usercenter.service;

import my.junw.usercenter.entity.UserEO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户 服务类
 * </p>
 *
 * @author ld
 * @since 2024-06-21
 */
public interface IUserEOService extends IService<UserEO> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    long userRegister(String userAccount,String userPassword,String checkPassword);



}
