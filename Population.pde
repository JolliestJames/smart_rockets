class Population {
  float mutationRate;
  Rocket[] population;
  ArrayList<Rocket> matingPool;
  int generations;

  void live() {
    for(int i = 0; i < population.length; i++) {
      population[i].run();
    }
  }

  void fitness() {

  }
  
  void selection() {

  }

  void reproduction() {

  }
}
