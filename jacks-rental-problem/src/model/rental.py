import numpy as np

from src.distributions import Poisson
from src.model.states import RentalModelState, ProbableUpdates


class CarRentalModel:
    def __init__(self, rental_cost: float,
                 moving_cost: float,
                 expectations=None,
                 max_cars: int = 20,
                 max_cars_to_move: int = 5,
                 max_expected_updates: int = 10,
                 use_expected_values: bool = True):
        """
        Car rental model for Jack's Rental Problem considering two locations.
        :param rental_cost: Money earned per car rented on any location
        :param moving_cost: Cost to move one car from one location to another
        :param expectations: dictionary containing expected value for both rental and return on a given day in a
        given location
        :param max_cars: maximum number of cars per location
        """
        if expectations is None:
            self.expectations = {'req1': 3, 'req2': 4, 'ret1': 3, 'ret2': 2}
        self.rental_cost = rental_cost
        self.moving_cost = moving_cost
        self.max_cars_to_move = max_cars_to_move
        self.max_cars = max_cars
        self.max_expected_updates = max_expected_updates
        self.update_probs = {key: [Poisson.pmf(value, expected) for value in range(self.max_cars + 1)]
                             for key, expected in self.expectations.items()}
        self.use_expected_values = use_expected_values
        self.state = None

    def states(self):
        for i in range(self.max_cars + 1):
            for j in range(self.max_cars + 1):
                yield RentalModelState(i, j)

    def probable_next_states(self):
        for rented_cars_1 in range(self.max_expected_updates):
            for rented_cars_2 in range(self.max_expected_updates):
                if self.use_expected_values:
                    yield ProbableUpdates(
                        rented_cars_1, self.update_probs['req1'][rented_cars_1],
                        rented_cars_2, self.update_probs['req2'][rented_cars_2],
                        self.expectations['ret1'], 1.0,
                        self.expectations['ret2'], 1.0
                    )
                else:
                    for returned_cars_1 in range(self.max_expected_updates):
                        for returned_cars_2 in range(self.max_expected_updates):
                            yield ProbableUpdates(
                                rented_cars_1, self.update_probs['req1'][rented_cars_1],
                                rented_cars_2, self.update_probs['req2'][rented_cars_2],
                                returned_cars_1, self.update_probs['ret1'][returned_cars_1],
                                returned_cars_2, self.update_probs['ret2'][returned_cars_2]
                            )

    def step(self, a: int, state: RentalModelState, gamma: float, V: np.ndarray):
        n_loc1 = state.n_cars_first_location
        n_loc2 = state.n_cars_second_location

        n1 = n_loc1
        n2 = n_loc2
        n_loc1 = min(max(n1 - a, 0), self.max_cars)
        n_loc2 = min(max(n2 + a, 0), self.max_cars)
        net_moved = min(abs(n_loc1 - n1), abs(n_loc2 - n2))
        action_return = - net_moved * self.moving_cost

        for state_prime in self.probable_next_states():
            req1 = min(state_prime.req1, n_loc1)
            req2 = min(state_prime.req2, n_loc2)
            p = state_prime.preq1 * state_prime.preq2 * state_prime.pret1 * state_prime.pret2
            new_n_cars_1 = min(n_loc1 - req1 + state_prime.ret1,
                               self.max_cars)
            new_n_cars_2 = min(n_loc2 - req2 + state_prime.ret2,
                               self.max_cars)
            reward = (req1 + req2) * self.rental_cost + gamma * V[new_n_cars_1, new_n_cars_2]
            action_return += p * reward
        return action_return
