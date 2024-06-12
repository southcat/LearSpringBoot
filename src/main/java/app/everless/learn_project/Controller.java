package app.everless.learn_project;

import app.everless.learn_project.model.User;
import app.everless.learn_project.mysql.MysqlConnectionPool;
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
    @RequestMapping("/hello")
    public String hello() throws SQLException {
        PreparedStatement preparedStatement = null;
        Connection connection = null;
        try {
            connection = mysqlConnectionPool.getConnection();
            String sql = "select * from user";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
            ResultSet metaData = preparedStatement.getResultSet();
            while (metaData.next()){
                System.out.println(metaData);
                User user = AnnotationUtil.mapResultSetToEntity(metaData, User.class);
                System.out.println(user);
              }



            return metaData.toString();
        } catch (Exception e) {
            return e.getMessage();
        } finally {
//            preparedStatement.close();
            mysqlConnectionPool.release(connection);
        }


//        mysqlConnectionPool.release(connection);

    }

}
