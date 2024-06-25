import numpy as np

from src.model import CarRentalModel


class PolicyImprovement:
    def __init__(self, model: CarRentalModel, gamma: float = 0.9):
        self.model = model
        self.gamma = gamma

    def improve(self, pi, V):
        policy_stable = True
        for state in self.model.states():
            old_action = pi[state.n_cars_first_location, state.n_cars_second_location]
            actions = np.arange(-self.model.max_cars_to_move, self.model.max_cars_to_move + 1)
            action_returns = []
            for action in actions:
                print(f"Trying {action} on state ({state.n_cars_first_location}, {state.n_cars_second_location})")
                if -state.n_cars_second_location <= action <= state.n_cars_first_location:
                    action_return = self.step(action, state, V)
                    action_returns.append(action_return)
                else:
                    action_returns.append(-1e+10)
            new_action = actions[np.argmax(action_returns)]
            pi[state.n_cars_first_location, state.n_cars_second_location] = new_action
            if new_action != old_action:
                policy_stable = False
        return policy_stable

    def step(self, a, state, V):
        nLoc1 = state.n_cars_first_location
        nLoc2 = state.n_cars_second_location

        n1 = nLoc1
        n2 = nLoc2
        nLoc1 = min(max(n1 - a, 0), self.model.max_cars)
        nLoc2 = min(max(n2 + a, 0), self.model.max_cars)
        net_moved = min(abs(nLoc1 - n1), abs(nLoc2 - n2))
        action_return = - net_moved * self.model.moving_cost

        for state_prime in self.model.probable_next_states():
            req1 = min(state_prime.req1, nLoc1)
            req2 = min(state_prime.req2, nLoc2)
            p = state_prime.preq1 * state_prime.preq2 * state_prime.pret1 * state_prime.pret2
            new_n_cars_1 = min(nLoc1 - req1 + state_prime.ret1,
                               self.model.max_cars)
            new_n_cars_2 = min(nLoc2 - req2 + state_prime.ret2,
                               self.model.max_cars)
            reward = (req1 + req2) * self.model.rental_cost + self.gamma * V[new_n_cars_1, new_n_cars_2]
            action_return += p * reward
        return action_return
