package com.fans.quartz;

import com.fans.common.CommonConstants;
import com.fans.utils.JsonUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.Method;
import java.util.List;


/**
 * className: DynamicJob
 *
 * @author k
 * @version 1.0
 * @description 调度中心执行逻辑的Job
 * @description @DisallowConcurrentExecution : 此标记用在实现Job的类上面,意思是不允许并发执行
 * @description 注意org.quartz.threadPool.threadCount线程池中线程的数量至少要多个
 * @description 否则@DisallowConcurrentExecution不生效
 * @description 假如Job的设置时间间隔为3秒, 但Job执行时间是5秒
 * @description 设置@DisallowConcurrentExecution以后程序会等任务执行完毕以后再去执行
 * @description 否则会在3秒时再启用新的线程执行
 * @date 2018-12-20 14:14
 **/
@DisallowConcurrentExecution
@Component
@Slf4j
public class DynamicJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        //JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        String jobType = map.getString("jobType");
        if (StringUtils.isNotBlank(jobType)) {
            if (StringUtils.equals(jobType, CommonConstants.JAR_JOB)) {
                executeJarJob(map);
            } else {
                executeClassJob(map);
            }
        } else {
            log.error("quartz--> Job Type Is Empty");
        }
    }

    /**
     * description: 执行class类型job
     *
     * @param map 任务入参
     * @author k
     * @date 2018/12/23 14:07
     **/
    private void executeClassJob(JobDataMap map) {
        String className = map.getString("className");
        String parameter = map.getString("parameter");
        String classMethod = map.getString("classMethod");
        printLog(map);
        log.info("quartz--> Running Job ClassName   : {}", className);
        log.info("quartz--> Running Job Parameter   : {}", parameter);
        log.info("quartz--> Running Job ClassMethod   : {}", classMethod);
        long startTime = System.currentTimeMillis();
        log.info("quartz--> Running Job Details As Follows >>>>>>>>>>>>>>>>>>>>: ");
        Class objectClass;
        try {
            objectClass = Class.forName(className);
            Method[] methods = objectClass.getDeclaredMethods();
            boolean isHas = false;
            for (Method method : methods) {
                if (StringUtils.equals(method.getName(), classMethod)) {
                    isHas = true;
                    if (StringUtils.isBlank(parameter)) {
                        method.invoke(objectClass.newInstance());
                    } else {
                        Object[] params = parameter.split(",");
                        method.invoke(objectClass.newInstance(), params);
                    }
                }
            }
            if (!isHas) {
                log.error("quartz--> Job Class Method Is Not Found !");
            }
            long endTime = System.currentTimeMillis();
            log.info("quartz--> >>>>>>>>>>>>> Running Job has been completed , cost time :  " + (endTime - startTime) + "ms\n");
        } catch (Exception e) {
            log.error("quartz--> Job Running Error", e);
            long endTime = System.currentTimeMillis();
            log.info("quartz--> >>>>>>>>>>>>> Running Job has been completed , cost time :  " + (endTime - startTime) + "ms\n");
        }
    }

    /**
     * description: 执行jar类型job
     *
     * @param map 任务入参
     * @author k
     * @date 2018/12/23 14:07
     **/
    private void executeJarJob(JobDataMap map) {
        String jarPath = map.getString("jarPath");
        String parameter = map.getString("parameter");
        String vmParam = map.getString("vmParam");
        printLog(map);
        log.info("quartz--> Running Job JarPath   : {}", jarPath);
        log.info("quartz--> Running Job Parameter   : {}", parameter);
        log.info("quartz--> Running Job VmParam   : {}", vmParam);
        long startTime = System.currentTimeMillis();
        log.info("quartz--> Running Job Details As Follows >>>>>>>>>>>>>>>>>>>>: ");
        if (StringUtils.isNotBlank(jarPath)) {
            File jar = new File(jarPath);
            if (jar.exists()) {
                ProcessBuilder processBuilder = new ProcessBuilder();
                processBuilder.directory(jar.getParentFile());
                List<String> commands = Lists.newArrayList();
                commands.add("java");
                if (StringUtils.isNotBlank(vmParam)) {
                    commands.add(vmParam);
                }
                commands.add("-jar");
                commands.add(jarPath);
                if (StringUtils.isNotBlank(parameter)) {
                    commands.add(parameter);
                }
                processBuilder.command(commands);
                log.info("quartz--> Running Job Commands : {}  ", JsonUtils.obj2String(commands));
                try {
                    Process process = processBuilder.start();
                    logProcess(process.getInputStream(), process.getErrorStream());
                } catch (Exception e) {
                    log.error("Job Execution Error : {}", e.getMessage());
                }
            }
        } else {
            log.error("Job Jar not found >>  " + jarPath);
        }
        long endTime = System.currentTimeMillis();
        log.info("quartz--> >>>>>>>>>>>>> Running Job has been completed , cost time :  " + (endTime - startTime) + "ms\n");
    }

    private void printLog(JobDataMap map) {
        log.info("quartz--> Running Job Name : {}", map.getString("name"));
        log.info("quartz--> Running Job Description  : {}", map.getString("JobDescription"));
        log.info("quartz--> Running Job Group  : {}", map.getString("group"));
        log.info("quartz--> Running Job Cron  : {}", map.getString("cronExpression"));
    }

    /**
     * description: 打印Job执行内容的日志
     *
     * @param inputStream 输入流
     * @param errorStream 错误流
     * @author k
     * @date 2018/12/22 14:56
     **/
    private void logProcess(InputStream inputStream, InputStream errorStream) throws IOException {
        String inputLine;
        String errorLine;
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        while ((inputLine = inputReader.readLine()) != null) {
            log.info("quartz--> " + inputLine);
        }
        while ((errorLine = errorReader.readLine()) != null) {
            log.error("quartz--> " + errorLine);
        }
    }
}
