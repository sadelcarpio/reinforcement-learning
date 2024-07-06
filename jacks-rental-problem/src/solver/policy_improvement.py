import numpy as np

from src.model.rental import CarRentalModel


class PolicyImprovement:
    def __init__(self, model: CarRentalModel, gamma: float = 0.9):
        self.model = model
        self.gamma = gamma

    def improve(self, pi: np.ndarray, V: np.ndarray):
        policy_stable = True
        for state in self.model.states():
            old_action = pi[state.n_cars_first_location, state.n_cars_second_location]
            actions = np.arange(-self.model.max_cars_to_move, self.model.max_cars_to_move + 1)
            action_returns = []
            for action in actions:
                print(f"Trying {action} on state ({state.n_cars_first_location}, {state.n_cars_second_location})")
                if -state.n_cars_second_location <= action <= state.n_cars_first_location:
                    action_return = self.model.step(action, state, self.gamma, V)
                    action_returns.append(action_return)
                else:
                    action_returns.append(-1e+10)
            new_action = actions[np.argmax(action_returns)]
            pi[state.n_cars_first_location, state.n_cars_second_location] = new_action
            if new_action != old_action:
                policy_stable = False
        return policy_stable
