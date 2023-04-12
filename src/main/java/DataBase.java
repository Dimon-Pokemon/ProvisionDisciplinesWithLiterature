import java.sql.Connection;
import java.sql.Driver;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

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

    public void select(String table, String[] columnArray){
        try{
            ResultSet result = statement.executeQuery(
                    """
                    SELECT %s FROM %s;
                    """.formatted(columnArray.length>0 ? String.join(", ", columnArray) : "*", table));
            while (result.next()){
                System.out.println("Код: "+result.getString(1)+"; Название: "+result.getString(2));

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addStudent(String lastName, String firstName, String middleName, String classTitleFk){
        try {
            statement.executeUpdate(
                    """
                    INSERT INTO student
                    (last_name, first_name, middle_name, class_title_fk)
                    values
                    ('%s', '%s', '%s', '%s');
                    """.formatted(lastName, firstName, middleName, classTitleFk)
            );
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addClass(String title, String codeDirectionTrainingFk, String studentAmount){
        try {
            statement.executeUpdate(
                    """
                    INSERT INTO class
                    (title, code_direction_training_fk, student_amount)
                    values
                    ('%s', '%s', %s);
                    """.formatted(title, codeDirectionTrainingFk, studentAmount)
            );
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void addDirectionOfTraining(String code, String title){
        try {
            statement.executeUpdate(
                    """
                    INSERT INTO direction_of_training
                    (code, title)
                    values
                    ('%s', '%s');
                    """.formatted(code, title)
            );
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
