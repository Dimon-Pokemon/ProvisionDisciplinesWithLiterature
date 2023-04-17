import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args){
        DataBase db = new DataBase();
        //db.selectLibraryProfileSpeciality();
        specialtiesIsProvidedWithBooks(db);
    }

    private static void specialtiesIsProvidedWithBooks(DataBase db){
        HashMap<String, String> books = db.getIsbnAndTitle();
        List<HashMap<String, String>> SQLQueryResult = db.selectLibraryProfileSpeciality();
        for(HashMap<String, String> row : SQLQueryResult){
            if (Integer.parseInt(row.get("student_amount"))>Integer.parseInt(row.get("amount"))){
                System.out.println("На специальности "
                        + row.get("code")
                        + ", направлении '"
                        + row.get("title")
                        +"' не хватает книг '"
                        + books.get(row.get("isbn_fk"))
                        + "'. Требуется: "
                        + row.get("student_amount")
                        + ". В распоряжении: "
                        + row.get("amount"));
            }
        }

    }
}
