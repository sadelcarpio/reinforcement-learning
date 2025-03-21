from src.model.rental import CarRentalModel
from src.solver.policy_solver import PolicySolver

if __name__ == '__main__':
    solver = PolicySolver(CarRentalModel(rental_cost=10,
                                         moving_cost=2,
                                         max_cars=20),
                          gamma=0.9)
    solver.solve()
