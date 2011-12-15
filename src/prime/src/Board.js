goog.provide('hps.prime.Board');

var PRIME_LIST = [
101, 103, 107, 
109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 
193, 197, 199, 211,223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 
281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379,
383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 
479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 
593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 
683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 
809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 
911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997
];

var BOARD_WIDTH = 3;

hps.prime.Board = function(){
    this._map = [];  // 0 - 8
    for(var x = 0;x<= 8;x++){
	this._map[x] = -1;
    }
    this._step = 0;
};


/*
 * 
 * x, y starts from 0 ends at 2
 * */
hps.prime.Board.getOffSet = function(x, y){
    return x + y* BOARD_WIDTH;
};

hps.prime.Board.prototype.set = function(x, y, num){
    // alert(x.toString()+' '+y.toString());
    if(x < BOARD_WIDTH && x >=0 && y<BOARD_WIDTH && y >= 0){
	this._map[hps.prime.Board.getOffSet(x,y)] = num;
	this._step ++ ;
    }
};


hps.prime.Board.prototype.get = function(x,y){
    return this._map[hps.prime.Board.getOffSet(x,y)];
};

hps.prime.Board.prototype.getAddScore = function(x, y, n){
    var totalScore = 0;
    var rowScore = this._checkRow(x,y);
    if(rowScore !== -1){
	totalScore += rowScore;
    }
    var colScore = this._checkCol(x,y);
    if(colScore !== -1){
	totalScore += colScore;
    }
    var diagScore1 = this._checkDiag1(x,y);
    if(diagScore1 !== -1){
	totalScore += diagScore1;
    }
    var diagScore2 = this._checkDiag2(x,y);
    if(diagScore2 !== -1){
	totalScore +=diagScore2;
    }
    
    return totalScore;
    
 };


hps.prime.Board.prototype.moveNum = function(){
    return this._step;    
};





/*
 * @return (NaN) the score of row return -1 if the row is incomplete
 * */
hps.prime.Board.prototype._checkRow = function(x,y){
    // alert(hps.prime.Board.getOffSet(x,y));
    var num = 0;
    var score = 0;
    for(var i = 0; i< BOARD_WIDTH ; ++i){
	var n = this._map[hps.prime.Board.getOffSet(i,y)];
	if ( n === -1){
	    return -1;
	}
	num = 10*num + n;
    }
    if(this._isValidPrime(num)){
	score ++; 
    }
    
    // reverse order
    num = 0;
    for(var j = BOARD_WIDTH -1; j>=0; j--){
	var n = this._map[hps.prime.Board.getOffSet(j,y)];
	if ( n === -1){
	    return -1;
	}
	num = 10*num + n;
    }
    if(this._isValidPrime(num)){
	score ++;
    }
    return score;
};

/*
 * @return (NaN) the socre of the column return -1 if the column is incomplete
 * */
hps.prime.Board.prototype._checkCol = function(x, y){
    var num = 0;
    var score = 0;
    for(var i = 0; i< BOARD_WIDTH ; ++i){
	var n = this._map[hps.prime.Board.getOffSet(x,i)];
	if ( n === -1){
	    return -1;
	}
	num = 10*num + n;
    }
    if(this._isValidPrime(num)){
	score ++; 
    }
    
    // reverse order
    num = 0;
    for(var j = BOARD_WIDTH -1; j>=0; j--){
	var n = this._map[hps.prime.Board.getOffSet(x,j)];
	if ( n === -1){
	    return -1;
	}
	num = 10*num + n;
    }
    if(this._isValidPrime(num)){
	score ++;
    }
    return score;  
};

/*
 * @return (NaN) return the score of diagonal return -1 if it's incomplete
 * */
hps.prime.Board.prototype._checkDiag1 = function(x, y){
    var score = 0;
    if(x === y){ // in diagonal 1
	var num = 0;
	var i = 0;
	var j = 0;
	for(i = 0,j=0; i< BOARD_WIDTH && j<BOARD_WIDTH; i++,j++){
	    var n = this._map[hps.prime.Board.getOffSet(i,j)];
	    if ( n === -1){
		return -1;
	    }
	    
	    num = 10*num + n;
	} 
	if(this._isValidPrime(num)){
	    score ++; 
	}
	num = 0;
	i = 0;
	j = 0;
	for(i = BOARD_WIDTH-1,j=BOARD_WIDTH-1; i>=0 && j>=0; i--,j--){
	    var n = this._map[hps.prime.Board.getOffSet(i,j)];
	    if ( n === -1){
		return -1;
	    }
	    
	    num = 10*num + n;
	} 
	if(this._isValidPrime(num)){
	    score ++; 
	}
	return score;
	
    }else{
	return -1;
    }
};

hps.prime.Board.prototype._checkDiag2 = function(x, y){
    var score = 0;
    if(2-x === y){ // in diagonal 2
	var num = 0;
	var i = 0;
	var j = 0;
	for(i = 0,j=BOARD_WIDTH-1; i< BOARD_WIDTH && j>=0; i++,j--){
	    var n = this._map[hps.prime.Board.getOffSet(i,j)];
	    if ( n === -1){
		return -1;
	    }
	    
	    num = 10*num + n;
	} 
	if(this._isValidPrime(num)){
	    score ++; 
	}
	num = 0;
	i = 0;
	j = 0;
	for(i = BOARD_WIDTH-1,j=0; i>=0 && j<BOARD_WIDTH; i--,j++){
	    var n = this._map[hps.prime.Board.getOffSet(i,j)];
	    if ( n === -1){
		return -1;
	    }
	    
	    num = 10*num + n;
	} 
	if(this._isValidPrime(num)){
	    score ++; 
	}
	return score;
	
    }else{
	return -1;
    }


};


hps.prime.Board.prototype._isValidPrime = function(num){
    
    if(!isNaN(num) && num>=100 && num<1000){
	for(var i = 0; i<PRIME_LIST.length; ++i){
	    if(num === PRIME_LIST[i]){
		return true;
	    }
	}	
	return false;
    }else{
	// error
	return false;
    }
};