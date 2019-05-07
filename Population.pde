class Population {
  float mutationRate;
  Rocket[] population;
  ArrayList<Rocket> matingPool;
  int generations;

  Population(mutationRate, totalPopulation) {
    population = new Rocket[totalPopulation];
    for (int i = 0; i < population.length; i++) {
      population[i] = new Rocket();
    }
  }

  void live() {
    for(int i = 0; i < population.length; i++) {
      population[i].run();
    }
  }

  void fitness() {
    for (int i = 0; i < population.length; i++) {
      population[i].fitness();
    }
  }
  
  void selection() {
    ArrayList<DNA> matingPool = new ArrayList<DNA>();
 
    for (int i = 0; i < population.length; i++) {
      int n = int(population[i].fitness * 100);
      for (int j = 0; j < n; j++) {
        matingPool.add(population[i]);
      }
    }
  }

  void reproduction() {
    for (int i = 0; i < population.length; i++) {
      int a = int(random(matingPool.size()));
      int b = int(random(matingPool.size()));
      DNA partnerA = matingPool.get(a);
      DNA partnerB = matingPool.get(b);
      DNA child = partnerA.crossover(partnerB);
      child.mutate(mutationRate);
  
      population[i] = child;
    }
  }
}
