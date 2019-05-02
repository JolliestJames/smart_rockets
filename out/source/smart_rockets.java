import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class smart_rockets extends PApplet {

Rocket rocket;

public void setup() {
  
  rocket = new Rocket(new PVector(100, 100));
}

public void draw() {
  background(255);
  rocket.update();
  rocket.display();
}

public void keyPressed() {
  if (keyCode == RIGHT) {
    rocket.applyForce(new PVector(10, 0));
  } else {
    rocket.applyForce(new PVector(-10, 0));
  }
}

class Rocket {

  PVector position;
  PVector velocity;
  PVector acceleration;

  float size;

  Rocket(PVector location) {
    acceleration = new PVector();
    velocity = new PVector();
    position = location.get();
    size = 4;
  }

  public void applyForce(PVector f) {
    acceleration.add(f);
  }

  public void update() {
    velocity.add(acceleration);
    position.add(velocity);
    acceleration.mult(0);
  }

  public void display() {
    float theta = velocity.heading2D() + PI/2;
    fill(200, 100);
    stroke(0);
    pushMatrix();
    translate(position.x, position.y);
    rotate(theta);

    // Thrusters
    rectMode(CENTER);
    fill(0);
    rect(-size/2, size*2, size/2, size);
    rect(size/2, size*2, size/2, size);

    // Fuselage
    fill(175);
    beginShape(TRIANGLES);
    vertex(0, -size*2);
    vertex(-size, size*2);
    vertex(size, size*2);
    endShape();

    popMatrix();
  }

}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "smart_rockets" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
