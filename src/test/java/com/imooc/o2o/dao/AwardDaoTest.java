package com.imooc.o2o.dao;

import com.imooc.o2o.entity.Award;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 2018/1/22.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AwardDaoTest {

    @Autowired
    private AwardDao awardDao;


    /**
     * 测试添加功能
     */
    @Test
    public void testAInsertAward() throws Exception{

        long shopId =1;
        //创建奖品一
        Award award1 = new Award();
        award1.setAwardName("测试一");
        award1.setAwardImg("test1");
        award1.setPoint(5);
        award1.setPriority(1);
        award1.setEnableStatus(1);
        award1.setCreateTime(new Date());
        award1.setLastEditTime(new Date());
        award1.setShopId(shopId);
        int effectedNum = awardDao.insertAward(award1);
        assertEquals(1,effectedNum);
        //创建奖品二
        Award award2 = new Award();
        award2.setAwardName("测试二");
        award2.setAwardImg("test2");
        award2.setPoint(2);
        award2.setPriority(2);
        award2.setEnableStatus(0);
        award2.setCreateTime(new Date());
        award2.setLastEditTime(new Date());
        award2.setShopId(shopId);
         effectedNum = awardDao.insertAward(award2);
        assertEquals(1,effectedNum);
    }


    /**
     * 测试查询列表功能
     * @throws Exception
     */
    @Test
    public void testBQueryAwardList() throws Exception{

        Award award = new Award();
        List<Award> awardList = awardDao.queryAwardList(award,0,3);
        assertEquals(2,awardList.size());
        int count = awardDao.queryAwardCount(award);
        assertEquals(2,count);
        award.setAwardName("测试");
        awardList = awardDao.queryAwardList(award,0,3);
        assertEquals(2,awardList.size());
        count = awardDao.queryAwardCount(award);
        assertEquals(2,count);
    }


    /**
     * 测试按照Id查询功能
     */
    @Test
    public void testCQueryAwardByAwardId(){

        Award awardCondition = new Award();
        awardCondition.setAwardName("测试一");
        //按照特定名字查询返回特定的奖品
        List<Award> awardList = awardDao.queryAwardList(awardCondition,0,1);
       System.out.println("查询后的结果："+awardList.size());
        assertEquals(1,awardList.size());
        //
        Award award = awardDao.queryAwardByAwardId(awardList.get(0).getAwardId());
        System.out.println("查询后的名字："+award.getAwardName());
        assertEquals("测试一",award.getAwardName());
    }


    /**
     * 测试更新功能
     */
    @Test
    public void testDUpdateAward(){
        Award awardCondition = new Award();
        awardCondition.setAwardName("测试一");
        // 按照特定名字查询返回特定的奖品
        List<Award> awardList = awardDao.queryAwardList(awardCondition,0,1);
        awardList.get(0).setAwardName("第一个测试奖品");
        int effectedNum = awardDao.updateAward(awardList.get(0));
        assertEquals(1,effectedNum);
        // 将修改名称后的奖品找出来并验证
        Award award = awardDao.queryAwardByAwardId(awardList.get(0).getAwardId());
        assertEquals("第一个测试奖品",award.getAwardName());


    }


    /**
     * 测试删除功能
     */
    @Test
    public void testDeleteAward(){

        Award awardCondition = new Award();
        awardCondition.setAwardName("测试");
        //查询出所有测试奖品并删除
        List<Award> awardList = awardDao.queryAwardList(awardCondition,0,2);
        assertEquals(2,awardList.size());
        for (Award award : awardList){
            int effecteNum = awardDao.deleteAward(award.getAwardId(),award.getShopId());
            assertEquals(1,effecteNum);
        }

    }


}
