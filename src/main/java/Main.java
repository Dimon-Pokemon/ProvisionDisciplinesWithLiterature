import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args){
        DataBase db = new DataBase();
        db.addNewRowInTable(
                "literature",
                new String[]{"isbn", "title", "edition"},
                new String[]{"127", "Книга 4", "1"}
        );
        getLiterature(db);
        specialtiesIsProvidedWithBooks(db);
    }

    /**
     * Метод выводит список всей доступной литературы в красивом, читабельном виде.
     * @param db объект класса DataBase. Через него работаем с базой.
     */
    private static void getLiterature(DataBase db){
        List<HashMap<String, String>> allLit = db.select("literature");
        System.out.println("В распоряжении есть следующие книги: ");
        for(HashMap<String, String> book : allLit){
            System.out.printf("""
                    ############################################################################
                        -Название: %s;
                        -Издание:  %s;
                        -ISBN код: %s;
                    ############################################################################
                    %n""", book.get("title"), book.get("edition"), book.get("isbn"));
        }
    }

    /**
     * Метод выводи информацию о направлениях обучения, которые не обеспечены достаточным количеством учебников.
     * @param db объект класса DataBase для работы с базой данных.
     */
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
