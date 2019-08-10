package com.example.moxin.future.mybatisTest.plugins;

import com.example.moxin.future.mybatisTest.dto.PageInfo;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Properties;

@Intercepts(@Signature(
        type= StatementHandler.class,
        method = "prepare",
        args = {Connection.class,Integer.class}
))
public class MyPageHelper implements Interceptor {
    private String dialect;
    private String pageSqlId;

    //做具体拦截的事情
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取statementHandler，所有sql可以执行的情景都在这里
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();

        //获取原始放入sql语句
        BoundSql boundSql =statementHandler.getBoundSql();
        String orgSql = boundSql.getSql();
        System.out.println("原始的sql："+orgSql);

        //获取分页参数
        Object paramObject = boundSql.getParameterObject();

        //获取statementHandler元数据
        MetaObject metaObject = MetaObject.forObject(statementHandler, SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                                 SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,new DefaultReflectorFactory());

        //获取mappedStatement
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");

        //获取方法的名称id
        String statementId = mappedStatement.getId();
        System.out.println("调用sql的ID："+statementId);

        if(statementId.matches(pageSqlId)){
            //封装参数为Map
            Map param = (Map) paramObject;
            PageInfo pageInfo = (PageInfo) param.get("page");

            //查询sql总跳数
            String conutSql = "select count(1) from ("+orgSql+")a";
            System.out.println("查询总数目的sql:"+conutSql);

            //获取JDBC的数据库操作
            Connection connection= (Connection) invocation.getArgs()[0];
            PreparedStatement countStatement = connection.prepareStatement(conutSql);
            ParameterHandler parameterHandler = (ParameterHandler) metaObject.getValue("delegate.parameterHandler");
            parameterHandler.setParameters(countStatement);
            ResultSet rs = countStatement.executeQuery();
            if(rs.next()){
                pageInfo.setTotalNumber(rs.getInt(1));
            }
            rs.close();
            countStatement.close();

            //将原始的sql改造成PageSql
            String pageSql = generatePageSql(orgSql,pageInfo);
            System.out.println("分页的sql:"+pageSql);

            //将修改后的语句重置给myBites
            metaObject.setValue("delegate.boundSql.sql",pageSql);



        }


        return invocation.proceed();
    }

    //将当前插件放入插件链中去
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }
    // 对插件进行配置属性
    @Override
    public void setProperties(Properties properties) {
        dialect = properties.getProperty("dialect");
        pageSqlId = properties.getProperty("pageSqlId");
    }

    private String generatePageSql(String orgSql,PageInfo pageInfo){
        //判断是什么数据库
        if("mysql".equals(dialect)){
            StringBuilder sql = new StringBuilder(orgSql);
            sql.append(" limit "+ pageInfo.getStartIndex()+","+pageInfo.getTotalSelect());
            return sql.toString();
        }else if("orcale".equals(dialect)){

        }
        return null;
    }
}
