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

  void live(ArrayList<Obstacle> os) {
    for(int i = 0; i < population.length; i++) {
      population[i].run(os);
    }
  }

  void fitness() {
    for (int i = 0; i < population.length; i++) {
      population[i].fitness();
    }
  }
  
  void selection() {
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

  void reproduction() {
    for (int i = 0; i < population.length; i++) {
      int a = int(random(matingPool.size()));
      int b = int(random(matingPool.size()));
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

  int generations() {
    return generations;
  }

  float maxFitness() {
    float max = 0;

    for (int i = 0; i < population.length; i++) {
       if(population[i].fitness() > max) {
         max = population[i].fitness();
       }
    }

    return max;
  }
}
