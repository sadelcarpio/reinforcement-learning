from src.distributions import Poisson
from src.states import RentalModelState, ProbableUpdates


class CarRentalModel:
    def __init__(self, rental_cost: float,
                 moving_cost: float,
                 expectations=None,
                 max_cars: int = 20,
                 max_cars_to_move: int = 5,
                 max_expected_updates: int = 10):
        """
        Car rental model for Jack's Rental Problem considering two locations.
        :param rental_cost: Money earned per car rented on any location
        :param moving_cost:
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
        self.state = None

    def states(self):
        for i in range(self.max_cars + 1):
            for j in range(self.max_cars + 1):
                yield RentalModelState(i, j)

    def probable_next_states(self):
        for rented_cars_1 in range(self.max_expected_updates):
            for rented_cars_2 in range(self.max_expected_updates):
                yield ProbableUpdates(
                    rented_cars_1, self.update_probs['req1'][rented_cars_1],
                    rented_cars_2, self.update_probs['req2'][rented_cars_2],
                    self.expectations['ret1'], 1.0,
                    self.expectations['ret2'], 1.0)
