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

    public static void serializeVectorOfAnts(String filepath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filepath))) {
            oos.writeObject(vectorOfAnt);
        } catch (IOException e) {
            throw new IOException("Error serializing vectorOfAnt to " + filepath, e);
        }
    }

    public static void deserializeVectorOfAnts(String filepath) throws IOException, ClassNotFoundException {
        Vector<AbstractAnt> vectorTempOfAnt;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filepath))) {
            Object readObject = ois.readObject();
            if (readObject instanceof Vector) {
                vectorTempOfAnt = (Vector<AbstractAnt>) readObject;
                for (AbstractAnt ant : vectorTempOfAnt) {
                    ant.deathTime = ant.deathTime - ant.birthTime;
                    ant.birthTime = 0;
                    ant.initImageView();
                    vectorOfAnt.add(ant);
                }
            } else {
                throw new ClassNotFoundException("The read object is not of type Vector<AbstractAnt>");
            }
        } catch (IOException e) {
            throw new IOException("Error deserializing vectorOfAnt from " + filepath, e);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Class not found during deserialization", e);
        }
    }
}
