package com.wemarket.wac;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WacApplicationTests {

    @Test
    public void contextLoads() {
        //        System.out.println( HanLP.segment("www.codesheep.cn是一个技术博客！") );
        StringUtils.isNotBlank("");
        JaccardSimilarity jac = new JaccardSimilarity();
        double simi = jac.apply("京卡", "京东卡");
        System.out.println(simi);
//        String[] wordArray = new String[]
//                {
//                        "蜜瓜",
//                        "哈密瓜",
//                        "水果",
//                        "苹果",
//                };
//        for (String a : wordArray)
//        {
//            for (String b : wordArray)
//            {
//                System.out.println(a + "\t" + b + "\t之间的距离是\t" + CoreSynonymDictionary.similarity(a, b));
//            }
//        }
    }

}
