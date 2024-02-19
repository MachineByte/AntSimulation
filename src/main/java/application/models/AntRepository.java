package application.models;

import application.models.data.AbstractAnt;
import application.models.data.implement.WarriorAnt;
import application.models.data.implement.WorkerAnt;

import java.util.*;

public class AntRepository {
    private static Vector<AbstractAnt> vectorOfAnt;

    private static  HashSet<Long> setOfId;

    private static TreeMap<Long, Set<Long>> mapOfBirthTime;
    private static AntRepository instance;

    private static final Map<Class<? extends AbstractAnt>, Long> lastTimeAntCreatedMap = new HashMap<>();
    private AntRepository(){
        vectorOfAnt = new Vector<>();
        setOfId = new HashSet<>();
        mapOfBirthTime = new TreeMap<>();
    }

    private static final Random random = new Random();

    public static synchronized long generateUniqueRandomId() {
        long newId;
        do {
            newId = Math.abs(random.nextLong());
        } while (setOfId.contains(newId));
        setOfId.add(newId);
        return newId;
    }
    public static AntRepository getInstance() {
        // Метод для получения единственного экземпляра
        if (instance == null) {
            instance = new AntRepository();
        }
        return instance;
    }

    public Vector<AbstractAnt> getVector() {
        return vectorOfAnt;
    }

    public static void createAntIfTimeElapsed(long timePassed, Class<? extends AbstractAnt> antClass, double appearanceTime, double appearanceChance,
                                              int simulationAreaWidth, int simulationAreaHeight) {
        double probability = random.nextDouble();
        long lastTimeAntCreated = lastTimeAntCreatedMap.getOrDefault(antClass, 0L);

        if (timePassed - lastTimeAntCreated >= appearanceTime) {
            if(appearanceChance >= probability) {
                long newId = generateUniqueRandomId(); // Генерация уникального случайного ID
                AbstractAnt newAnt = (antClass == WarriorAnt.class) ?
                        new WarriorAnt(simulationAreaWidth, simulationAreaHeight, timePassed, newId) :
                        new WorkerAnt(simulationAreaWidth, simulationAreaHeight, timePassed, newId);
                vectorOfAnt.add(newAnt);
                mapOfBirthTime.computeIfAbsent(timePassed, k -> new HashSet<>()).add(newId);
            }
            lastTimeAntCreatedMap.put(antClass, timePassed);
        } else if (timePassed < lastTimeAntCreated) {
            lastTimeAntCreatedMap.clear(); // Сброс времени создания
        }

    }


    public static void deleteAntsIfLifeTimeElapsed(long timePassed) {
        Iterator<AbstractAnt> iterator = vectorOfAnt.iterator();
        while (iterator.hasNext()) {
            AbstractAnt ant = iterator.next();
            if (ant.deathTime <= timePassed) {
                iterator.remove();
            }
        }
    }

    public long getWorkerCount(){
        return vectorOfAnt.stream().filter(abstractAnt -> abstractAnt.getClass() == WorkerAnt.class).count();
    }

    public long getWarriorCount(){
        return vectorOfAnt.stream().filter(abstractAnt -> abstractAnt.getClass() == WarriorAnt.class).count();
    }
}
