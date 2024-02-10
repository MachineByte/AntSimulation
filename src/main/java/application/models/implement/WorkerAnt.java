package application.models.implement;

import application.models.AbstractAnt;
import application.models.IBehaviour;

public class WorkerAnt extends AbstractAnt implements IBehaviour {
    public WorkerAnt(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
