package com.examples.dto;

import java.util.List;

public class SearchCriteriaForClass {

    private String sortBy ;
    private String filteredClass ;
    private Boolean ascending ;
    private List<SearchCriteria> searchCriteria;

    public SearchCriteriaForClass() {
    }

    /**
     * Gets filteredClass
     *
     * @return value of filteredClass field
     */
    public String getFilteredClass() {
        return filteredClass;
    }

    /**
     * Sets <code>SearchCriteriaForClass</code> filteredClass value
     *
     * @param filteredClass - set new value of filteredClass
     */
    public void setFilteredClass(String filteredClass) {
        this.filteredClass = filteredClass;
    }

    /**
     * Gets sortBy
     *
     * @return value of sortBy field
     */
    public String getSortBy() {
        return sortBy;
    }

    /**
     * Sets <code>SearchCriteriaForClass</code> sortBy value
     *
     * @param sortBy - set new value of sortBy
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Gets ascending
     *
     * @return value of ascending field
     */
    public Boolean getAscending() {
        return ascending;
    }

    /**
     * Sets <code>SearchCriteriaForClass</code> ascending value
     *
     * @param ascending - set new value of ascending
     */
    public void setAscending(Boolean ascending) {
        this.ascending = ascending;
    }

    /**
     * Gets searchCriteria
     *
     * @return value of searchCriteria field
     */
    public List<SearchCriteria> getSearchCriteria() {
        return searchCriteria;
    }

    /**
     * Sets <code>SearchCriteriaForClass</code> searchCriteria value
     *
     * @param searchCriteria - set new value of searchCriteria
     */
    public void setSearchCriteria(List<SearchCriteria> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
}
