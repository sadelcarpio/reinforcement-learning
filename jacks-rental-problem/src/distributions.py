import math


class Poisson:
    @classmethod
    def pmf(cls, lam: float, n: int):
        return ((lam ** n) / math.factorial(n)) * math.exp(-lam)
