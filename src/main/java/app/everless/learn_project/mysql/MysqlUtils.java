package app.everless.learn_project.mysql;

import app.everless.learn_project.utils.AnnotationUtil;

import java.lang.reflect.Field;
import java.util.Map;

public class MysqlUtils {
    public static <T> T excuteQuerySql(Class<T> clazz){
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
                Field field = clazz.getDeclaredField(key);
                field.setAccessible(true);
                Object o = field.get(instance);

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
//        执行sql语句






        return null;

    }
}
