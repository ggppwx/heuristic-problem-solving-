goog.provide('hps.prime.BoardFactory');

goog.require('hps.prime.Board');

hps.prime.BoardFactory.create = function(filledCell){
    board = new hps.prime.Board();  
    for(var x = 0;x<= 8;x++){
	board._map[x] = -1;
    }
    return board;
};