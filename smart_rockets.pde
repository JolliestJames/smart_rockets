Rocket rocket;

void setup() {
  size(800, 600);
  rocket = new Rocket(new PVector(100, 100));
}

void draw() {
  background(255);
  rocket.update();
  rocket.display();
}
