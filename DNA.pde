class DNA {
  PVector[] genes;
  float maxForce = 0.1;

  DNA() {
    genes = PVector[LIFETIME];

    for(int i = 0; i < genes.length; i++) {
      genes[i] = PVector.random2D();
      genes.mult(random[0], maxForce);
    }
  }
}