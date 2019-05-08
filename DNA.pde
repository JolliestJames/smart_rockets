class DNA {
  PVector[] genes;
  float maxForce = 0.1;

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

  DNA crossover(DNA partner) {
    DNA child = new DNA();

    for (int i = 0; i < genes.length; i++) {
      int coinToss = int(random(2));
      if (coinToss == 1) {
        child.genes[i] = genes[i];
      } else {
        child.genes[i] = partner.genes[i];
      }
    }

    return child;
  }

  void mutate(float mutationRate) {
    for (int i = 0; i < genes.length; i++) {
      if (random(1) < mutationRate) {
        float angle = random(TWO_PI);
        genes[i] = new PVector(cos(angle), sin(angle));
        genes[i].mult(random(0, maxForce));
      }
    }
  }
}
