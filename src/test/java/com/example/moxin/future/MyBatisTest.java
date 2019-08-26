import com.example.moxin.future.FutureApplication;
import com.example.moxin.future.mybatisTest.dao.TestMapper;
import com.example.moxin.future.mybatisTest.dto.PageInfo;
import com.example.moxin.future.mybatisTest.dto.TestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FutureApplication.class)
public class MyBatisTest {
    @Autowired
    TestMapper testMapper;


    @Test
    public void test()  {

        for (int i=0;i<10;i++){
            testMapper.insert("测试"+i);
        }

//        List<TestModel> a = testMapper.getAll();
//        for (TestModel testModel :a){
//            System.out.println(testModel.getName());
//        }

    }

    @Test
    public void testMybitsPage()  {


        PageInfo pageInfo = new PageInfo();
        pageInfo.setCurrentPage(2);
        Map param = new HashMap();
        param.put("page",pageInfo);
        List<TestModel> a = testMapper.getAllByPage(param);
        for (TestModel testModel :a){
            System.out.println(testModel.getName());
        }

    }

    @Test
    public void testMybitsQuery()  {


       List param = new ArrayList();
        param.add("1");
        param.add("2");
        List<TestModel> a = testMapper.getNameByIds(param);
        for (TestModel testModel :a){
            System.out.println(testModel.getName());
        }

    }


}
