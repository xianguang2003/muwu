/*
 * Copyright (c) 2021 fanmu All Rights Reserved.
 * 
 * 创建人：mumu
 * 联系方式：fanmu_projects@163.com
 * 开源地址: https://github.com/fanmu-projects
 */
package com.fanmu.muwu.service.provider.user.api.model.view.dictitem;

import com.fanmu.muwu.service.provider.user.api.model.dto.dictitem.DictItemLabelValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DictItemLabelValueSerializer extends JsonSerializer<DictItemLabelValue> {

    @Override
    public void serialize(DictItemLabelValue dictItemLabelValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("label", dictItemLabelValue.getItemText());
        if (dictItemLabelValue.getType() == 0) {
            jsonGenerator.writeStringField("value", dictItemLabelValue.getItemValue());
        } else {
            jsonGenerator.writeNumberField("value", Long.parseLong(dictItemLabelValue.getItemValue()));
        }
//        jsonGenerator.writeNumberField("type", dictItemLabelValue.getType());
//        jsonGenerator.writeNumberField("number", dictItemLabelValue.getNumber());
        jsonGenerator.writeEndObject();
    }
}
