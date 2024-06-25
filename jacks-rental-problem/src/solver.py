import numpy as np
from matplotlib import pyplot as plt

from src.model import CarRentalModel
from src.policy_evaluation import PolicyEvaluation
from src.policy_improvement import PolicyImprovement


class PolicySolver:
    def __init__(self, model: CarRentalModel, gamma: float = 0.9, theta: float = 0.001):
        self.model = model
        self.pi = np.zeros((model.max_cars + 1, model.max_cars + 1), dtype=int)
        self.V = np.zeros((model.max_cars + 1, model.max_cars + 1))
        self.theta = theta
        self.evaluator = PolicyEvaluation(model, gamma)
        self.improver = PolicyImprovement(model)

    def solve(self):
        policy_stable = False
        it = 0
        while not policy_stable:
            it += 1
            print(f"Policy iteration #{it}")
            self.evaluator.evaluate(self.pi, self.V, self.theta)
            policy_stable = self.improver.improve(self.pi, self.V)
        print(f"Reached stable policy after {it} iterations :)")
        plt.imshow(self.pi, origin='lower')
        plt.show()


if __name__ == '__main__':
    solver = PolicySolver(CarRentalModel(rental_cost=10,
                                         moving_cost=2,
                                         max_cars=20))
    solver.solve()
