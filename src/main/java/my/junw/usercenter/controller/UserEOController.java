package my.junw.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import my.junw.usercenter.common.BaseResponse;
import my.junw.usercenter.common.ErrorCode;
import my.junw.usercenter.common.ResultUtils;
import my.junw.usercenter.entity.UserEO;
import my.junw.usercenter.exception.BusinessException;
import my.junw.usercenter.service.IUserEOService;
import my.junw.usercenter.util.UserToUser;
import my.junw.usercenter.vo.UserLoginRequst;
import my.junw.usercenter.vo.UserRegisterRequst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static my.junw.usercenter.contant.UserConstant.ADMIN_ROLE;
import static my.junw.usercenter.contant.UserConstant.USER_LOGIN_STATE;

/**
 * <p>
 * 用户 前端控制器
 * </p>
 *
 * @author ld
 * @since 2024-06-21
 */
@RestController   // 返回值默认为 json 类型
@RequestMapping("/user")
public class UserEOController {

    @Resource
    private IUserEOService service;

    /**
     * 注册
     * @param urr
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequst urr){
        // 入参校验，无关业务逻辑
        if(urr == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = urr.getUserAccount();
        String userPassword = urr.getUserPassword();
        String checkPassword = urr.getCheckPassword();
        if(StringUtils.isBlank(userAccount) || StringUtils.isBlank(userPassword) || StringUtils.isBlank(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(service.userRegister(userAccount, userPassword, checkPassword));
    }

    /**
     * 登录
     * @param ulr
     * @param hsr
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserEO> userLogin(@RequestBody UserLoginRequst ulr, HttpServletRequest hsr){
        // 入参校验，无关业务逻辑
        if(ulr == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = ulr.getUserAccount();
        String userPassword = ulr.getUserPassword();
        if(StringUtils.isBlank(userAccount) || StringUtils.isBlank(userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(service.doLogin(userAccount, userPassword,hsr));
    }

    /**
     * 用户注销
     * @param hsr
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest hsr){
        if(hsr == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = service.userLogout(hsr);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     * @param hsr
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<UserEO> getCurrentUser(HttpServletRequest hsr){
        UserEO cuser = (UserEO) hsr.getSession().getAttribute(USER_LOGIN_STATE);
        if(cuser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = cuser.getId();
        // 数据库 验证用户是否合法
        UserEO dbuser = service.getById(userId);
        UserEO safeUser = UserToUser.INSTANCE.toSafeUser(dbuser);
        return  ResultUtils.success(safeUser);
    }


    /**
     * 根据用户名查询用户
     * @param userName  可为空
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<UserEO>> userSearch(String userName,HttpServletRequest hsr){
        // 仅管理员可查询
        if(!isAdmin(hsr)){
            throw new BusinessException(ErrorCode.NO_AUTH,"仅限管理员可操作");
        }
        // 查询
        QueryWrapper<UserEO> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userName)){
            queryWrapper.like(UserEO.USERNAME,userName);
        }
        List<UserEO> rlist = service.list(queryWrapper);
        List<UserEO> safelist = rlist.stream().map(user -> UserToUser.INSTANCE.toSafeUser(user)).collect(Collectors.toList());
        // 脱敏
        return ResultUtils.success(safelist);
    }

    /**
     * 根据id删除用户
     * @param id   逻辑删除
     * @return
     */
    @GetMapping("/delete")
    public BaseResponse<Boolean> deleteUser(long id, HttpServletRequest hsr){
        // 仅管理员可查询
        if(!isAdmin(hsr)){
            throw new BusinessException(ErrorCode.NO_AUTH,"仅限管理员可操作");
        }
        if(id <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"不在数据范围内");
        }
        return ResultUtils.success(service.removeById(id));
    }

    // 判定当前登录用户是否为管理员
    private boolean isAdmin(HttpServletRequest hsr){
        // 鉴权
        UserEO loginUser = (UserEO)hsr.getSession().getAttribute(USER_LOGIN_STATE);
        // 仅管理员可查询
        if(loginUser != null && loginUser.getUserRole() != null &&  ADMIN_ROLE == loginUser.getUserRole()){
            return true;
        }
        return false;
    }

}
