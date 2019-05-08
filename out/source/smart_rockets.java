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

PVector target;

public void setup() {
  
  float mutationRate = 0.01f;
  lifeCounter = 0;
  target = new PVector(width/2, 24);
  population = new Population(mutationRate, 50);
}

public void draw() {
  background(255);
  fill(0);
  ellipse(target.x, target.y, 24, 24);

  if (lifeCounter < LIFETIME) {
    population.live();
    lifeCounter++;
  } else {
    lifeCounter = 0;
    population.fitness();
    population.selection();
    population.reproduction();
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

  public void live() {
    for(int i = 0; i < population.length; i++) {
      population[i].run();
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

  Rocket(PVector location, DNA _dna) {
    acceleration = new PVector();
    velocity = new PVector();
    position = location.get();
    size = 4;
    dna = _dna;
  }

  public void run() {
    if (!reachedTarget()) {
      applyForce(dna.genes[geneCounter]);
      geneCounter = (geneCounter + 1) % dna.genes.length;
      update();
    }
    display();
  }

  public float fitness() {
    float distance = dist(position.x, position.y, target.x, target.y);
    fitness = pow(1.0f / distance, 2);
    return fitness;
  }

  public void applyForce(PVector f) {
    acceleration.add(f);
  }

  public boolean reachedTarget() {
    float distance = dist(position.x, position.y, target.x, target.y);

    if (distance < 12) {
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
