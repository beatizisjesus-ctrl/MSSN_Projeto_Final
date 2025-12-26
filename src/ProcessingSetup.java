import processing.core.PApplet;

public class ProcessingSetup extends PApplet {

    private static IProcessingApp app;
    private int lastUpdateTime;

    public static void main(String[] args) {
        app = new OceanApp();
        PApplet.main(ProcessingSetup.class.getName());
    }

    @Override
    public void settings() {
        if (app instanceof OceanApp) size(800, 800);
        else size(800, 600);
    }

    @Override
    public void setup() {
        app.setup(this);
        lastUpdateTime = millis();
        
    }

    @Override
    public void draw() {
        int now = millis();
        float dt = (now - lastUpdateTime) / 1000f;
        app.draw(this, dt);
        lastUpdateTime = now;
    }

    @Override
    public void keyPressed() { app.keyPressed(this); }

    @Override
    public void mousePressed() { app.mousePressed(this); }
    
    @Override
    public void mouseReleased() { app.mouseReleased(this); }
    
    @Override
    public void mouseDragged() { app.mouseDragged(this); }

}