package com.webank.ai.fate.board.dao;

import com.webank.ai.fate.board.pojo.ExperimentLib;
import com.webank.ai.fate.board.pojo.ExperimentLibExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExperimentLibMapper {
    long countByExample(ExperimentLibExample example);

    int deleteByExample(ExperimentLibExample example);

    int deleteByPrimaryKey(Integer fId);

    int insert(ExperimentLib record);

    int insertSelective(ExperimentLib record);

    List<ExperimentLib> selectByExample(ExperimentLibExample example);

    ExperimentLib selectByPrimaryKey(Integer fId);

    int updateByExampleSelective(@Param("record") ExperimentLib record, @Param("example") ExperimentLibExample example);

    int updateByExample(@Param("record") ExperimentLib record, @Param("example") ExperimentLibExample example);

    int updateByPrimaryKeySelective(ExperimentLib record);

    int updateByPrimaryKey(ExperimentLib record);
}