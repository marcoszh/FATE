package com.webank.ai.fate.board.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExperimentExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ExperimentExample() {
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

        public Criteria andFEidIsNull() {
            addCriterion("f_eid is null");
            return (Criteria) this;
        }

        public Criteria andFEidIsNotNull() {
            addCriterion("f_eid is not null");
            return (Criteria) this;
        }

        public Criteria andFEidEqualTo(Integer value) {
            addCriterion("f_eid =", value, "fEid");
            return (Criteria) this;
        }

        public Criteria andFEidNotEqualTo(Integer value) {
            addCriterion("f_eid <>", value, "fEid");
            return (Criteria) this;
        }

        public Criteria andFEidGreaterThan(Integer value) {
            addCriterion("f_eid >", value, "fEid");
            return (Criteria) this;
        }

        public Criteria andFEidGreaterThanOrEqualTo(Integer value) {
            addCriterion("f_eid >=", value, "fEid");
            return (Criteria) this;
        }

        public Criteria andFEidLessThan(Integer value) {
            addCriterion("f_eid <", value, "fEid");
            return (Criteria) this;
        }

        public Criteria andFEidLessThanOrEqualTo(Integer value) {
            addCriterion("f_eid <=", value, "fEid");
            return (Criteria) this;
        }

        public Criteria andFEidIn(List<Integer> values) {
            addCriterion("f_eid in", values, "fEid");
            return (Criteria) this;
        }

        public Criteria andFEidNotIn(List<Integer> values) {
            addCriterion("f_eid not in", values, "fEid");
            return (Criteria) this;
        }

        public Criteria andFEidBetween(Integer value1, Integer value2) {
            addCriterion("f_eid between", value1, value2, "fEid");
            return (Criteria) this;
        }

        public Criteria andFEidNotBetween(Integer value1, Integer value2) {
            addCriterion("f_eid not between", value1, value2, "fEid");
            return (Criteria) this;
        }

        public Criteria andFPidIsNull() {
            addCriterion("f_pid is null");
            return (Criteria) this;
        }

        public Criteria andFPidIsNotNull() {
            addCriterion("f_pid is not null");
            return (Criteria) this;
        }

        public Criteria andFPidEqualTo(Integer value) {
            addCriterion("f_pid =", value, "fPid");
            return (Criteria) this;
        }

        public Criteria andFPidNotEqualTo(Integer value) {
            addCriterion("f_pid <>", value, "fPid");
            return (Criteria) this;
        }

        public Criteria andFPidGreaterThan(Integer value) {
            addCriterion("f_pid >", value, "fPid");
            return (Criteria) this;
        }

        public Criteria andFPidGreaterThanOrEqualTo(Integer value) {
            addCriterion("f_pid >=", value, "fPid");
            return (Criteria) this;
        }

        public Criteria andFPidLessThan(Integer value) {
            addCriterion("f_pid <", value, "fPid");
            return (Criteria) this;
        }

        public Criteria andFPidLessThanOrEqualTo(Integer value) {
            addCriterion("f_pid <=", value, "fPid");
            return (Criteria) this;
        }

        public Criteria andFPidIn(List<Integer> values) {
            addCriterion("f_pid in", values, "fPid");
            return (Criteria) this;
        }

        public Criteria andFPidNotIn(List<Integer> values) {
            addCriterion("f_pid not in", values, "fPid");
            return (Criteria) this;
        }

        public Criteria andFPidBetween(Integer value1, Integer value2) {
            addCriterion("f_pid between", value1, value2, "fPid");
            return (Criteria) this;
        }

        public Criteria andFPidNotBetween(Integer value1, Integer value2) {
            addCriterion("f_pid not between", value1, value2, "fPid");
            return (Criteria) this;
        }

        public Criteria andFEnameIsNull() {
            addCriterion("f_ename is null");
            return (Criteria) this;
        }

        public Criteria andFEnameIsNotNull() {
            addCriterion("f_ename is not null");
            return (Criteria) this;
        }

        public Criteria andFEnameEqualTo(String value) {
            addCriterion("f_ename =", value, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameNotEqualTo(String value) {
            addCriterion("f_ename <>", value, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameGreaterThan(String value) {
            addCriterion("f_ename >", value, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameGreaterThanOrEqualTo(String value) {
            addCriterion("f_ename >=", value, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameLessThan(String value) {
            addCriterion("f_ename <", value, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameLessThanOrEqualTo(String value) {
            addCriterion("f_ename <=", value, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameLike(String value) {
            addCriterion("f_ename like", value, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameNotLike(String value) {
            addCriterion("f_ename not like", value, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameIn(List<String> values) {
            addCriterion("f_ename in", values, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameNotIn(List<String> values) {
            addCriterion("f_ename not in", values, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameBetween(String value1, String value2) {
            addCriterion("f_ename between", value1, value2, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEnameNotBetween(String value1, String value2) {
            addCriterion("f_ename not between", value1, value2, "fEname");
            return (Criteria) this;
        }

        public Criteria andFEdescIsNull() {
            addCriterion("f_edesc is null");
            return (Criteria) this;
        }

        public Criteria andFEdescIsNotNull() {
            addCriterion("f_edesc is not null");
            return (Criteria) this;
        }

        public Criteria andFEdescEqualTo(String value) {
            addCriterion("f_edesc =", value, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescNotEqualTo(String value) {
            addCriterion("f_edesc <>", value, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescGreaterThan(String value) {
            addCriterion("f_edesc >", value, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescGreaterThanOrEqualTo(String value) {
            addCriterion("f_edesc >=", value, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescLessThan(String value) {
            addCriterion("f_edesc <", value, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescLessThanOrEqualTo(String value) {
            addCriterion("f_edesc <=", value, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescLike(String value) {
            addCriterion("f_edesc like", value, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescNotLike(String value) {
            addCriterion("f_edesc not like", value, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescIn(List<String> values) {
            addCriterion("f_edesc in", values, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescNotIn(List<String> values) {
            addCriterion("f_edesc not in", values, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescBetween(String value1, String value2) {
            addCriterion("f_edesc between", value1, value2, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFEdescNotBetween(String value1, String value2) {
            addCriterion("f_edesc not between", value1, value2, "fEdesc");
            return (Criteria) this;
        }

        public Criteria andFDatasetIsNull() {
            addCriterion("f_dataset is null");
            return (Criteria) this;
        }

        public Criteria andFDatasetIsNotNull() {
            addCriterion("f_dataset is not null");
            return (Criteria) this;
        }

        public Criteria andFDatasetEqualTo(String value) {
            addCriterion("f_dataset =", value, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetNotEqualTo(String value) {
            addCriterion("f_dataset <>", value, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetGreaterThan(String value) {
            addCriterion("f_dataset >", value, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetGreaterThanOrEqualTo(String value) {
            addCriterion("f_dataset >=", value, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetLessThan(String value) {
            addCriterion("f_dataset <", value, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetLessThanOrEqualTo(String value) {
            addCriterion("f_dataset <=", value, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetLike(String value) {
            addCriterion("f_dataset like", value, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetNotLike(String value) {
            addCriterion("f_dataset not like", value, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetIn(List<String> values) {
            addCriterion("f_dataset in", values, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetNotIn(List<String> values) {
            addCriterion("f_dataset not in", values, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetBetween(String value1, String value2) {
            addCriterion("f_dataset between", value1, value2, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFDatasetNotBetween(String value1, String value2) {
            addCriterion("f_dataset not between", value1, value2, "fDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetIsNull() {
            addCriterion("f_partner_dataset is null");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetIsNotNull() {
            addCriterion("f_partner_dataset is not null");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetEqualTo(String value) {
            addCriterion("f_partner_dataset =", value, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetNotEqualTo(String value) {
            addCriterion("f_partner_dataset <>", value, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetGreaterThan(String value) {
            addCriterion("f_partner_dataset >", value, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetGreaterThanOrEqualTo(String value) {
            addCriterion("f_partner_dataset >=", value, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetLessThan(String value) {
            addCriterion("f_partner_dataset <", value, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetLessThanOrEqualTo(String value) {
            addCriterion("f_partner_dataset <=", value, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetLike(String value) {
            addCriterion("f_partner_dataset like", value, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetNotLike(String value) {
            addCriterion("f_partner_dataset not like", value, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetIn(List<String> values) {
            addCriterion("f_partner_dataset in", values, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetNotIn(List<String> values) {
            addCriterion("f_partner_dataset not in", values, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetBetween(String value1, String value2) {
            addCriterion("f_partner_dataset between", value1, value2, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFPartnerDatasetNotBetween(String value1, String value2) {
            addCriterion("f_partner_dataset not between", value1, value2, "fPartnerDataset");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeIsNull() {
            addCriterion("f_create_time is null");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeIsNotNull() {
            addCriterion("f_create_time is not null");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeEqualTo(Date value) {
            addCriterion("f_create_time =", value, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeNotEqualTo(Date value) {
            addCriterion("f_create_time <>", value, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeGreaterThan(Date value) {
            addCriterion("f_create_time >", value, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("f_create_time >=", value, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeLessThan(Date value) {
            addCriterion("f_create_time <", value, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("f_create_time <=", value, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeIn(List<Date> values) {
            addCriterion("f_create_time in", values, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeNotIn(List<Date> values) {
            addCriterion("f_create_time not in", values, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeBetween(Date value1, Date value2) {
            addCriterion("f_create_time between", value1, value2, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("f_create_time not between", value1, value2, "fCreateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeIsNull() {
            addCriterion("f_update_time is null");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeIsNotNull() {
            addCriterion("f_update_time is not null");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeEqualTo(Date value) {
            addCriterion("f_update_time =", value, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeNotEqualTo(Date value) {
            addCriterion("f_update_time <>", value, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeGreaterThan(Date value) {
            addCriterion("f_update_time >", value, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("f_update_time >=", value, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeLessThan(Date value) {
            addCriterion("f_update_time <", value, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("f_update_time <=", value, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeIn(List<Date> values) {
            addCriterion("f_update_time in", values, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeNotIn(List<Date> values) {
            addCriterion("f_update_time not in", values, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("f_update_time between", value1, value2, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("f_update_time not between", value1, value2, "fUpdateTime");
            return (Criteria) this;
        }

        public Criteria andFStatusIsNull() {
            addCriterion("f_status is null");
            return (Criteria) this;
        }

        public Criteria andFStatusIsNotNull() {
            addCriterion("f_status is not null");
            return (Criteria) this;
        }

        public Criteria andFStatusEqualTo(Short value) {
            addCriterion("f_status =", value, "fStatus");
            return (Criteria) this;
        }

        public Criteria andFStatusNotEqualTo(Short value) {
            addCriterion("f_status <>", value, "fStatus");
            return (Criteria) this;
        }

        public Criteria andFStatusGreaterThan(Short value) {
            addCriterion("f_status >", value, "fStatus");
            return (Criteria) this;
        }

        public Criteria andFStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("f_status >=", value, "fStatus");
            return (Criteria) this;
        }

        public Criteria andFStatusLessThan(Short value) {
            addCriterion("f_status <", value, "fStatus");
            return (Criteria) this;
        }

        public Criteria andFStatusLessThanOrEqualTo(Short value) {
            addCriterion("f_status <=", value, "fStatus");
            return (Criteria) this;
        }

        public Criteria andFStatusIn(List<Short> values) {
            addCriterion("f_status in", values, "fStatus");
            return (Criteria) this;
        }

        public Criteria andFStatusNotIn(List<Short> values) {
            addCriterion("f_status not in", values, "fStatus");
            return (Criteria) this;
        }

        public Criteria andFStatusBetween(Short value1, Short value2) {
            addCriterion("f_status between", value1, value2, "fStatus");
            return (Criteria) this;
        }

        public Criteria andFStatusNotBetween(Short value1, Short value2) {
            addCriterion("f_status not between", value1, value2, "fStatus");
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