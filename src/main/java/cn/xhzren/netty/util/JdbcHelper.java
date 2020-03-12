package cn.xhzren.netty.util;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;

import java.util.Properties;

public class JdbcHelper {

    public void test() {
        Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", "test");
        myBatisProperties.setProperty("JDBC.schema", "mybatis-guice_TEST");
        myBatisProperties.setProperty("derby.create", "true");
        myBatisProperties.setProperty("JDBC.username", "sa");
        myBatisProperties.setProperty("JDBC.password", "");
        myBatisProperties.setProperty("JDBC.autoCommit", "false");
//
        Injector injector = Guice.createInjector(
                org.mybatis.guice.datasource.helper.JdbcHelper.HSQLDB_Embedded,
                new Module() {
                    public void configure(Binder binder) {
                        Names.bindProperties(binder, myBatisProperties);
                    }
                }
        );
    }
}
