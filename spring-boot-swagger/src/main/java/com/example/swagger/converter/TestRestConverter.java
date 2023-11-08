package com.example.swagger.converter;

import com.example.swagger.entity.Test;
import com.example.swagger.entity.TestDTO;
import com.example.swagger.entity.TestVO;
import com.lmaye.cloud.starter.web.service.IRestConverter;
import org.mapstruct.Mapper;

/**
 * -- ActivityRestConverter
 *
 * @author Lmay Zhou
 * @date 2023/9/7 22:59
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Mapper(componentModel = "spring")
public interface TestRestConverter extends IRestConverter<Test, TestVO, TestDTO> {

}
