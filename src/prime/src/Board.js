goog.provide('hps.prime.Board');

var BOARD_WIDTH = 3;

hps.prime.Board = function(){
    this._map = [];  // 0 - 8
    for(var x = 0;x<= 8;x++){
	this._map[x] = 0;
    }
    this._step = 0;
};

hps.prime.Board.getOffSet = function(x, y){
    return x + y* BOARD_WIDTH;
};

hps.prime.Board.prototype.set = function(x, y, num){
    if(x < BOARD_WIDTH && x >=0 && y<BOARD_WIDTH && y >= 0){
	this._map[hps.prime.Board.getOffSet(x,y)] = num;
	this._step ++ ;
    }
};


hps.prime.Board.prototype.get = function(x,y){
    return this._map[hps.prime.Board.getOffSet(x,y)];
};