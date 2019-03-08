package com.wemarket.wac.common;

import com.alibaba.druid.pool.DruidDataSource;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wemarket.wac.common.cache.BaseGuavaCacheManager;
import com.wemarket.wac.common.exception.SysException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Public initialization class
 **/
@Configuration
@EnableTransactionManagement
@ComponentScan
@EnableScheduling
@EnableAsync
@EnableCaching
public class GlobalConfiguration extends WebMvcConfigurerAdapter implements SchedulingConfigurer {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalConfiguration.class);

    @Value("${wac.taskexecutor.corepoolsize}")
    private int corePoolSize;
    @Value("${wac.taskexecutor.keepaliveseconds}")
    private int keepAliveSeconds;
    @Value("${wac.taskexecutor.maxpoolsize}")
    private int maxPoolSize;
    @Value("${wac.taskexecutor.queuecapacity}")
    private int queueCapacity;
    @Value("${wac.front.taskexecutor.allowcorethreadtimeout}")
    private boolean allowCoreThreadTimeOut;
    @Value("${wac.front.taskexecutor.awaitterminationseconds}")
    private int awaitTerminationSeconds;
    @Value("${wac.front.taskexecutor.waitfortaskstocompleteonshutdown}")
    private boolean waitForTasksToCompleteOnShutdown;

    @Value("${jdbc.wac.url}")
    private String jdbcUrl;
    @Value("${jdbc.wac.user}")
    private String jdbcUsername;
    @Value("${jdbc.wac.password}")
    private String jdbcPassword;
    @Value("${jdbc.wac.initialSize}")
    private int jdbcInitialSize;
    @Value("${jdbc.wac.minIdle}")
    private int jdbcMinIdle;
    @Value("${jdbc.wac.maxActive}")
    private int jdbcMaxActive;
    @Value("${jdbc.wac.maxWait}")
    private int jdbcMaxWait;
    @Value("${jdbc.wac.timeBetweenEvictionRunsMillis}")
    private int jdbcTimeBetweenEvictionRunsMillis;
    @Value("${jdbc.wac.minEvictableIdleTimeMillis}")
    private int jdbcMinEvictableIdleTimeMillis;
    @Value("${jdbc.wac.validationQuery}")
    private String jdbcValidationQuery;
    @Value("${jdbc.wac.testWhileIdle}")
    private boolean jdbcTestWhileIdle;
    @Value("${jdbc.wac.testOnBorrow}")
    private boolean jdbcTestOnBorrow;
    @Value("${jdbc.wac.testOnReturn}")
    private boolean jdbcTestOnReturn;

    @Value("${redis.maxTotal:200}")
    private int maxTotal;
    @Value("${redis.maxIdle:100}")
    private int maxIdle;
    @Value("${redis.minIdle:8}")
    private int minIdle;
    @Value("${redis.maxActive:300}")
    private int maxActive;
    @Value("${redis.maxWaitMillis:100000}")
    private long maxWaitMillis;
    @Value("${redis.testOnBorrow:true}")
    private boolean testOnBorrow;
    @Value("${redis.testOnReturn:true}")
    private boolean testOnReturn;
    @Value("${redis.testWhileIdle:true}")
    private boolean testWhileIdle;
    @Value("${redis.timeBetweenEvictionRunsMillis:30000}")
    private long timeBetweenEvictionRunsMillis;
    @Value("${redis.numTestsPerEvictionRun:10}")
    private int numTestsPerEvictionRun;
    @Value("${redis.minEvictableIdleTimeMillis:60000}")
    private long minEvictableIdleTimeMillis;
    @Value("${redis.timeout:100000}")
    private int timeout;
    @Value("${redis.sentinel.nodes}")
    private String nodes;
    @Value("${redis.sentinel.master}")
    private String masterName;
    @Value("${redis.password}")
    private String password;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar){
        taskRegistrar.setScheduler(setTaskScheduler());
    }

    @Bean(name = "taskExecutor")
    public ThreadPoolTaskExecutor setTaskExecutor(){
        return getTask();
    }

    @Bean(name = "taskScheduler")
    public ThreadPoolTaskScheduler setTaskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(maxPoolSize);
        return scheduler;
    }

    private ThreadPoolTaskExecutor getTask() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setQueueCapacity(queueCapacity);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAllowCoreThreadTimeOut(true);
        return executor;
    }

    @Bean(name = "frontTaskExecutor")
    public ThreadPoolTaskExecutor setFrontTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    @Bean(name = "transactionManager")
    public DataSourceTransactionManager setTransactionManager() {
        DataSourceTransactionManager txManager =
                new DataSourceTransactionManager();
        txManager.setDataSource(setDataSource());

        return txManager;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory setSqlSessionFactory() throws IOException {
        SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
        factory.setDataSource(setDataSource());

        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        factory.setConfigLocation(resourceLoader.getResource("classpath:mybatis-config.xml"));
        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver =
                new PathMatchingResourcePatternResolver();
        String packageSearchPath = "classpath*:mapping/wac_*DAO.xml";
        factory.setMapperLocations(pathMatchingResourcePatternResolver.getResources(packageSearchPath));
        try{
            return factory.getObject();
        } catch (Exception e) {
            throw new SysException("SqlSessionFactory construct fail", e);
        }
    }

    @Bean(name = "dataSource", initMethod = "init", destroyMethod = "close")
    public DruidDataSource setDataSource() {
        try {
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(jdbcUrl);
            dataSource.setUsername(jdbcUsername);
            dataSource.setPassword(jdbcPassword);
            dataSource.setInitialSize(jdbcInitialSize);
            dataSource.setMinIdle(jdbcMinIdle);
            dataSource.setMaxActive(jdbcMaxActive);
            dataSource.setMaxWait(jdbcMaxWait);
            dataSource.setTimeBetweenEvictionRunsMillis(jdbcTimeBetweenEvictionRunsMillis);
            dataSource.setMinEvictableIdleTimeMillis(jdbcMinEvictableIdleTimeMillis);
            dataSource.setValidationQuery(jdbcValidationQuery);
            dataSource.setTestWhileIdle(jdbcTestWhileIdle);
            dataSource.setTestOnBorrow(jdbcTestOnBorrow);
            dataSource.setTestOnReturn(jdbcTestOnReturn);
            dataSource.setFilters("stat, slf4j");

            return dataSource;
        } catch (Exception e) {
            throw new SysException("Wac datasource construct failed", e);
        }
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //represent the specified number of megabytes
        factory.setMaxFileSize("5MB");
        factory.setMaxRequestSize("5MB");
        return factory.createMultipartConfig();
    }

    @Bean(name = "messageSource")
    public MessageSource setMessageSource() {
        ReloadableResourceBundleMessageSource messageSource =
                new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600);
        return messageSource;
    }

    @Bean("jedisPoolConfig")
    public JedisPoolConfig initJedisPoolConfig(){
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestWhileIdle(testWhileIdle);
        poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
        poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        return poolConfig;
    }

    @Bean(value = "sentinelPool")
    public JedisSentinelPool initJedisPool(
            @Qualifier(value = "jedisPoolConfig") JedisPoolConfig jedisPoolConfig
    ){
        Set<String> nodeSet = new HashSet<>();
        //判断字符串是否为空
        if(nodes == null || "".equals(nodes)){
            LOGGER.error("RedisSentinelConfiguration initialize error nodeString is null");
            throw new RuntimeException("RedisSentinelConfiguration initialize error nodeString is null");
        }
        String[] nodeArray = nodes.split(",");
        //判断是否为空
        if(nodeArray == null || nodeArray.length == 0){
            LOGGER.error("RedisSentinelConfiguration initialize error nodeArray is null");
            throw new RuntimeException("RedisSentinelConfiguration initialize error nodeArray is null");
        }
        //循环注入至Set中
        for(String node : nodeArray){
            nodeSet.add(node);
        }
        //创建连接池对象
        JedisSentinelPool jedisPool =
                new JedisSentinelPool(masterName ,nodeSet ,jedisPoolConfig ,timeout , password);
        return jedisPool;
    }

    @Bean(name = "cacheManager")
    public BaseGuavaCacheManager getCacheManager(@Value("${wac.guava.cache.config}")
                                                 String guavaCacheConfig) {
        Map<String, String> wacCacheConfig = new HashMap<>();
        wacCacheConfig.put("wacCacheConfig", guavaCacheConfig);
        BaseGuavaCacheManager bgCacheManager =
                new BaseGuavaCacheManager(wacCacheConfig);
        return bgCacheManager;
    }
}
