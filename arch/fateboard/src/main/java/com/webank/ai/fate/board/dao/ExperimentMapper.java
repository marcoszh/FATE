package com.webank.ai.fate.board.dao;

import com.webank.ai.fate.board.pojo.Experiment;
import com.webank.ai.fate.board.pojo.ExperimentExample;
import com.webank.ai.fate.board.pojo.ExperimentWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExperimentMapper {
    long countByExample(ExperimentExample example);

    int deleteByExample(ExperimentExample example);

    int deleteByPrimaryKey(Integer fEid);

    int insert(ExperimentWithBLOBs record);

    int insertSelective(ExperimentWithBLOBs record);

    List<ExperimentWithBLOBs> selectByExampleWithBLOBs(ExperimentExample example);

    List<Experiment> selectByExample(ExperimentExample example);

    ExperimentWithBLOBs selectByPrimaryKey(Integer fEid);

    int updateByExampleSelective(@Param("record") ExperimentWithBLOBs record, @Param("example") ExperimentExample example);

    int updateByExampleWithBLOBs(@Param("record") ExperimentWithBLOBs record, @Param("example") ExperimentExample example);

    int updateByExample(@Param("record") Experiment record, @Param("example") ExperimentExample example);

    int updateByPrimaryKeySelective(ExperimentWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(ExperimentWithBLOBs record);

    int updateByPrimaryKey(Experiment record);
}