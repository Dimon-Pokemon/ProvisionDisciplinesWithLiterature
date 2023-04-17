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

    /**
     * Метод позволяет выбрать из любой таблицы любые столбцы.
     * Основан на простом SELECT запросе.
     * @param table таблица, к которой будет отправлен запрос.
     * @param columnArray столбцы, которые будем выбирать из таблицы.
     */
    public List<HashMap<String, String>> select(String table, String[] columnArray){
        List<HashMap<String, String>> result = new ArrayList<>();
        try{
            // Выполнение SQL запроса
            ResultSet resultQuery = statement.executeQuery(
                    """
                    SELECT %s FROM %s;
                    """.formatted(columnArray.length>0 ? String.join(", ", columnArray) : "*", table));
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

    public void addNewRowInTable(String table, String[] columnArray, String[] values){
        try{
            if (table.equals("student")){
                throw new Exception("Добавляйте новые записи в student с помощью addStudent");
            }
            statement.executeUpdate(
                    """
                    INSERT INTO %s
                    (%s)
                    values
                    (%s);
                    """.formatted(table, String.join(", ", columnArray), String.join(", ", values)));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addStudent(String lastName, String firstName, String middleName, String classTitleFk){
        try {
            int studentCountInt = 0;
            ResultSet studentCount = statement.executeQuery(
                    """
                    SELECT student.id FROM student
                    WHERE student.profile_title_fk = '%s';
                    """.formatted(classTitleFk)); // Получаем кол-во записей студентов на данном специальности
            while(studentCount.next()){
                studentCountInt += 1;
            }
            //System.out.println(studentCountInt);
            ResultSet studentAmountInClass = statement.executeQuery(
                    """
                    SELECT profile.student_amount FROM profile
                    WHERE profile.title = '%s';
                    """.formatted(classTitleFk));
            int studentAmountInClassInt = 0;
            while (studentAmountInClass.next())
                studentAmountInClassInt = studentAmountInClass.getInt(1);
            if (studentCountInt >= studentAmountInClassInt){
                throw new Exception("Нет мест на данной специальности.");
            }
            statement.executeUpdate(
                    """
                    INSERT INTO student
                    (last_name, first_name, middle_name, profile_title_fk)
                    values
                    ('%s', '%s', '%s', '%s');
                    """.formatted(lastName, firstName, middleName, classTitleFk)
            );
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
