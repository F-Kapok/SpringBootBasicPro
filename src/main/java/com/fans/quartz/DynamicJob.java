package com.fans.quartz;

import com.fans.utils.JsonMapper;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * @ClassName DynamicJob
 * @Description: TODO 调度中心执行逻辑的Job
 * @Description: TODO @DisallowConcurrentExecution : 此标记用在实现Job的类上面,意思是不允许并发执行
 * @Description: TODO 注意org.quartz.threadPool.threadCount线程池中线程的数量至少要多个
 * @Description: TODO 否则@DisallowConcurrentExecution不生效
 * @Description: TODO 假如Job的设置时间间隔为3秒,但Job执行时间是5秒
 * @Description: TODO 设置@DisallowConcurrentExecution以后程序会等任务执行完毕以后再去执行
 * @Description: TODO 否则会在3秒时再启用新的线程执行
 * @Author fan
 * @Date 2018-12-22 14:38
 * @Version 1.0
 **/
@DisallowConcurrentExecution
@Component
@Slf4j
public class DynamicJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
        JobDataMap map = jobExecutionContext.getMergedJobDataMap();
        String jarPath = map.getString("jarPath");
        String parameter = map.getString("parameter");
        String vmParam = map.getString("vmParam");
        log.info("quartz--> Running Job Name : {}", map.getString("name"));
        log.info("quartz--> Running Job Description  : {}", map.getString("JobDescription"));
        log.info("quartz--> Running Job Group  : {}", map.getString("group"));
        log.info("quartz--> Running Job Cron  : {}", map.getString("cronExpression"));
        log.info("quartz--> Running Job JarPath   : {}", jarPath);
        log.info("quartz--> Running Job Parameter   : {}", parameter);
        log.info("quartz--> Running Job VmParam   : {}", vmParam);
        long startTime = System.currentTimeMillis();
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
                log.info("quartz--> Running Job Details As Follows >>>>>>>>>>>>>>>>>>>>: ");
                log.info("quartz--> Running Job Commands : {}  ", JsonMapper.obj2String(commands));
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

    /**
     * @Description: TODO 打印Job执行内容的日志
     * @Param: [inputStream, errorStream]
     * @return: void
     * @Author: fan
     * @Date: 2018/12/22 14:56
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
