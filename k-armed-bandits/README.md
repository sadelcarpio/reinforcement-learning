# K-Armed Bandits Simulation (Refactored)

A clean, extensible implementation of multi-armed bandit algorithms following **SOLID principles**, **design patterns**, and **clean architecture**.

## 🎯 Features

- ✅ **SOLID Principles**: Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, Dependency Inversion
- ✅ **Design Patterns**: Strategy, Factory, Builder, Observer
- ✅ **Clean Architecture**: Clear separation of concerns, dependency inversion
- ✅ **Testable**: Comprehensive unit tests with dependency injection
- ✅ **Visualization**: Built-in charting with XChart
- ✅ **Type-Safe**: Strong typing with interfaces

## 📦 Architecture

```
org.reinforcement.bandits/
├── domain/              # Core domain models
│   ├── IBandit          # Bandit interface
│   ├── Bandit           # Stationary bandit
│   ├── NonStationaryBandit
│   ├── IBanditModel     # Model interface
│   └── KArmedBanditsModel
├── algorithms/          # Learning algorithms
│   ├── IActionSelector  # Strategy for action selection
│   ├── IQValueUpdater   # Strategy for Q-value updates
│   ├── BanditAlgorithm  # Main algorithm orchestrator
│   ├── selectors/       # Action selection strategies
│   │   ├── EpsilonGreedySelector
│   │   ├── UCBSelector
│   │   └── SoftmaxSelector
│   └── updaters/        # Q-value update strategies
│       ├── SampleAverageUpdater
│       └── ConstantStepUpdater
├── simulation/          # Simulation orchestration
│   ├── BanditSimulation
│   ├── SimulationBuilder
│   ├── SimulationResult
│   └── ExperimentRunner
├── observers/           # Tracking and metrics (Observer pattern)
│   ├── ISimulationObserver
│   ├── OptimalActionTracker
│   ├── RewardTracker
│   └── ActionDistributionTracker
├── factories/           # Factory pattern for creating bandits
│   ├── IBanditFactory
│   ├── StationaryBanditFactory
│   ├── NonStationaryBanditFactory
│   └── ModelFactory
└── visualization/       # Charting utilities
    ├── ChartBuilder
    └── VisualizationDemo
```

## 🚀 Quick Start

### 1. Basic Simulation

```java
// Create and run an epsilon-greedy simulation
BanditSimulation simulation = (BanditSimulation) new SimulationBuilder()
    .withSeed(42)
    .withNumBandits(10)
    .withStationaryBandits()
    .withEpsilonGreedy(0.1)
    .withSampleAverageUpdate()
    .build();

SimulationResult result = simulation.run(1000);
System.out.println("Optimal action: " + result.getOptimalActionIndex());
System.out.println("Q-values: " + Arrays.toString(result.getQValues()));
```

### 2. Track Metrics with Observers

```java
OptimalActionTracker optimalTracker = new OptimalActionTracker(
    simulation.getModel(), 1000
);
RewardTracker rewardTracker = new RewardTracker(1000);

simulation.addObserver(optimalTracker);
simulation.addObserver(rewardTracker);

simulation.run(1000);

System.out.println("Optimal action rate: " +
    optimalTracker.getOptimalActionPercentage());
System.out.println("Average reward: " +
    rewardTracker.getAverageReward());
```

### 3. Run Multi-Trial Experiments

```java
SimulationBuilder builder = new SimulationBuilder()
    .withNumBandits(10)
    .withStationaryBandits()
    .withEpsilonGreedy(0.1)
    .withSampleAverageUpdate();

ExperimentRunner.AggregatedResults results =
    ExperimentRunner.runExperiment(builder, 100, 1000, 42);

System.out.println("Final optimal rate: " +
    results.getFinalOptimalActionRate());
```

### 4. Visualize Results

```java
// Create comparison chart
Map<String, double[]> algorithms = new HashMap<>();
algorithms.put("ε-greedy", epsilonResults);
algorithms.put("UCB", ucbResults);

XYChart chart = ChartBuilder.createComparisonChart(
    "Algorithm Comparison",
    ChartBuilder.generateStepData(1000),
    algorithms
);

ChartBuilder.displayChart(chart);
ChartBuilder.saveChart(chart, "./results", BitmapEncoder.BitmapFormat.PNG);
```

## 🧪 Algorithms Implemented

### 1. **Epsilon-Greedy**
Explores randomly with probability ε, exploits otherwise.

```java
.withEpsilonGreedy(0.1)  // 10% exploration
.withSampleAverageUpdate()
```

### 2. **Upper Confidence Bound (UCB)**
Balances exploration/exploitation using uncertainty.

```java
.withUCB(2.0)  // c parameter
.withSampleAverageUpdate()
```

