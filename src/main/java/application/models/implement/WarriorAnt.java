package application.models.implement;

import application.models.AbstractAnt;
import application.models.IBehaviour;

public class WarriorAnt extends AbstractAnt implements IBehaviour {
    public WarriorAnt(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
