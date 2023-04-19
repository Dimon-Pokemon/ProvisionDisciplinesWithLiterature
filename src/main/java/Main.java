import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args){
        DataBase db = new DataBase();
        getLiterature(db);
        specialtiesIsProvidedWithBooks(db);
    }

    private static void getLiterature(DataBase db){
        List<HashMap<String, String>> allLit = db.select("literature");
        System.out.println("В распоряжении есть следующие книги: ");
        for(HashMap<String, String> book : allLit){
            System.out.println(
                    """
                    ############################################################################
                    \t-Название: %s;
                    \t-Издание:  %s;
                    \t-ISBN код: %s;
                    ############################################################################
                    """.formatted(book.get("title"), book.get("edition"), book.get("isbn")));
        }
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
