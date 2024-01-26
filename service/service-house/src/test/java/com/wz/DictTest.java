package com.wz;

import com.wz.dao.DictDao;
import com.wz.entity.Dict;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

//Spring集成Junit4(单元测试)
//当前只是为了测试dao，因此只使用dao.xml
@ContextConfiguration(locations = "classpath:spring/spring-dao.xml")
@RunWith(SpringRunner.class)
public class DictTest {

    @Autowired
    private DictDao dictDao;

    /*
     * 测试根据父id查询所有子节点
     */
    @Test
    public void testFindListByParentId() {
        List<Dict> listByParentId = dictDao.findListByParentId(1L);
        for (Dict dict : listByParentId) {
            System.out.println("dict = " + dict);
        }
    }
}
