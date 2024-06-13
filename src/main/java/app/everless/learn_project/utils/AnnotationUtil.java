package app.everless.learn_project.utils;

import app.everless.learn_project.Annotation.TableFielId;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class AnnotationUtil {

    //获取表字段和实体类字段的对应关系,通过注解获取，如果没有则会进行驼峰转换
    public static Map<String,String> getTableFiedIds(Class<?> clazz){
        Map<String,String> fieldMap = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            TableFielId annotation = field.getAnnotation(TableFielId.class);
            if (annotation != null){
                fieldMap.put(field.getName(),annotation.value());
            }else{
                fieldMap.put(field.getName(),camelToUnderline(field.getName()));
            }
        }
        return fieldMap;
    }



//map类型数据转向实体类
    public static <T> T mapResultSetToEntity(ResultSet resultSet, Class<T> clazz) throws Exception {
        T instance = clazz.getDeclaredConstructor().newInstance();
        Map<String, String> tableFiedIds = getTableFiedIds(clazz);
        for(Map.Entry<String,String> entry : tableFiedIds.entrySet()){
//            字段名
            String fieldName = entry.getKey();
//             列名
            String columnName = entry.getValue();
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            if (!columnName.equals("")){
                field.set(instance,resultSet.getObject(columnName));
            }else{
                field.set(instance,resultSet.getObject(camelToUnderline(fieldName)));
            }
        }
        return instance;

    }



    //    驼峰命名转换
    public static String camelToUnderline(String param){
        if (param == null || "".equals(param.trim())){
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)){
                sb.append("_");
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String underlineToCamel(String param){
        if (param == null || "".equals(param.trim())){
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == '_'){
                if (++i < len){
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
