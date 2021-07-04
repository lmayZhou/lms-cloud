package com.lmaye.cloud.starter.canal.factory;

import com.lmaye.cloud.starter.canal.utils.EntryUtil;
import com.lmaye.cloud.starter.canal.utils.FieldUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * -- MapColumnModelFactory
 *
 * @author Lmay Zhou
 * @date 2021/3/22 11:27
 * @email lmay@lmaye.com
 */
public class MapColumnModelFactory extends AbstractModelFactory<Map<String, String>> {
    @Override
    <R> R newInstance(Class<R> c, Map<String, String> valueMap) throws Exception {
        R object = c.newInstance();
        Map<String, String> columnNames = EntryUtil.getFieldName(object.getClass());
        for (Map.Entry<String, String> entry : valueMap.entrySet()) {
            String fieldName = columnNames.get(entry.getKey());
            if (StringUtils.isNotEmpty(fieldName)) {
                FieldUtil.setFieldValue(object, fieldName, entry.getValue());
            }
        }
        return object;
    }
}
