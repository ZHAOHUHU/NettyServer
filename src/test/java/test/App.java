package test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import shenzhen.teamway.mapper.TblAccountMapper;
import shenzhen.teamway.pojo.TblAccount;

import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * @program: NettyServer
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2018-09-03 15:51
 **/
public class App {
    public static void main(String[] args) {
        Reader reader = null;
        try {
            reader = Resources.getResourceAsReader("mybatisconfig.xml");
            SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
            SqlSessionFactory factory = builder.build(reader);
            SqlSession session = factory.openSession();
            final TblAccountMapper eventMapper = session.getMapper(TblAccountMapper.class);
            final List<TblAccount> tbl_events = eventMapper.selectAll();
            for (TblAccount tbl_event : tbl_events) {
                System.out.println(tbl_event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}