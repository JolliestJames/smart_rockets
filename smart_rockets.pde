int lifeCounter;
Population population;
final int LIFETIME = 360;
// PVector target;
Obstacle target;
ArrayList<Obstacle> obstacles;

void setup() {
  size(640, 360);
  float mutationRate = 0.01;
  lifeCounter = 0;

  target = new Obstacle(width/2-12, 24, 24, 24);
  population = new Population(mutationRate, 50);

  obstacles = new ArrayList<Obstacle>();
  obstacles.add(new Obstacle(width/2-100, height/2, 200, 10));
}

void draw() {
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
