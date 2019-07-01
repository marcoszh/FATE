package com.webank.ai.fate.board.services;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.webank.ai.fate.board.dao.DataSourceMapper;
import com.webank.ai.fate.board.pojo.DataSource;
import com.webank.ai.fate.board.pojo.DataSourceExample;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Description TODO
 * @Author kaideng
 **/
@Component
public class DataSourceService {

    DataSourceMapper dataSourceMapper;

    public Map listDatasets() {

        Map libTemp = Maps.newHashMap();

        DataSourceExample dataSourceExample = new DataSourceExample();

        List<DataSource> lists = dataSourceMapper.selectByExample(dataSourceExample);

        for (DataSource dataSource : lists) {

            String lib = dataSource.getfLibrary();
            if (libTemp.get(lib) == null) {
                libTemp.put(lib, Lists.newArrayList());

            }
            ((List) libTemp.get(lib)).add(dataSource);


            /**
             * 还需要添加是否勾选的选项
             */
        }
        return libTemp;
    }


}
