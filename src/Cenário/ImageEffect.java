package Cenário;
import globais.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.sound.SoundFile;

public class ImageEffect {
    PVector pos;
    float initialRadius;
    PImage[] sprites;
    int speedFactor; 
    int counter = 0; 
    private SoundFile sound;
    
    public ImageEffect(PVector pos, float initialRadius, PImage[] sprites, int speedFactor, SoundFile sound) {
        this.pos = pos.copy();
        this.initialRadius = initialRadius;
        this.sprites = sprites;
        this.speedFactor = speedFactor;
        this.sound = sound;
        this.sound.play();
    }

    public boolean isFinished() {
        return (counter / speedFactor) >= sprites.length;
    }

    public void display(PApplet p, SubPlot plt) {
        if (isFinished()) return;
        int frameIndex = counter / speedFactor; 
        if (frameIndex >= sprites.length) {
             frameIndex = sprites.length - 1;
        }

        PImage currentSprite = sprites[frameIndex];
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float displaySize = PApplet.map(counter, 0, sprites.length * speedFactor, initialRadius, initialRadius * 2);
        float pixelWidth = displaySize * plt.getPixelWidth();
        float pixelHeight = displaySize * plt.getPixelHeight();
        p.imageMode(PApplet.CENTER);
        p.image(currentSprite, pp[0], pp[1], pixelWidth, pixelHeight);
        counter++; 
    }
}