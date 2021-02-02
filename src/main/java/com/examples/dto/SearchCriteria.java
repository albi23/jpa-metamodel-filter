package com.examples.dto;

import com.examples.enums.OperationSign;

import java.util.HashMap;
import java.util.Map;

public class SearchCriteria {

    private String classFilterField;
    private Map<OperationSign, Object> criteriaMap = new HashMap<>();

    public SearchCriteria(String classFilterField, Map<OperationSign, Object> criteriaMap) {
        this.classFilterField = classFilterField;
        this.criteriaMap = criteriaMap;
    }

    /**
     * Gets classFilterField
     *
     * @return value of classFilterField field
     */
    public String getClassFilterField() {
        return classFilterField;
    }

    /**
     * Sets <code>SearchCriteria</code> classFilterField value
     *
     * @param classFilterField - set new value of classFilterField
     */
    public void setClassFilterField(String classFilterField) {
        this.classFilterField = classFilterField;
    }

    /**
     * Gets criteriaMap
     *
     * @return value of criteriaMap field
     */
    public Map<OperationSign, Object> getCriteriaMap() {
        return criteriaMap;
    }

    /**
     * Sets <code>SearchCriteria</code> criteriaMap value
     *
     * @param criteriaMap - set new value of criteriaMap
     */
    public void setCriteriaMap(Map<OperationSign, Object> criteriaMap) {
        this.criteriaMap = criteriaMap;
    }
}
