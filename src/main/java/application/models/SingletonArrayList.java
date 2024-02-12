package application.models;

import java.util.ArrayList;

public class SingletonArrayList {
    private static ArrayList<AbstractAnt> arrayList;
    private static SingletonArrayList instance;

    private SingletonArrayList(){
        arrayList = new ArrayList<>();
    }

    public static SingletonArrayList getInstance() {
        // Метод для получения единственного экземпляра
        if (instance == null) {
            instance = new SingletonArrayList();
        }
        return instance;
    }

    public ArrayList<AbstractAnt> getArrayList() {
        return  arrayList;
    }
}
