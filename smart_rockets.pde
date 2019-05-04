Rocket rocket;

final int LIFETIME = 100;

void setup() {
  size(800, 600);
  rocket = new Rocket(new PVector(100, 100));
}

void draw() {
  background(255);
  rocket.update();
  rocket.run();
}
