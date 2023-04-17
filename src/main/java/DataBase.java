import java.sql.Connection;
import java.sql.Driver;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class DataBase {

    private Connection connection;
    private Statement statement;

    public DataBase(){
        try {
            this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Admin2022");
            this.statement = connection.createStatement();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public HashMap<String, String> getIsbnAndTitle(){
        HashMap<String, String> result =  new HashMap<String, String>();
        try{
            ResultSet resultQuery = statement.executeQuery(
                    """
                     SELECT isbn, title from literature
                     """
            );
            while(resultQuery.next()){
                result.put(resultQuery.getString("isbn"), resultQuery.getString("title"));
                //System.out.println("ISBN: "+resultQuery.getString("isbn")+" Название: "+resultQuery.getString("title"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * Метод позволяет выбрать из любой таблицы любые столбцы.
     * Основан на простом SELECT запросе.
     * @param table таблица, к которой будет отправлен запрос.
     * @param columnArray столбцы, которые будем выбирать из таблицы.
     */
    public List<HashMap<String, String>> select(String table, String[] columnArray){
        List<HashMap<String, String>> result = new ArrayList<>();
        if(columnArray.length<1){
            throw new RuntimeException("Массив столбцов не может быть пустым!");
        }
        try{
            // Выполнение SQL запроса
            ResultSet resultQuery = statement.executeQuery(
                    """
                    SELECT %s FROM %s;
                    """.formatted(table, String.join(", ", columnArray)));
            // Результат SQL запроса помещается в список.
            while (resultQuery.next()){
                //System.out.println("Код: "+result.getString(1)+"; Название: "+result.getString(2));
                // row - одна строка результата запроса
                HashMap<String, String> row = new HashMap<>(); // Пары имя_столбца:значение
                for(int i = 0; i<columnArray.length; i++){
                    row.put(columnArray[i], resultQuery.getString(i+1));
                }
                result.add(row); // Добавляем получившуюся строку в список строк result
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public List<HashMap<String, String>> selectLibraryProfileSpeciality(){ //List<HashMap<String, String>>
        String sql =
                """
                select * from profile p
                join speciality s on s.code = p.code_speciality_fk
                join "library" l on l.profile_title_fk = p.title;
                """;
        List<HashMap<String, String>> result = new ArrayList<>();
        try {
            ResultSet resultQuery = statement.executeQuery(sql);
            while (resultQuery.next()){
                // row - одна строка результата запроса
                HashMap<String, String> row = new HashMap<>(); // Пары имя_столбца:значение
                for(int i = 0; i<resultQuery.getMetaData().getColumnCount(); i++){
                    row.put(resultQuery.getMetaData().getColumnName(i+1), resultQuery.getString(i+1));
//                    System.out.println("Колонка: "+resultQuery.getMetaData().getColumnName(i+1)+" Значение: "+row.get(resultQuery.getMetaData().getColumnName(i+1)));
                }
                result.add(row); // Добавляем получившуюся строку в список строк result
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public void addNewRowInTable(String table, String[] columnArray, String[] values){
        try {
            statement.executeUpdate(
                    """
                    INSERT INTO %s
                    (%s)
                    values
                    (%s);
                    """.formatted(table, String.join(", ", columnArray), String.join(", ", values)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