### 3. **Gradient Bandit**
Learns action preferences using softmax selection.

```java
.withGradientBandit(0.1)  // step size α
```

### 4. **Non-Stationary Problems**
Use constant step-size for tracking drift.

```java
.withNonStationaryBandits(0.01)  // drift rate
.withEpsilonGreedy(0.1)
.withConstantStepUpdate(0.1)  // constant α
```

## 🏗️ Design Patterns Used

### Strategy Pattern
```java
// Plug in different action selection strategies
IActionSelector selector = new EpsilonGreedySelector(0.1, random);
IQValueUpdater updater = new SampleAverageUpdater();

BanditAlgorithm algorithm = new BanditAlgorithm(
    numActions, initialQ, selector, updater
);
```

### Factory Pattern
```java
// Create models with different bandit types
IBanditFactory factory = new StationaryBanditFactory(random);
IBanditModel model = ModelFactory.createModel(10, factory);
```

### Builder Pattern
```java
// Fluent configuration
SimulationBuilder builder = new SimulationBuilder()
    .withSeed(42)
    .withNumBandits(10)
    .withStationaryBandits()
    .withEpsilonGreedy(0.1)
    .withSampleAverageUpdate()
    .withInitialQ(0.0);
```

### Observer Pattern
```java
// Decouple tracking from simulation
simulation.addObserver(new OptimalActionTracker(model, steps));
simulation.addObserver(new RewardTracker(steps));
simulation.addObserver(new ActionDistributionTracker(numActions));
```

## 🎓 SOLID Principles

### Single Responsibility Principle
- `QValueEstimator`: Manages Q-values
- `EpsilonGreedySelector`: Selects actions
- `SampleAverageUpdater`: Updates Q-values
- Each class has one reason to change

### Open/Closed Principle
- Add new algorithms without modifying existing code
- Extend through interfaces (`IActionSelector`, `IQValueUpdater`)
- No boolean flags or switch statements

### Liskov Substitution Principle
- All `IBandit` implementations are substitutable
- All `IActionSelector` implementations work with any algorithm
- Contract-based design

### Interface Segregation Principle
- Small, focused interfaces
- Clients depend only on methods they use
- No fat interfaces

### Dependency Inversion Principle
- High-level modules depend on abstractions
- `BanditSimulation` depends on `IBanditModel`, not concrete class
- Easy to mock for testing

## 📊 Visualization

Charts created with **XChart** for publication-quality plots.

### Available Chart Types:
1. **Learning Curves**: Average reward over time
2. **Optimal Action %**: Percentage of optimal selections
3. **Algorithm Comparison**: Side-by-side performance
4. **Parameter Studies**: Effect of hyperparameters
5. **Action Distribution**: Bar charts of selection frequency

### Run the Demo:
```bash
# Compile and run visualization demo
java org.reinforcement.bandits.visualization.VisualizationDemo
```

This creates comparison plots for all algorithms and saves them as PNG files.

## 🧪 Testing

Run unit tests:
```bash
mvn test
```

### Test Coverage:
- ✅ Domain models (Bandit, KArmedBanditsModel)
- ✅ Algorithm components (selectors, updaters)
- ✅ Simulation integration tests
- ✅ Factory pattern tests
- ✅ Observer pattern tests

## 📈 Example Results

```
=== Final Performance (1000 steps, 100 trials) ===
Algorithm             | Avg Reward | Optimal %
---------------------------------------------------
Greedy                |      1.234 |     61.2%
ε-greedy (0.01)       |      1.456 |     89.3%
ε-greedy (0.1)        |      1.389 |     81.7%
UCB                   |      1.489 |     92.1%
Gradient Bandit       |      1.367 |     78.9%
```

## 🔧 Configuration Options

### Model Configuration:
```java
.withNumBandits(10)              // Number of arms
.withStationaryBandits()         // Fixed reward distributions
.withNonStationaryBandits(0.01)  // Drifting rewards
.withSeed(42)                    // For reproducibility
```

### Algorithm Configuration:
```java
.withEpsilonGreedy(0.1)          // ε parameter
.withUCB(2.0)                    // c parameter
.withGradientBandit(0.1)         // α step size
.withInitialQ(5.0)               // Optimistic initialization
```

### Update Strategy:
```java
.withSampleAverageUpdate()       // For stationary problems
.withConstantStepUpdate(0.1)     // For non-stationary problems
```

## 📚 References

- Sutton & Barto, "Reinforcement Learning: An Introduction" (2nd ed.)
- Chapter 2: Multi-armed Bandits

## 🤝 Contributing

This is a learning project demonstrating clean architecture principles.

## 📄 License

Educational use - Part of reinforcement learning coursework.