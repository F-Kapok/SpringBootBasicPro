package com.fans.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.sql.*;
import java.util.*;

/**
 * @Description: 数据库连接类 说明:封装了 无参，有参 调用
 * @Param:
 * @return:
 * @Author: fan
 * @Date: 2018/11/20 9:50
 **/
@Slf4j
public class ConnJDBC {

    /**
     * 创建数据库连接 对象
     */
    private Connection con = null;
    /**
     * 创建PreparedStatement对象
     */
    private PreparedStatement psmt = null;
    /**
     * 创建CallableStatement对象
     */
    private CallableStatement csmt = null;
    /**
     * 创建结果集对象
     */
    private ResultSet rs = null;

    /**
     * 建立数据库连接
     *
     * @return
     */
    private Connection getConnection() {
        Properties properties = new Properties();
        try {
            String classPath = this.getClass().getResource("/").getPath().replace("/classes", "")
                    + "config/ds.properties";
            FileInputStream stream = new FileInputStream(classPath);
            properties.load(stream);
            String driver = properties.getProperty("jdbc.driver");
            String url = properties.getProperty("jdbc.url");
            String username = properties.getProperty("jdbc.username");
            String password = properties.getProperty("jdbc.password");
            Class.forName(driver);
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * 关闭所有资源
     */
    private void closeAll() {
        // 关闭结果集对象
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("SQLException is {}", e.getMessage());
            }
        }

        // 关闭PreparedStatement对象
        if (psmt != null) {
            try {
                psmt.close();
            } catch (SQLException e) {
                log.error("SQLException is {}", e.getMessage());
            }
        }

        // 关闭CallableStatement 对象
        if (csmt != null) {
            try {
                csmt.close();
            } catch (SQLException e) {
                log.error("SQLException is {}", e.getMessage());
            }
        }

        // 关闭Connection 对象
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.error("SQLException is {}", e.getMessage());
            }
        }
    }

    /**
     * @Description: 增删改
     * @Param: [sql, params]
     * @return: int 受影响的行数
     * @Author: fan
     * @Date: 2018/11/20 9:52
     **/
    public int update(String sql, Object[] params) {
        int affectedLine = 0;
        try {
            con = this.getConnection();
            psmt = con.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    psmt.setObject(i + 1, params[i]);
                }
            }
            affectedLine = psmt.executeUpdate();
        } catch (SQLException e) {
            log.error("SQLException is {}", e.getMessage());
        } finally {
            closeAll();
        }
        return affectedLine;
    }

    /**
     * @Description: 查询 结果放入ResultSet中
     * @Param: [sql, params]
     * @return: java.sql.ResultSet 结果集
     * @Author: fan
     * @Date: 2018/11/20 9:52
     **/
    public ResultSet query(String sql, Object[] params) {
        try {
            con = this.getConnection();
            psmt = con.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.length; i++) {
                    psmt.setObject(i + 1, params[i]);
                }
            }
            rs = psmt.executeQuery();
        } catch (SQLException e) {
            log.error("SQLException is {}", e.getMessage());
        }
        return rs;
    }

    /**
     * @Description: 获取结果集，并将结果放在List中
     * @Param: [sql, params]
     * @return: java.util.List<java.util.Map                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object>>
     * @Author: fan
     * @Date: 2018/11/20 9:52
     **/
    public List<Map<String, Object>> getListMap(String sql, Object[] params) {
        ResultSet rs = query(sql, params);
        ResultSetMetaData rsmd = null;
        int columnCount = 0;
        try {
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
        } catch (SQLException e1) {
            log.error("SQLException is {}", e1.getMessage());
        }
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
                list.add(map);
            }
        } catch (SQLException e) {
            log.error("SQLException is {}", e.getMessage());
        } finally {
            // 关闭所有资源
            closeAll();
        }
        return list;
    }

    /**
     * @Description: 获取结果集，并将结果放在Map中
     * @Param: [sql, params]
     * @return: java.util.Map<java.lang.String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               java.lang.Object> 结果集
     * @Author: fan
     * @Date: 2018/11/20 9:52
     **/
    public Map<String, Object> getMap(String sql, Object[] params) {
        ResultSet rs = query(sql, params);
        ResultSetMetaData rsmd = null;
        int columnCount = 0;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            rsmd = rs.getMetaData();
            columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    map.put(rsmd.getColumnLabel(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            log.error("SQLException is {}", e.getMessage());
        } finally {
            closeAll();
        }
        return map;
    }
}
