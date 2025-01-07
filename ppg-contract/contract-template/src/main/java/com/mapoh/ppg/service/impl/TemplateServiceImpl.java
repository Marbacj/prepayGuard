package com.mapoh.ppg.service.impl;

import com.mapoh.ppg.dao.ContractTemplateDao;
import com.mapoh.ppg.dto.TemplateRequest;
import com.mapoh.ppg.entity.ContractTemplate;
import com.mapoh.ppg.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mabohv
 * @date 2025/1/7 22:47
 */

@Service
public class TemplateServiceImpl implements TemplateService {

    ContractTemplateDao contractTemplateDao;

    @Autowired
    public TemplateServiceImpl(ContractTemplateDao contractTemplateDao) {
        this.contractTemplateDao = contractTemplateDao;
    }

    @Override
    public String buildTemplate(TemplateRequest request) {
        if(request == null) {
            return "the request is null";
        }
        String name = request.getName();
        String description = request.getDescription();
        String fields = request.getFields();

        boolean flag = contractTemplateDao.existsByName(name);
        if(flag) {
            return "the template name already exists";
        }
        contractTemplateDao.save(new ContractTemplate(name, description, fields));
        return "success";
    }


}
