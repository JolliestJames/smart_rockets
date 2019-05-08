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

int lifeCounter;
Population population;
final int LIFETIME = 360;
// PVector target;
Obstacle target;
ArrayList<Obstacle> obstacles;

public void setup() {
  
  float mutationRate = 0.01f;
  lifeCounter = 0;

  target = new Obstacle(width/2-12, 24, 24, 24);
  population = new Population(mutationRate, 50);

  obstacles = new ArrayList<Obstacle>();
  obstacles.add(new Obstacle(width/2-100, height/2, 200, 10));
}

public void draw() {
  background(255);

  target.display();

  if (lifeCounter < LIFETIME) {
    population.live(obstacles);
    lifeCounter++;
  } else {
    lifeCounter = 0;
    population.fitness();
    population.selection();
    population.reproduction();
  }

  for (Obstacle obs : obstacles) {
    obs.display();
  }

  fill(0);
  text("Generation #: " + population.generations(), 10, 18);
  text("Cycles left: " + (LIFETIME-lifeCounter), 10, 36);
}
class DNA {
  PVector[] genes;
  float maxForce = 0.1f;

  DNA() {
    genes = new PVector[LIFETIME];

    for(int i = 0; i < genes.length; i++) {
      genes[i] = PVector.random2D();
      genes[i].mult(random(0, maxForce));
    }
  }

  DNA(PVector[] old) {
    genes = old;
  }

  public DNA crossover(DNA partner) {
    DNA child = new DNA();

    for (int i = 0; i < genes.length; i++) {
      int coinToss = PApplet.parseInt(random(2));
      if (coinToss == 1) {
        child.genes[i] = genes[i];
      } else {
        child.genes[i] = partner.genes[i];
      }
    }

    return child;
  }

  public void mutate(float mutationRate) {
    for (int i = 0; i < genes.length; i++) {
      if (random(1) < mutationRate) {
        float angle = random(TWO_PI);
        genes[i] = new PVector(cos(angle), sin(angle));
        genes[i].mult(random(0, maxForce));
      }
    }
  }
}
class Obstacle {
  PVector pos;
  float w, h;

  Obstacle(float x, float y, float _w, float _h) {
    pos = new PVector(x, y);
    w = _w;
    h = _h;
  }

  public void display() {
    stroke(0);
    fill(175);
    strokeWeight(2);
    rectMode(CORNER);
    rect(pos.x, pos.y, w, h);
  }

  public boolean contains(PVector v) {
    if (v.x > pos.x && v.x < pos.x + w && v.y > pos.y && v.y < pos.y + h) {
      return true;
    } else {
      return false;
    }
  }
}
class Population {
  float mutationRate;
  Rocket[] population;
  ArrayList<Rocket> matingPool;
  int generations;

  Population(float mRate, int totalPopulation) {
    mutationRate = mRate;
    population = new Rocket[totalPopulation];
    matingPool = new ArrayList<Rocket>();
    generations = 0;
    for (int i = 0; i < population.length; i++) {
      PVector position = new PVector(width/2,height+20);
      population[i] = new Rocket(position, new DNA());
    }
  }

  public void live(ArrayList<Obstacle> os) {
    for(int i = 0; i < population.length; i++) {
      population[i].run(os);
    }
  }

  public void fitness() {
    for (int i = 0; i < population.length; i++) {
      population[i].fitness();
    }
  }
  
  public void selection() {
    matingPool.clear();

    float max = maxFitness();

    for (int i = 0; i < population.length; i++) {
      float fitnessNormal = map(population[i].fitness(), 0, max, 0, 1);
      int n = (int) (fitnessNormal * 100);
      for (int j = 0; j < n; j++) {
        matingPool.add(population[i]);
      }
    }
  }

  public void reproduction() {
    for (int i = 0; i < population.length; i++) {
      int a = PApplet.parseInt(random(matingPool.size()));
      int b = PApplet.parseInt(random(matingPool.size()));
      Rocket partnerA = matingPool.get(a);
      Rocket partnerB = matingPool.get(b);
      DNA aDNA = partnerA.dna();
      DNA bDNA = partnerB.dna();
      DNA child = aDNA.crossover(bDNA);
      child.mutate(mutationRate);
      PVector position = new PVector(width/2, height+20);
  
      population[i] = new Rocket(position, child);
    }
    generations++;
  }

  public int generations() {
    return generations;
  }

  public float maxFitness() {
    float max = 0;

    for (int i = 0; i < population.length; i++) {
       if(population[i].fitness() > max) {
         max = population[i].fitness();
       }
    }

    return max;
  }
}
class Rocket {

  PVector position;
  PVector velocity;
  PVector acceleration;

  DNA dna;

  float size;
  float fitness;
  int geneCounter = 0;

  boolean stopped = false;

  Rocket(PVector location, DNA _dna) {
    acceleration = new PVector();
    velocity = new PVector();
    position = location.get();
    size = 4;
    dna = _dna;
  }

  public void run(ArrayList<Obstacle> os) {
    if (!reachedTarget() && !stopped) {
      applyForce(dna.genes[geneCounter]);
      geneCounter = (geneCounter + 1) % dna.genes.length;
      update();
      obstacles(os);
    }
    display();
  }

  public float fitness() {
    float distance = dist(position.x, position.y, target.pos.x, target.pos.y);
    fitness = pow(1.0f / distance, 2);
    if (stopped) { fitness *= 0.1f; }
    return fitness;
  }

  public void applyForce(PVector f) {
    acceleration.add(f);
  }

  public boolean reachedTarget() {
    if (target.contains(position)) {
      return true;
    } else {
      return false;
    }
  }

  public void update() {
    velocity.add(acceleration);
    position.add(velocity);
    acceleration.mult(0);
  }

  public void obstacles(ArrayList<Obstacle> os) {
    for (Obstacle obs : os) {
      if(obs.contains(position)) {
        stopped = true;
      }
    }
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

  public DNA dna() {
    return dna;
  }
}
  public void settings() {  size(640, 360); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "smart_rockets" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
