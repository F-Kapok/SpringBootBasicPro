package com.fans.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobEntity {
    /**
     * 主键ID
     */
    private Integer id;

    /**
     * job名称
     */
    private String name;

    /**
     * job组名
     */
    private String group;

    /**
     * 执行的cron
     */
    private String cron;

    /**
     * job的参数
     */
    private String parameter;

    /**
     * job描述信息
     */
    private String description;

    /**
     * vm参数
     */
    private String vmParam;

    /**
     * job的jar路径
     */
    private String jarPath;

    /**
     * job的执行状态,只有该值为OPEN才会执行该Job
     */
    private String status;

    /**
     * 任务类型 0：jar 1：class
     */
    private String jobType;

    /**
     * jon类名称
     */
    private String className;

    /**
     * job类方法
     */
    private String classMethod;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group == null ? null : group.trim();
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron == null ? null : cron.trim();
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter == null ? null : parameter.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getVmParam() {
        return vmParam;
    }

    public void setVmParam(String vmParam) {
        this.vmParam = vmParam == null ? null : vmParam.trim();
    }

    public String getJarPath() {
        return jarPath;
    }

    public void setJarPath(String jarPath) {
        this.jarPath = jarPath == null ? null : jarPath.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType == null ? null : jobType.trim();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className == null ? null : className.trim();
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod == null ? null : classMethod.trim();
    }
}