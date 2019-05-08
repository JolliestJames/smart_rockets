int lifeCounter;
Population population;

final int LIFETIME = 360;

PVector target;

void setup() {
  size(640, 360);
  float mutationRate = 0.01;
  lifeCounter = 0;
  target = new PVector(width/2, 24);
  population = new Population(mutationRate, 50);
}

void draw() {
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
