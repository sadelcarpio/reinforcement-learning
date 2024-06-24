import numpy as np

from src.model import CarRentalModel


class PolicyEvaluation:
    def __init__(self, model: CarRentalModel, gamma: float = 0.9):
        self.model = model
        self.gamma = gamma

    def evaluate(self, pi, V, theta: float = 0.1):
        delta = 1
        it = 0
        while delta > theta:
            delta = 0
            it += 1
            print(f"Iteration {it}")
            for state in self.model.states():
                v = V[state.n_cars_first_location, state.n_cars_second_location]
                a = int(pi[state.n_cars_first_location, state.n_cars_second_location])
                self.model.set_state(state)
                action_return = self.step(a, V)
                V[state.n_cars_first_location, state.n_cars_second_location] = action_return
                delta = max(abs(action_return - v), delta)
            print(delta)
        return V

    def step(self, a, V):
        action_return = self.model.move(a)
        state = self.model.state
        for state_prime in self.model.probable_next_states():
            req1 = min(state_prime.req1, state.n_cars_first_location)
            req2 = min(state_prime.req2, state.n_cars_second_location)
            p = state_prime.preq1 * state_prime.preq2 * state_prime.pret1 * state_prime.pret2
            new_n_cars_1 = min(state.n_cars_first_location - req1 + state_prime.ret1,
                               self.model.max_cars)
            new_n_cars_2 = min(state.n_cars_second_location - req2 + state_prime.ret2,
                               self.model.max_cars)
            reward = (req1 + req2) * self.model.rental_cost + self.gamma * V[new_n_cars_1, new_n_cars_2]
            action_return += p * reward
        return action_return


if __name__ == '__main__':
    policy_iterator = PolicyEvaluation(CarRentalModel(rental_cost=10,
                                                      moving_cost=2,
                                                      max_cars=20),
                                       pi=np.zeros((21, 21)), V=np.zeros((21, 21)))
    policy_iterator.evaluate()
