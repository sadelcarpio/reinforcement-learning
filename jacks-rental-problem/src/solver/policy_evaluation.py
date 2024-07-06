import numpy as np

from src.model.rental import CarRentalModel


class PolicyEvaluation:
    def __init__(self, model: CarRentalModel, gamma: float = 0.9):
        self.model = model
        self.gamma = gamma

    def evaluate(self, pi: np.ndarray, V: np.ndarray, theta: float = 0.001):
        delta = 1
        it = 0
        while delta > theta:
            delta = 0
            it += 1
            print(f"Iteration {it}")
            for state in self.model.states():
                v = V[state.n_cars_first_location, state.n_cars_second_location]
                a = pi[state.n_cars_first_location, state.n_cars_second_location]
                action_return = self.model.step(a, state, self.gamma, V)
                V[state.n_cars_first_location, state.n_cars_second_location] = action_return
                delta = max(abs(action_return - v), delta)
            print(delta)
        return V
