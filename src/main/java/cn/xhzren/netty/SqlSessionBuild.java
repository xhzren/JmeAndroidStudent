package cn.xhzren.netty;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SqlSessionBuild {

    private final static String resource = "mybatis-config.xml";
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            sqlSessionFactory = new SqlSessionFactoryBuilder()
                    .build(Resources.getResourceAsStream(resource));
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionBuild.getSession();
        TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
        List<Map> map = testMapper.getUserList();
        map.forEach((e)-> {
            e.forEach((s, o)-> {
                System.out.println(s + ": " + o);
            });
        });
    }
}
