package com.fans.utils.xml.model;

import lombok.*;

import java.io.Serializable;

/**
 * @ClassName User
 * @Description:
 * @Author k
 * @Date 2018-11-13 19:47
 * @Version 1.0
 **/
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = -7715366160505406037L;

    private String name;
    private int age;
}
