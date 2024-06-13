package app.everless.learn_project;

import app.everless.learn_project.model.User;
import app.everless.learn_project.mysql.MysqlConnectionPool;
import app.everless.learn_project.mysql.MysqlUtils;
import app.everless.learn_project.utils.AnnotationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@RestController
@RequestMapping("/api")
public class Controller {
    @Autowired
    MysqlConnectionPool mysqlConnectionPool;
    @Autowired
    MysqlUtils mysqlUtils;
    @RequestMapping("/hello")
    public String hello() throws SQLException {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        User user = new User();
        user.setId(2);
        User user1 = mysqlUtils.excuteQuerySql(user,User.class);
        System.out.println(user1);
;
        return "hello";

    }

}
