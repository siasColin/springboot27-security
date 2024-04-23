package cn.net.ssd.common.init;

import cn.net.ssd.common.util.IdWorker;
import cn.net.ssd.common.util.JwtUtils;
import cn.net.ssd.config.RsaKeyProperties;
import cn.net.ssd.model.sysManage.SysUser;
import cn.net.ssd.service.common.IDBInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Package: cn.net.ssd.task.common
 * @Author: sxf
 * @Date: 2020-10-14
 * @Description: 项目启动后执行一些初始化任务
 */
@Component
public class Init implements ApplicationRunner {
    @Value("${snowflake.workerId:0}")
    private long workerId;
    @Autowired
    private IDBInitService dbInitService;

    @Autowired
    private RsaKeyProperties prop;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //初始化id生成器
        IdWorker.init(workerId);
        JwtUtils.generateTokenExpireInSeconds(new SysUser(), prop.getPrivateKey(), 3);
        dbInitService.dbInit();
        redisInit();
    }

    /**
     * @description: TODO 连接池预热
     * @author SXF
     * @date 2023/5/30 16:20
     * @version 1.0
     */
    public void redisInit() {
        // 获取 Redis 连接
        RedisConnection connection = null;
        try {
            connection = redisTemplate.getConnectionFactory().getConnection();
            // 执行一些无关紧要的操作
            String ping = connection.ping();
        } finally {
            if (connection != null) {
                // 关闭连接，归还到连接池
                connection.close();
                connection = null;
            }
        }
    }
}
