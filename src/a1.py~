import time

costMap = {}
costMap[0] = 0
costMap[100] = 0

final = {}

combination = {0:[0,0,0,0,0], 100:[0,0,0,0,0]}

def cost(n, d1, d2, d3, d4, d5):
    # n: the minimum number of coins to represents the price  
    # d1 - d5: the denomination
    minCost = 100
    denom = 0
    previous = -1
    
    if n in costMap:
        # if cost already exists     
        return costMap[n]
    
    if n < 0:
        # give up this probe
        print "PREVIOUS PRICE NEGETIVE!!"
        return 100
    
    if n == 0:
        # cost(0) = 0
        return 0
    if n == 100:
        return 0
    
    if n >= d1:
        temp = cost(n - d1, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = 1
            previous = n - d1
            
    if n >= d2:
        temp = cost(n - d2, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = 2
            previous = n - d2

    if n >= d3:
        temp = cost(n - d3, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = 3
            previous = n - d3
            
    if n >= d4:
        temp = cost(n - d4, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = 4
            previous = n - d4
            
    if n >= d5:
        temp = cost(n - d5, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = 5
            previous = n - d5


    # the cost(n) = minCost + 1
    # put the denomination to current step, and costMap
    
    if denom > 0:
        costMap[n] = minCost + 1;
        combination[n] = list(combination[previous])
        combination[n][denom - 1] = combination[n][denom - 1] + 1
    elif denom < 0:
        # never executed 
        """
        costMap[n] = minCost + 1;
        combination[n] = list(combination[previous])
        combination[n][denom-1] = combination[n][denom-1] - 1
        """
        print "ERROR"
    else:
        print "ERROR"

    """
    print "for price ",n
    print "cost is ",costMap[n]
    
    print "previous is ", previous
    print "denom is", denom
    """
    return costMap[n]

def cost1(n, d1, d2, d3, d4, d5):
    # initial value of min cost should have been calculated
    minCost = costMap[n] - 1
    denom = 0
    previous = -1
    
    """ n must exist
    if n in costMap:
        # if cost already exists     
        return costMap[n]
    """
    if n in final:
        # if the cost is final value
        return costMap[n]

    if n < 0:
        # give up this probe
        print "PREVIOUS PRICE NEGETIVE!!"
        return 100
    
    if n > 100:
        return costMap[n]
    
    if n == 0:
        # cost(0) = 0
        return 0
    if n == 100:
        return 0
    
    if  n + d1 <= 200:
        temp = cost1(n + d1, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = -1
            previous = n + d1

    if n + d2 <= 200:
        temp = cost1(n+d2, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = -2
            previous = n + d2

    if n + d3 <= 200:
        temp = cost1(n+d3, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = -3
            previous = n + d3

    if n + d4 <= 200:
        temp = cost1(n+d4, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = -4
            previous = n + d4

    if n + d5 <= 200:
        temp = cost1(n+d5, d1, d2, d3, d4, d5)
        if temp < minCost:
            minCost = temp
            denom = -5
            previous = n + d5
            
    # if minCost has been changed 
    if denom > 0:
        # never executed
        print "ERROR"
        """
        costMap[n] = minCost + 1
        final[n] = 1
        combination[n] = list(combination[previous])
        combination[n][denom - 1] = combination[n][denom - 1] + 1
        """
    elif denom < 0:
        costMap[n] = minCost + 1
        final[n] = 1
        combination[n] = list(combination[previous])
        combination[n][0-denom-1] = combination[n][0-denom-1] - 1
    else:
        # denom == 0, the min cost has not change
        final[n] = 1
        # print "combination not change"
    """
    print "previous is ", previous
    print "denom is", denom
    """
    return costMap[n]

#test function cost
def test1():
    start = time.time()
    for i in range(1,100):
        cost(i, 1, 2, 15, 25, 50)

    for i in range(1,100):
        cost1(100-i, 1, 2, 15, 25, 50)
        
    elapsed = time.time() - start 

    for n in range(1,100):
        print "---"
        print "for price n", n, "cost is", costMap[n]
        print "combination is", combination[n]
        print ""

    print "time", elapsed

def getScore(d1, d2, d3, d4, d5, N):
    # init variable costMap, final, combination
    costMap.clear()
    costMap[0] = 0
    costMap[100] = 0
    
    final.clear()
    combination.clear()
    combination[0] = [0,0,0,0,0]
    combination[100] = [0,0,0,0,0]
    
    """
    print costMap
    print final
    print combination
    """
    sum = 0
    for i in range(1,100+d5):
        cost(i, d1, d2, d3, d4, d5)
    
    # print costMap
    
    for i in range(1, 100):
        cost1(100-i, d1, d2, d3, d4, d5)

    for i in range(1,100):
        """
        print "---"
        print "for price ", i, "cost is", costMap[i]
        print "combination is", combination[i]
        print ""
        """
        
        # if n is multiple of 5, cost * N 
        if i % 5 == 0:
            sum += costMap[i] * N
        else:
            sum += costMap[i]

    # print "score >> ",sum
    return sum, combination



    
def cal(N):
    minScore = 10000
    minCombi = {}
    denomination = []
    # enumerate the denominations 
    #for d1 in range(1,99):
    start = time.time()
    d1 = 1

    if True:
        for d2 in range(d1+1,48):
            for d3 in range(d2+1,49):
                for d4 in range(d3+1,50):
                    for d5 in range(d4+1,51):
                        # dynamic programming 
                        # given denominations, get combinations for minimum score
                        # print "for denomination: ",d1, d2, d3, d4, d5
                        score, combi = getScore(d1, d2, d3, d4, d5, N)
                        if score < minScore:
                            minScore = score 
                            minCombi = combi.copy()
                            denomination = [d1, d2, d3, d4, d5]
                            print "for denomination: ",d1, d2, d3, d4, d5
                            print minScore 
    
    # the optimal denomination
    elapsed = time.time() - start 
    print "time", elapsed
    print "the optimal denomination is", denomination
    print "score is", minScore
    for i in range(0,101):
        print "price",i,"combination",minCombi[i]


def pureTest():
    for d1 in range(1, 50):
        for d2 in range(d1+1, 50):
            for d3 in range(d2+1, 50):
                for d4 in range(d3+1, 50):
                    print d1, d2, d3, d4
