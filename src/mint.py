import time

costMap = {}
costMap[0] = 0
costMap[100] = 0

final = {}

combination = {0:[0,0,0,0,0], 100:[0,0,0,0,0]}

def cost(d1, d2, d3, d4, d5):
    # n: the minimum number of coins to represents the price  
    # d1 - d5: the denomination
    
    costMap[1] = 1
    combination[1] = [1,0,0,0,0]
    for price in range(2,100+d5):
        minCost = 100
        denom = 0
        previous = -1
        if price == 100:
            continue
        if price >= d1:
            temp = costMap[price - d1]
            if price == d1:
                minCost = 0
                denom = 1
                previous = price - d1
            elif temp < minCost:
                minCost = temp
                denom = 1
                previous = price - d1
                
        if price >= d2:
            temp = costMap[price - d2]
            if price == d2:
                minCost = 0
                denom = 2
                previous = price - d2
            elif temp < minCost:
                minCost = temp
                denom = 2
                previous = price - d2
                
        if price >= d3:
            temp = costMap[price - d3]
            if price == d3:
                minCost = 0
                denom = 3
                previous = price - d3
            elif temp < minCost:
                minCost = temp
                denom = 3
                previous = price - d3
                
        if price >= d4:
            temp = costMap[price - d4]
            if price == d4:
                minCost = 0
                denom = 4
                previous = price - d4
            elif temp < minCost:
                minCost = temp
                denom = 4
                previous = price - d4
        if price >= d5:
            temp = costMap[price - d5]
            if price == d5:
                minCost = 0
                denom = 5
                previous = price - d5
            elif temp < minCost:
                minCost = temp
                denom = 5
                previous = price - d5
                
        #print "price",price,"previous", previous, "denom", denom
        #print combination[previous]
        costMap[price] = minCost + 1
        combination[price] = list(combination[previous])
        combination[price][denom - 1] = combination[price][denom - 1] + 1
        

def cost1(d1, d2, d3, d4, d5):
    # initial value of min cost should have been calculated
    

    for m in range(1,100):
        denom = 0
        previous = -1
        price = 100 - m
        minCost = costMap[price] - 1
        temp = costMap[price + d1]
        if temp < minCost :
            minCost = temp
            denom = -1
            previous = price + d1
        temp = costMap[price + d2]
        if temp < minCost :
            minCost = temp
            denom = -2
            previous = price + d2
        temp = costMap[price + d3]
        if temp < minCost :
            minCost = temp
            denom = -3
            previous = price + d3
        temp = costMap[price + d4]
        if temp < minCost :
            minCost = temp
            denom = -4
            previous = price + d4
        temp = costMap[price + d5]
        if temp < minCost :
            minCost = temp
            denom = -5
            previous = price + d5
        costMap[price] = minCost + 1
        if denom < 0 :
            
            combination[price] = list(combination[previous])
            combination[price][0-denom-1] = combination[price][0-denom-1] - 1
        


def getScore(d1, d2, d3, d4, d5, N):
    # init variable costMap, final, combination
    costMap.clear()
    costMap[0] = 0
    costMap[100] = 0
    
    combination.clear()
    combination[0] = [0,0,0,0,0]
    combination[100] = [0,0,0,0,0]
    
    """
    print costMap
    print final
    print combination
    """
    sum = 0
    # for i in range(1,100+d5):
    cost(d1, d2, d3, d4, d5)
    
    # print combination

    cost1(d1, d2, d3, d4, d5)

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


        
########################################
##    test
########################################
def pureTest():
    for d1 in range(1, 50):
        for d2 in range(d1+1, 50):
            for d3 in range(d2+1, 50):
                for d4 in range(d3+1, 50):
                    print d1, d2, d3, d4

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
