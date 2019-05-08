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

  void run() {
    if (!reachedTarget()) {
      applyForce(dna.genes[geneCounter]);
      geneCounter = (geneCounter + 1) % dna.genes.length;
      update();
    }
    display();
  }

  float fitness() {
    float distance = dist(position.x, position.y, target.x, target.y);
    fitness = pow(1.0 / distance, 2);
    return fitness;
  }

  void applyForce(PVector f) {
    acceleration.add(f);
  }

  boolean reachedTarget() {
    float distance = dist(position.x, position.y, target.x, target.y);

    if (distance < 12) {
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
