package cn.net.ssd.service.common.impl;

import cn.net.ssd.mapper.common.DBInitMapper;
import cn.net.ssd.service.common.IDBInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author SXF
 * @version 1.0
 * @description: TODO
 * @date 2023/5/30 15:05
 */
@Service
public class DBInitServiceImpl implements IDBInitService {
    @Autowired
    private DBInitMapper dbInitMapper;


    @Override
    public void dbInit() {
        dbInitMapper.dbInit();
    }
}
