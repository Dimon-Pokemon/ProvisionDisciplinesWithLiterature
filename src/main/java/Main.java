public class Main {
    public static void main(String[] args){
        DataBase db = new DataBase();
        //db.addDirectionOfTraining("10.03.01", "Информационная безопасность");
        db.select("direction_of_training", new String[0]);
    }
}
