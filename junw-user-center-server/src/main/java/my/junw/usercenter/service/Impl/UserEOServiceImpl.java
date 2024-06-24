package my.junw.usercenter.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.val;
import my.junw.usercenter.entity.UserEO;
import my.junw.usercenter.dao.UserEODao;
import my.junw.usercenter.service.IUserEOService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 用户 服务实现类
 * </p>
 *
 * @author ld
 * @since 2024-06-21
 */
@Service
public class UserEOServiceImpl extends ServiceImpl<UserEODao, UserEO> implements IUserEOService {

    // 用户注册
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        if(userAccount.length() <4){
            return -1;
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }
        // 账号不能包含特殊字符
        String regex = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。 ，、？]";
        Matcher matcher = Pattern.compile(regex).matcher(userAccount);
        if(matcher.find()){
            return -1;
        }
        // 密码与校验密码要一致
        if(!userPassword.equals(checkPassword)){
            return -1;
        }
        // --> 数据库查询 放其他校验之后
        // 账号不能重复
        QueryWrapper<UserEO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(UserEO.USERACCOUNT,userAccount);
        long count = this.count(queryWrapper);
        if(count > 0){
            return -1;
        }

        // 2. 密码加密
        final String SALT = "junw";
        String md5Password = DigestUtils.md5DigestAsHex((SALT + userPassword ).getBytes());

        // 3. 保存数据
        UserEO addUser = new UserEO();
        addUser.setUserAccount(userAccount);
        addUser.setUserPassword(md5Password);
        boolean saveResult = this.save(addUser);
        if(!saveResult){
            return -1;
        }
        return addUser.getId();
    }
}
