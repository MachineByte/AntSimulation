package application.models;

import application.models.data.AbstractAnt;
import application.models.data.implement.WarriorAnt;
import application.models.data.implement.WorkerAnt;

import java.util.ArrayList;
import java.util.Random;

public class AntRepository {
    private static ArrayList<AbstractAnt> arrayListOfAnt;
    private static AntRepository instance;

    private AntRepository(){
        arrayListOfAnt = new ArrayList<>();
    }

    public static AntRepository getInstance() {
        // Метод для получения единственного экземпляра
        if (instance == null) {
            instance = new AntRepository();
        }
        return instance;
    }

    public ArrayList<AbstractAnt> getArrayList() {
        return arrayListOfAnt;
    }

    public static void createAntIfTimeElapsed(long timePassed, Class<? extends AbstractAnt> antClass, double appearanceTime, double appearanceChance,
                                              int simulationAreaWidth,int simulationAreaHeight) {
        Random random = new Random();
        double probability = random.nextDouble();
        if ((Math.round(timePassed / 100.0) * 100) % appearanceTime == 0 && appearanceChance >= probability) {
            AbstractAnt newAnt = (antClass == WarriorAnt.class) ? new WarriorAnt(simulationAreaWidth, simulationAreaHeight) : new WorkerAnt(simulationAreaWidth, simulationAreaHeight);
            arrayListOfAnt.add(newAnt);
        }
    }

    public long getWorkerCount(){
        return arrayListOfAnt.stream().filter(abstractAnt -> abstractAnt.getClass() == WorkerAnt.class).count();
    }

    public long getWarriorCount(){
        return arrayListOfAnt.stream().filter(abstractAnt -> abstractAnt.getClass() == WarriorAnt.class).count();
    }
}
