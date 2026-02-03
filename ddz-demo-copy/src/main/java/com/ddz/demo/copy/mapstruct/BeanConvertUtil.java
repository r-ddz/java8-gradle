package com.ddz.demo.copy.mapstruct;

import com.ddz.demo.core.model.data.UserDemo;
import com.ddz.demo.core.model.dto.UserDTO;
import com.ddz.demo.core.model.info.UserInfo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BeanConvertUtil {

    UserDTO copy(UserDemo userDemo);

    List<UserInfo> copy(List<UserDemo> userDemos);

}
