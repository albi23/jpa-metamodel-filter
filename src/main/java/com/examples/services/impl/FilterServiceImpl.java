package com.examples.services.impl;

import com.examples.model.BaseEntity;
import com.examples.services.api.FilterService;
import org.springframework.stereotype.Service;

@Service
public class FilterServiceImpl implements FilterService {

    @Override
    public String getModelPackageName() {
        return BaseEntity.class.getPackageName();
    }
}
