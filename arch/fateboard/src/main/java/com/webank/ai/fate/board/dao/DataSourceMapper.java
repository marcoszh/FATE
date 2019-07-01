package com.webank.ai.fate.board.dao;

import com.webank.ai.fate.board.pojo.DataSource;
import com.webank.ai.fate.board.pojo.DataSourceExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataSourceMapper {
    long countByExample(DataSourceExample example);

    int deleteByExample(DataSourceExample example);

    int deleteByPrimaryKey(Integer fId);

    int insert(DataSource record);

    int insertSelective(DataSource record);

    List<DataSource> selectByExample(DataSourceExample example);

    DataSource selectByPrimaryKey(Integer fId);

    int updateByExampleSelective(@Param("record") DataSource record, @Param("example") DataSourceExample example);

    int updateByExample(@Param("record") DataSource record, @Param("example") DataSourceExample example);

    int updateByPrimaryKeySelective(DataSource record);

    int updateByPrimaryKey(DataSource record);
}