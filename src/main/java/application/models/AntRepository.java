package application.models;

import application.controllers.Habitat;
import application.models.data.AbstractAnt;
import application.models.data.implement.WarriorAnt;
import application.models.data.implement.WorkerAnt;
import javafx.application.Platform;

import java.io.*;
import java.util.*;

public class AntRepository {
    private static Vector<AbstractAnt> vectorOfAnt;

    private static HashSet<Long> setOfId;

    private static TreeMap<Long, Set<Long>> mapOfBirthTime;
    private static AntRepository instance;

    private static final Map<Class<? extends AbstractAnt>, Long> lastTimeAntCreatedMap = new HashMap<>();

    private AntRepository() {
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

    public static synchronized void createAntIfTimeElapsed(long timePassed, Class<? extends AbstractAnt> antClass, double appearanceTime, double appearanceChance,
                                                           int simulationAreaWidth, int simulationAreaHeight) throws InterruptedException {
        double probability = random.nextDouble();
        long lastTimeAntCreated = lastTimeAntCreatedMap.getOrDefault(antClass, 0L);

        if (timePassed - lastTimeAntCreated >= appearanceTime) {
            if (appearanceChance >= probability) {
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


    public static void deleteAntsIfLifeTimeElapsed(long timePassed) throws InterruptedException {
        Iterator<AbstractAnt> iterator = vectorOfAnt.iterator();
        while (iterator.hasNext()) {
            AbstractAnt ant = iterator.next();
            if (ant.deathTime <= timePassed) {
                iterator.remove();
                ant.killThread();
            }
        }
//        System.out.println("deleted");
    }

    public long getWorkerCount() {
        return vectorOfAnt.stream().filter(abstractAnt -> abstractAnt.getClass() == WorkerAnt.class).count();
    }

    public long getWarriorCount() {
        return vectorOfAnt.stream().filter(abstractAnt -> abstractAnt.getClass() == WarriorAnt.class).count();
    }

    public TreeMap<Long, Set<Long>> getMapOfBirthTime() {
        return mapOfBirthTime;
    }

    public void setClassThreadStatus(Class<? extends AbstractAnt> obj, boolean status) {
        Platform.runLater(() -> {
            for (AbstractAnt ant : vectorOfAnt) {
                if (ant.getClass() == obj) {
                    if (!status) {
                        try {
                            ant.waitThread();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        try {
                            ant.notifyThread();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            if (obj == WarriorAnt.class) {
                WarriorAnt.isEnabled = status;
            }

            if (obj == WorkerAnt.class) {
                WorkerAnt.isEnabled = status;
            }
        });
    }

    public void setClassThreadPriority(Class<? extends AbstractAnt> obj, int priority) {
        for (AbstractAnt ant : vectorOfAnt) {
            if (ant.getClass() == obj) {
                ant.setPriority(priority);
            }
        }
    }

    public void serializeAllObjects() throws IOException {
        FileOutputStream outputStream = new FileOutputStream("objects.ser");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        objectOutputStream.writeObject(vectorOfAnt);
        objectOutputStream.close();
    }

    public void deserializeAllObjects() throws IOException, ClassNotFoundException, InterruptedException {
        FileInputStream fileInputStream = new FileInputStream("objects.ser");
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

//        System.out.println(vectorOfAnt.size());
        vectorOfAnt.clear();

//        System.out.println(vectorOfAnt.size());
        for (AbstractAnt obj : (Vector<AbstractAnt>) objectInputStream.readObject()) {
            if (obj.getClass() == WorkerAnt.class) {
                WorkerAnt workerAnt = new WorkerAnt(Habitat.WIDTH, Habitat.HEIGHT, Habitat.startTime, generateUniqueRandomId());

                workerAnt.x = obj.x;
                workerAnt.y = obj.y;
                workerAnt.id = obj.id;
                workerAnt.startX = ((WorkerAnt) obj).startX;
                workerAnt.startY = ((WorkerAnt) obj).startY;
                workerAnt.movingToTarget = ((WorkerAnt) obj).movingToTarget;
                workerAnt.deathTime = workerAnt.birthTime + WorkerAnt.liveTime;
                workerAnt.initImageView();
                vectorOfAnt.add(workerAnt);
            } else {
                WarriorAnt warriorAnt = new WarriorAnt(Habitat.WIDTH, Habitat.HEIGHT, Habitat.startTime, generateUniqueRandomId());

                warriorAnt.x = obj.x;
                warriorAnt.y = obj.y;
                warriorAnt.id = obj.id;
                warriorAnt.centerX = ((WarriorAnt) obj).centerX;
                warriorAnt.centerY = ((WarriorAnt) obj).centerY;
                warriorAnt.angle = ((WarriorAnt) obj).angle;
                warriorAnt.deathTime = warriorAnt.birthTime + WarriorAnt.liveTime;
                warriorAnt.initImageView();
                vectorOfAnt.add(warriorAnt);
            }
            System.out.println(obj);
        }
    }
}
