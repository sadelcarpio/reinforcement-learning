from dataclasses import dataclass


@dataclass
class RentalModelState:
    n_cars_first_location: int
    n_cars_second_location: int


@dataclass
class ProbableUpdates:
    req1: int
    preq1: float
    req2: int
    preq2: float
    ret1: int
    pret1: float
    ret2: int
    pret2: float
