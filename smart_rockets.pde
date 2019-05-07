int lifeCounter;
Population population;

final int LIFETIME = 100;

void setup() {
  size(800, 600);
  float mutationRate = 0.01;
  population = new Population(mutationRate, 50);
}

void draw() {
  background(255);
  if (lifeCounter < LIFETIME) {
    population.live();
    lifeCounter++;
  } else {
    lifeCounter = 0;
    population.fitness();
    population.selection();
    population.reproduction();
  }
}
