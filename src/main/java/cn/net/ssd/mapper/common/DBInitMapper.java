package cn.net.ssd.mapper.common;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DBInitMapper {
    @Select("SELECT 1")
    int dbInit();
}
