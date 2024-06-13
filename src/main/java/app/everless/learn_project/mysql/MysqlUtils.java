package app.everless.learn_project.mysql;

import app.everless.learn_project.utils.AnnotationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
@Component
public class MysqlUtils {
    @Autowired
    private MysqlConnectionPool mysqlConnectionPool;

    public <T> T excuteQuerySql(T object,Class<T> clazz){

//        构建对象的instance
        T instance = null;
        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
//        解析出实体类中的属性列表
        Map<String, String> tableFiedIds = AnnotationUtil.getTableFiedIds(clazz);
//        构建sql查询语句
        StringBuilder sql = new StringBuilder("select ");
        for (Map.Entry<String, String> entry : tableFiedIds.entrySet()) {
            sql.append(entry.getValue()).append(",");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" from ").append(clazz.getSimpleName().toLowerCase());
//        添加where条件根据tableFiedIds的map中的key去获取属性
        boolean whereAdd = false;
        for (Map.Entry<String, String> entry : tableFiedIds.entrySet()) {
//            获取到当前键
            String key = entry.getKey();
//            获取到当前值

            try {
                Field field = object.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object o = field.get(object);

                String s = "";
                if (field.getType().equals(int.class) &&(int) o == 0){
                    s = "";
                }else{
                    s = String.valueOf(o);
                }
                if (!s.isEmpty() && !s.equals("null")){
                    if (!whereAdd){
                        sql.append(" where ").append(entry.getValue()).append(" = ").append(s);
                        whereAdd = true;
                    }else {
                        sql.append(" and ").append(entry.getValue()).append(" = ").append(s);
                    }
                }


            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        sql.append(" limit 1");
//        执行sql语句
        Connection connection = null;
        try{
            connection =mysqlConnectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql.toString());
            preparedStatement.execute();
            ResultSet metaData = preparedStatement.getResultSet();
            while (metaData.next()){
            System.out.println(metaData);
            T t = AnnotationUtil.mapResultSetToEntity(metaData, clazz);
            return t;
            }

        }catch (Exception e){

            e.printStackTrace();
        }finally {
            mysqlConnectionPool.release(connection);
        }

        return null;

    }
}
