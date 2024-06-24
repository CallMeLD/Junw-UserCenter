package my.junw.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import my.junw.usercenter.contant.UserConstant;
import my.junw.usercenter.entity.UserEO;
import my.junw.usercenter.service.IUserEOService;
import my.junw.usercenter.util.UserToUser;
import my.junw.usercenter.vo.UserLoginRequst;
import my.junw.usercenter.vo.UserRegisterRequst;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public Long userRegister(@RequestBody UserRegisterRequst urr){
        // 入参校验，无关业务逻辑
        if(urr == null){
            return null;
        }
        return service.userRegister(urr.getUserAccount(), urr.getUserPassword(), urr.getCheckPassword());
    }

    /**
     * 登录
     * @param ulr
     * @param hsr
     * @return
     */
    @PostMapping("/login")
    public UserEO userLogin(@RequestBody UserLoginRequst ulr, HttpServletRequest hsr){
        // 入参校验，无关业务逻辑
        if(ulr == null){
            return null;
        }
        return service.doLogin(ulr.getUserAccount(), ulr.getUserPassword(),hsr);
    }

    /**
     * 根据用户名查询用户
     * @param userName  可为空
     * @return
     */
    @GetMapping("/search")
    public List<UserEO> userSearch(String userName,HttpServletRequest hsr){
        // 仅管理员可查询
        if(!isAdmin(hsr)){
            return new ArrayList<>();
        }
        // 查询
        QueryWrapper<UserEO> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userName)){
            queryWrapper.like(UserEO.USERNAME,userName);
        }
        List<UserEO> rlist = service.list(queryWrapper);
        // 脱敏
        return rlist.stream().map(user -> UserToUser.INSTANCE.toSafeUser(user)).collect(Collectors.toList());
    }

    /**
     * 根据id删除用户
     * @param id   逻辑删除
     * @return
     */
    @GetMapping("/delete")
    public boolean deleteUser(long id,HttpServletRequest hsr){
        // 仅管理员可查询
        if(!isAdmin(hsr)){
            return false;
        }
        if(id <= 0){
            return false;
        }
        return service.removeById(id);
    }

    // 判定当前登录用户是否为管理员
    private boolean isAdmin(HttpServletRequest hsr){
        // 鉴权
        UserEO loginUser = (UserEO)hsr.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        // 仅管理员可查询
        if(loginUser != null && loginUser.getUserRole() != null &&  UserConstant.ADMIN_ROLE == loginUser.getUserRole()){
            return true;
        }
        return false;
    }

}
