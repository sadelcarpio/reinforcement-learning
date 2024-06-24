import math


class Poisson:
    @classmethod
    def pmf(cls, lam, n):
        return ((lam ** n) / math.factorial(n)) * math.exp(-lam)
