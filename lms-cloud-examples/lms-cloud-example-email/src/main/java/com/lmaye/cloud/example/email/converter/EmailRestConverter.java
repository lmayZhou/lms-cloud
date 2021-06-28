package com.lmaye.cloud.example.email.converter;

import com.lmaye.cloud.example.email.dto.EmailDTO;
import com.lmaye.cloud.example.email.vo.EmailVO;
import com.lmaye.cloud.starter.email.entity.Email;
import com.lmaye.cloud.starter.web.service.IRestConverter;
import org.mapstruct.Mapper;

/**
 * -- Email Rest Converter
 *
 * @author lmay.Zhou
 * @date 2021/6/29 01:53
 * @email lmay@lmaye.com
 * @since JDK1.8
 */
@Mapper(componentModel = "spring")
public interface EmailRestConverter extends IRestConverter<Email, EmailVO, EmailDTO> {
}
