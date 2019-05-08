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

  void run(ArrayList<Obstacle> os) {
    if (!reachedTarget() && !stopped) {
      applyForce(dna.genes[geneCounter]);
      geneCounter = (geneCounter + 1) % dna.genes.length;
      update();
      obstacles(os);
    }
    display();
  }

  float fitness() {
    float distance = dist(position.x, position.y, target.pos.x, target.pos.y);
    fitness = pow(1.0 / distance, 2);
    if (stopped) { fitness *= 0.1; }
    return fitness;
  }

  void applyForce(PVector f) {
    acceleration.add(f);
  }

  boolean reachedTarget() {
    if (target.contains(position)) {
      return true;
    } else {
      return false;
    }
  }

  void update() {
    velocity.add(acceleration);
    position.add(velocity);
    acceleration.mult(0);
  }

  void obstacles(ArrayList<Obstacle> os) {
    for (Obstacle obs : os) {
      if(obs.contains(position)) {
        stopped = true;
      }
    }
  }

  void display() {
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

  DNA dna() {
    return dna;
  }
}
