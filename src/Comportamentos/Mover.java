package Comportamentos;

import processing.core.PVector;

public abstract class Mover {

    protected PVector pos;//posiçao
    public PVector vel;//velocidade
    public PVector acc;//acelereação acumulada no frame atual
    protected float mass; //massa do objeto (influencia forças)
    private static final double G = 6.67e-11; //constante gravitacional

    protected Mover(PVector pos, PVector vel, float mass) {
        this.pos = pos.copy();
        this.vel = vel;
        acc = new PVector();
        this.mass = mass;
    }

    public void applyForce(PVector force) { acc.add(PVector.div(force, mass)); }

    public void move(float dt) {
        vel.add(acc.mult(dt));
        pos.add(PVector.mult(vel, dt));
        acc.mult(0);
    }

    public PVector attraction(Mover m) {
        PVector r = PVector.sub(this.pos, m.pos);
        float dist = r.mag();
        float strength = (float) (G * this.mass * m.mass / Math.pow(dist, 2));
        return r.normalize().mult(strength);
    }

    public PVector getPos() { return pos; }
    public PVector getVel() { return vel; }
    public PVector getAcc() { return acc; }
    
    public void setAcc(PVector acc) {this.acc = acc;}
    public void setVel(PVector vel) {this.vel = vel;}
    public void setPos(PVector pos) {this.pos = pos;}

}
