package com.example.moxin.future.mybatisTest.dao;

import com.example.moxin.future.mybatisTest.dto.PageInfo;
import com.example.moxin.future.mybatisTest.dto.TestModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TestMapper {

    @Select("select id,name from test")
    List<TestModel> select();

    @Insert("insert into test(name) values(#{name})")
    int insert(@Param("name") String name);

    List<TestModel> getAll();

    List<TestModel> getAllByPage(Map pageInfo);
}
