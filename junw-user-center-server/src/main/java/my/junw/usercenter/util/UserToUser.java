package my.junw.usercenter.util;

import my.junw.usercenter.entity.UserEO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserToUser {

    UserToUser INSTANCE = Mappers.getMapper( UserToUser.class );

    @Mapping(target = "userPassword", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    UserEO toSafeUser(UserEO s );

}
