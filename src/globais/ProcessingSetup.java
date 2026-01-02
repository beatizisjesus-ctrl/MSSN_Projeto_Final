package globais;
import Apps.OceanApp;
import Apps.OceanApp2;
import processing.core.PApplet;

public class ProcessingSetup extends PApplet {

    private static IProcessingApp app;
    private int lastUpdateTime;

    public static void startApp(int i) {
        if(i == 0) app = new OceanApp();
        else if(i == 1) app = new OceanApp2();
        PApplet.main(ProcessingSetup.class.getName());
    }

    @Override
    public void settings() {
        size(800, 800);
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