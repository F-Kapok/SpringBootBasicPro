## 项目规范

- 代码包分层含义
>controller层：api控制层
>
>service层：业务处理服务层
>
>logic层：复杂业务处理层，用于分解service类中的代码
>
>mapper层：数据操作层
>
>entity层：实体类层，映射单张数据表字段
>
>
>common、utils、extension层：项目通用配置类、工具类、扩展类
>
>do层：实体类层，映射多张表数据查询字段
>
>dto层：前端到控制层的入参
>
>vo层：后端返回前端的视图对象
>
>bo层：自己业务对象，例如logic层类中方法使用的入参

