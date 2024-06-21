package my.junw.usercenter.controller;
import my.junw.usercenter.service.Impl.UserServiceImpl;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

/**
* 用户(user)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/user")
public class UserController {
/**
* 服务对象
*/
    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
    * 通过主键查询单条数据
    *
    * @param id 主键
    * @return 单条数据
    */
    @GetMapping("selectOne")
    public User selectOne(Integer id) {
    return userServiceImpl.selectByPrimaryKey(id);
    }

}
