package my.junw.usercenter.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import my.junw.usercenter.common.ErrorCode;
import my.junw.usercenter.contant.UserConstant;
import my.junw.usercenter.entity.UserEO;
import my.junw.usercenter.dao.UserEODao;
import my.junw.usercenter.exception.BusinessException;
import my.junw.usercenter.service.IUserEOService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import my.junw.usercenter.util.UserToUser;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.net.http.HttpRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static my.junw.usercenter.contant.UserConstant.SALT;
import static my.junw.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author ld
 * @since 2024-06-21
 */
@Service
@Slf4j
public class UserEOServiceImpl extends ServiceImpl<UserEODao, UserEO> implements IUserEOService {


    // 用户注册
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空。");
        }
        if(userAccount.length() <4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度至少为4。");
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度至少为8。");
        }
        // 账号不能包含特殊字符
        String regex = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。 ，、？]";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能包含特殊字符。");
        }
        // 密码与校验密码要一致
        if(!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码与校验密码不一致。");
        }
        // --> 数据库查询 放其他校验之后
        // 账号不能重复
        QueryWrapper<UserEO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserEO.USERACCOUNT,userAccount);
        long count = this.count(queryWrapper);
        if(count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已被注册，请重新尝试。");
        }

        // 2. 密码加密
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword ).getBytes());

        // 3. 保存数据
        UserEO addUser = new UserEO();
        addUser.setUserAccount(userAccount);
        addUser.setUserPassword(md5Password);
        boolean saveResult = this.save(addUser);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"账号注册保存失败，请联系管理员。");
        }
        return addUser.getId();
    }

    // 用户登录
    @Override
    public UserEO doLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空。");
        }
        if(userAccount.length() <4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度至少为4。");
        }
        if(userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码长度至少为8。");
        }
        // 账号不能包含特殊字符
        String regex = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。 ，、？]";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if(matcher.find()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号不能包含特殊字符。");
        }
        // 2. 密码加密
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword ).getBytes());
        // 查询用户是否存在
        QueryWrapper<UserEO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserEO.USERACCOUNT,userAccount);
        queryWrapper.eq(UserEO.USERPASSWORD,md5Password);
        UserEO dbuser = this.baseMapper.selectOne(queryWrapper);
        if(dbuser == null){
            // 不存在，记录日志
            log.info("The user fails to log in and the query account does not exist");
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户不存在。");
        }
        // 3. 返回安全用户(脱敏)
        UserEO safeUser = UserToUser.INSTANCE.toSafeUser(dbuser);
        // 4. 记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE,safeUser);
        return safeUser;
    }

    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}
