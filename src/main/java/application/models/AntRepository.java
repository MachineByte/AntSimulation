package application.models;

import application.models.data.AbstractAnt;
import application.models.data.implement.WarriorAnt;
import application.models.data.implement.WorkerAnt;

import java.util.*;

public class AntRepository {
    private static Vector<AbstractAnt> vectorOfAnt;
    private static HashSet<Long> setOfId;
    private static TreeMap<Long, Set<Long>> mapOfBirthTime;
    private static AntRepository instance;
    private static final Map<Class<? extends AbstractAnt>, Long> lastTimeAntCreatedMap = new HashMap<>();
    private static final Random random = new Random();

    private AntRepository(){
        vectorOfAnt = new Vector<>();
        setOfId = new HashSet<>();
        mapOfBirthTime = new TreeMap<>();
    }

    // Статический метод для получения единственного экземпляра
    public static AntRepository getInstance() {
        if (instance == null) {
            instance = new AntRepository();
        }
        return instance;
    }

    // Получение вектора муравьев
    public Vector<AbstractAnt> getVector() {
        return vectorOfAnt;
    }

    // Генерация уникального случайного ID
    public static synchronized long generateUniqueRandomId() {
        long newId;
        do {
            newId = Math.abs(random.nextLong());
        } while (setOfId.contains(newId));
        setOfId.add(newId);
        return newId;
    }

    // Создание муравья, если прошло достаточно времени
    public static void createAntIfTimeElapsed(long timePassed, Class<? extends AbstractAnt> antClass, double appearanceTime,
                                              double appearanceChance, int simulationAreaWidth, int simulationAreaHeight) {
        double probability = random.nextDouble();
        long lastTimeAntCreated = lastTimeAntCreatedMap.getOrDefault(antClass, 0L);

        if (timePassed - lastTimeAntCreated >= appearanceTime) {
            if (appearanceChance >= probability) {
                long newId = generateUniqueRandomId();
                AbstractAnt newAnt = (antClass == WarriorAnt.class) ?
                        new WarriorAnt(simulationAreaWidth, simulationAreaHeight, timePassed, newId) :
                        new WorkerAnt(simulationAreaWidth, simulationAreaHeight, timePassed, newId);
                vectorOfAnt.add(newAnt);
                mapOfBirthTime.computeIfAbsent(timePassed, k -> new HashSet<>()).add(newId);
            }
            lastTimeAntCreatedMap.put(antClass, timePassed);
        } else if (timePassed < lastTimeAntCreated) {
            lastTimeAntCreatedMap.clear();
        }
    }

    // Удаление муравьев, если время жизни истекло
    public static void deleteAntsIfLifeTimeElapsed(long timePassed) {
        vectorOfAnt.removeIf(ant -> ant.getDeathTime() <= timePassed);
    }

    // Получение количества рабочих муравьев
    public long getWorkerCount() {
        return vectorOfAnt.stream().filter(abstractAnt -> abstractAnt.getClass() == WorkerAnt.class).count();
    }

    // Получение количества воинственных муравьев
    public long getWarriorCount() {
        return vectorOfAnt.stream().filter(abstractAnt -> abstractAnt.getClass() == WarriorAnt.class).count();
    }

    // Получение карты времени рождения муравьев
    public TreeMap<Long, Set<Long>> getMapOfBirthTime() {
        return mapOfBirthTime;
    }
}
