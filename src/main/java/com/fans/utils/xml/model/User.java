package com.fans.utils.xml.model;

import lombok.*;

import java.io.Serializable;

/**
 * className: User
 *
 * @author k
 * @version 1.0
 * @description
 * @date 2018-12-20 14:14
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
