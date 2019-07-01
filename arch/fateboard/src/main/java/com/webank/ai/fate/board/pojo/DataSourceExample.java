package com.webank.ai.fate.board.pojo;

import java.util.ArrayList;
import java.util.List;

public class DataSourceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DataSourceExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andFIdIsNull() {
            addCriterion("f_id is null");
            return (Criteria) this;
        }

        public Criteria andFIdIsNotNull() {
            addCriterion("f_id is not null");
            return (Criteria) this;
        }

        public Criteria andFIdEqualTo(Integer value) {
            addCriterion("f_id =", value, "fId");
            return (Criteria) this;
        }

        public Criteria andFIdNotEqualTo(Integer value) {
            addCriterion("f_id <>", value, "fId");
            return (Criteria) this;
        }

        public Criteria andFIdGreaterThan(Integer value) {
            addCriterion("f_id >", value, "fId");
            return (Criteria) this;
        }

        public Criteria andFIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("f_id >=", value, "fId");
            return (Criteria) this;
        }

        public Criteria andFIdLessThan(Integer value) {
            addCriterion("f_id <", value, "fId");
            return (Criteria) this;
        }

        public Criteria andFIdLessThanOrEqualTo(Integer value) {
            addCriterion("f_id <=", value, "fId");
            return (Criteria) this;
        }

        public Criteria andFIdIn(List<Integer> values) {
            addCriterion("f_id in", values, "fId");
            return (Criteria) this;
        }

        public Criteria andFIdNotIn(List<Integer> values) {
            addCriterion("f_id not in", values, "fId");
            return (Criteria) this;
        }

        public Criteria andFIdBetween(Integer value1, Integer value2) {
            addCriterion("f_id between", value1, value2, "fId");
            return (Criteria) this;
        }

        public Criteria andFIdNotBetween(Integer value1, Integer value2) {
            addCriterion("f_id not between", value1, value2, "fId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdIsNull() {
            addCriterion("f_party_id is null");
            return (Criteria) this;
        }

        public Criteria andFPartyIdIsNotNull() {
            addCriterion("f_party_id is not null");
            return (Criteria) this;
        }

        public Criteria andFPartyIdEqualTo(String value) {
            addCriterion("f_party_id =", value, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdNotEqualTo(String value) {
            addCriterion("f_party_id <>", value, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdGreaterThan(String value) {
            addCriterion("f_party_id >", value, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdGreaterThanOrEqualTo(String value) {
            addCriterion("f_party_id >=", value, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdLessThan(String value) {
            addCriterion("f_party_id <", value, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdLessThanOrEqualTo(String value) {
            addCriterion("f_party_id <=", value, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdLike(String value) {
            addCriterion("f_party_id like", value, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdNotLike(String value) {
            addCriterion("f_party_id not like", value, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdIn(List<String> values) {
            addCriterion("f_party_id in", values, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdNotIn(List<String> values) {
            addCriterion("f_party_id not in", values, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdBetween(String value1, String value2) {
            addCriterion("f_party_id between", value1, value2, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFPartyIdNotBetween(String value1, String value2) {
            addCriterion("f_party_id not between", value1, value2, "fPartyId");
            return (Criteria) this;
        }

        public Criteria andFLibraryIsNull() {
            addCriterion("f_library is null");
            return (Criteria) this;
        }

        public Criteria andFLibraryIsNotNull() {
            addCriterion("f_library is not null");
            return (Criteria) this;
        }

        public Criteria andFLibraryEqualTo(String value) {
            addCriterion("f_library =", value, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryNotEqualTo(String value) {
            addCriterion("f_library <>", value, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryGreaterThan(String value) {
            addCriterion("f_library >", value, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryGreaterThanOrEqualTo(String value) {
            addCriterion("f_library >=", value, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryLessThan(String value) {
            addCriterion("f_library <", value, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryLessThanOrEqualTo(String value) {
            addCriterion("f_library <=", value, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryLike(String value) {
            addCriterion("f_library like", value, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryNotLike(String value) {
            addCriterion("f_library not like", value, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryIn(List<String> values) {
            addCriterion("f_library in", values, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryNotIn(List<String> values) {
            addCriterion("f_library not in", values, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryBetween(String value1, String value2) {
            addCriterion("f_library between", value1, value2, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFLibraryNotBetween(String value1, String value2) {
            addCriterion("f_library not between", value1, value2, "fLibrary");
            return (Criteria) this;
        }

        public Criteria andFNameIsNull() {
            addCriterion("f_name is null");
            return (Criteria) this;
        }

        public Criteria andFNameIsNotNull() {
            addCriterion("f_name is not null");
            return (Criteria) this;
        }

        public Criteria andFNameEqualTo(String value) {
            addCriterion("f_name =", value, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameNotEqualTo(String value) {
            addCriterion("f_name <>", value, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameGreaterThan(String value) {
            addCriterion("f_name >", value, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameGreaterThanOrEqualTo(String value) {
            addCriterion("f_name >=", value, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameLessThan(String value) {
            addCriterion("f_name <", value, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameLessThanOrEqualTo(String value) {
            addCriterion("f_name <=", value, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameLike(String value) {
            addCriterion("f_name like", value, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameNotLike(String value) {
            addCriterion("f_name not like", value, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameIn(List<String> values) {
            addCriterion("f_name in", values, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameNotIn(List<String> values) {
            addCriterion("f_name not in", values, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameBetween(String value1, String value2) {
            addCriterion("f_name between", value1, value2, "fName");
            return (Criteria) this;
        }

        public Criteria andFNameNotBetween(String value1, String value2) {
            addCriterion("f_name not between", value1, value2, "fName");
            return (Criteria) this;
        }

        public Criteria andFDataTypeIsNull() {
            addCriterion("f_data_type is null");
            return (Criteria) this;
        }

        public Criteria andFDataTypeIsNotNull() {
            addCriterion("f_data_type is not null");
            return (Criteria) this;
        }

        public Criteria andFDataTypeEqualTo(String value) {
            addCriterion("f_data_type =", value, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeNotEqualTo(String value) {
            addCriterion("f_data_type <>", value, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeGreaterThan(String value) {
            addCriterion("f_data_type >", value, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeGreaterThanOrEqualTo(String value) {
            addCriterion("f_data_type >=", value, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeLessThan(String value) {
            addCriterion("f_data_type <", value, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeLessThanOrEqualTo(String value) {
            addCriterion("f_data_type <=", value, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeLike(String value) {
            addCriterion("f_data_type like", value, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeNotLike(String value) {
            addCriterion("f_data_type not like", value, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeIn(List<String> values) {
            addCriterion("f_data_type in", values, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeNotIn(List<String> values) {
            addCriterion("f_data_type not in", values, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeBetween(String value1, String value2) {
            addCriterion("f_data_type between", value1, value2, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFDataTypeNotBetween(String value1, String value2) {
            addCriterion("f_data_type not between", value1, value2, "fDataType");
            return (Criteria) this;
        }

        public Criteria andFSizeIsNull() {
            addCriterion("f_size is null");
            return (Criteria) this;
        }

        public Criteria andFSizeIsNotNull() {
            addCriterion("f_size is not null");
            return (Criteria) this;
        }

        public Criteria andFSizeEqualTo(Integer value) {
            addCriterion("f_size =", value, "fSize");
            return (Criteria) this;
        }

        public Criteria andFSizeNotEqualTo(Integer value) {
            addCriterion("f_size <>", value, "fSize");
            return (Criteria) this;
        }

        public Criteria andFSizeGreaterThan(Integer value) {
            addCriterion("f_size >", value, "fSize");
            return (Criteria) this;
        }

        public Criteria andFSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("f_size >=", value, "fSize");
            return (Criteria) this;
        }

        public Criteria andFSizeLessThan(Integer value) {
            addCriterion("f_size <", value, "fSize");
            return (Criteria) this;
        }

        public Criteria andFSizeLessThanOrEqualTo(Integer value) {
            addCriterion("f_size <=", value, "fSize");
            return (Criteria) this;
        }

        public Criteria andFSizeIn(List<Integer> values) {
            addCriterion("f_size in", values, "fSize");
            return (Criteria) this;
        }

        public Criteria andFSizeNotIn(List<Integer> values) {
            addCriterion("f_size not in", values, "fSize");
            return (Criteria) this;
        }

        public Criteria andFSizeBetween(Integer value1, Integer value2) {
            addCriterion("f_size between", value1, value2, "fSize");
            return (Criteria) this;
        }

        public Criteria andFSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("f_size not between", value1, value2, "fSize");
            return (Criteria) this;
        }

        public Criteria andFRowNumIsNull() {
            addCriterion("f_row_num is null");
            return (Criteria) this;
        }

        public Criteria andFRowNumIsNotNull() {
            addCriterion("f_row_num is not null");
            return (Criteria) this;
        }

        public Criteria andFRowNumEqualTo(Integer value) {
            addCriterion("f_row_num =", value, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFRowNumNotEqualTo(Integer value) {
            addCriterion("f_row_num <>", value, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFRowNumGreaterThan(Integer value) {
            addCriterion("f_row_num >", value, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFRowNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("f_row_num >=", value, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFRowNumLessThan(Integer value) {
            addCriterion("f_row_num <", value, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFRowNumLessThanOrEqualTo(Integer value) {
            addCriterion("f_row_num <=", value, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFRowNumIn(List<Integer> values) {
            addCriterion("f_row_num in", values, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFRowNumNotIn(List<Integer> values) {
            addCriterion("f_row_num not in", values, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFRowNumBetween(Integer value1, Integer value2) {
            addCriterion("f_row_num between", value1, value2, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFRowNumNotBetween(Integer value1, Integer value2) {
            addCriterion("f_row_num not between", value1, value2, "fRowNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumIsNull() {
            addCriterion("f_column_num is null");
            return (Criteria) this;
        }

        public Criteria andFColumnNumIsNotNull() {
            addCriterion("f_column_num is not null");
            return (Criteria) this;
        }

        public Criteria andFColumnNumEqualTo(Integer value) {
            addCriterion("f_column_num =", value, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumNotEqualTo(Integer value) {
            addCriterion("f_column_num <>", value, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumGreaterThan(Integer value) {
            addCriterion("f_column_num >", value, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("f_column_num >=", value, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumLessThan(Integer value) {
            addCriterion("f_column_num <", value, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumLessThanOrEqualTo(Integer value) {
            addCriterion("f_column_num <=", value, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumIn(List<Integer> values) {
            addCriterion("f_column_num in", values, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumNotIn(List<Integer> values) {
            addCriterion("f_column_num not in", values, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumBetween(Integer value1, Integer value2) {
            addCriterion("f_column_num between", value1, value2, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFColumnNumNotBetween(Integer value1, Integer value2) {
            addCriterion("f_column_num not between", value1, value2, "fColumnNum");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeIsNull() {
            addCriterion("f_upload_time is null");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeIsNotNull() {
            addCriterion("f_upload_time is not null");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeEqualTo(Integer value) {
            addCriterion("f_upload_time =", value, "fUploadTime");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeNotEqualTo(Integer value) {
            addCriterion("f_upload_time <>", value, "fUploadTime");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeGreaterThan(Integer value) {
            addCriterion("f_upload_time >", value, "fUploadTime");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeGreaterThanOrEqualTo(Integer value) {
            addCriterion("f_upload_time >=", value, "fUploadTime");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeLessThan(Integer value) {
            addCriterion("f_upload_time <", value, "fUploadTime");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeLessThanOrEqualTo(Integer value) {
            addCriterion("f_upload_time <=", value, "fUploadTime");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeIn(List<Integer> values) {
            addCriterion("f_upload_time in", values, "fUploadTime");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeNotIn(List<Integer> values) {
            addCriterion("f_upload_time not in", values, "fUploadTime");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeBetween(Integer value1, Integer value2) {
            addCriterion("f_upload_time between", value1, value2, "fUploadTime");
            return (Criteria) this;
        }

        public Criteria andFUploadTimeNotBetween(Integer value1, Integer value2) {
            addCriterion("f_upload_time not between", value1, value2, "fUploadTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}