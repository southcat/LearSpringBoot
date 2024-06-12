package app.everless.learn_project.mysql;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

public class MysqlConnectionPool {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private Vector<Connection> connectionPool = new Vector<Connection>();
    private int poolSize = 10;
//    初始化创建池连接

    public MysqlConnectionPool(String url, String username, String password, String driverClassName){
        this.url = url;
        this.username = username;
        this.password = password;
        this.driverClassName = driverClassName;
        this.connectionPool = new Vector<Connection>(poolSize);
    }
    @PostConstruct
    public void init(){
        try {
            Class.forName(driverClassName);
            for (int i = 0; i < poolSize; ++i) {
                Connection connection = DriverManager.getConnection(url, username, password);
                connectionPool.add(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized Connection getConnection(){
        if (!connectionPool.isEmpty()){
            Connection connection = connectionPool.getFirst();
            connectionPool.removeFirst();
            return connection;
        }else {
         throw  new RuntimeException("连接池目前已满");
        }
    }
    public synchronized void release(Connection connection){
        connectionPool.add(connection);
    }


}
