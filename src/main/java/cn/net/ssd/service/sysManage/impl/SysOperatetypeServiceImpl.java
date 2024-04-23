package cn.net.ssd.service.sysManage.impl;

import cn.net.ssd.mapper.sysManage.SysOperatetypeMapper;
import cn.net.ssd.model.sysManage.SysOperatetype;
import cn.net.ssd.service.sysManage.ISysOperatetypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SysOperatetypeServiceImpl implements ISysOperatetypeService {
    @Autowired
    private SysOperatetypeMapper sysOperatetypeMapper;

    private static final Logger logger = LoggerFactory.getLogger(SysOperatetypeServiceImpl.class);

    @Override
    public SysOperatetype selectByPrimaryKey(Long id) {
        return this.sysOperatetypeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int deleteByPrimaryKey(Long id) {
        return this.sysOperatetypeMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SysOperatetype record) {
        return this.sysOperatetypeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysOperatetype record) {
        return this.sysOperatetypeMapper.updateByPrimaryKey(record);
    }

    @Override
    public int insert(SysOperatetype record) {
        return this.sysOperatetypeMapper.insert(record);
    }

    @Override
    public int insertSelective(SysOperatetype record) {
        return this.sysOperatetypeMapper.insertSelective(record);
    }

    @Override
    public int insertBatch(List<SysOperatetype> operatetypeList) {
        return this.sysOperatetypeMapper.insertBatch(operatetypeList);
    }

    @Override
    public int insertBatchSelective(List<SysOperatetype> operatetypeList) {
        return this.sysOperatetypeMapper.insertBatchSelective(operatetypeList);
    }

    @Override
    public int updateBatchByPrimaryKey(List<SysOperatetype> operatetypeList) {
        return this.sysOperatetypeMapper.updateBatchByPrimaryKey(operatetypeList);
    }

    @Override
    public int updateBatchByPrimaryKeySelective(List<SysOperatetype> operatetypeList) {
        return this.sysOperatetypeMapper.updateBatchByPrimaryKeySelective(operatetypeList);
    }

    @Override
    public List<SysOperatetype> selectByParams(Map<String, Object> paramMap) {
        return this.sysOperatetypeMapper.selectByParams(paramMap);
    }

    @Override
    public List<Long> selectOperatetypeidListByRoleid(Map<String, Object> paramMap) {
        return this.sysOperatetypeMapper.selectOperatetypeidListByRoleid(paramMap);
    }
}