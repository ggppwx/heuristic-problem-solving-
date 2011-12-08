goog.provide('hps.prime.BoardFactory');

goog.require('hps.prime.Board');

hps.prime.BoardFactory.create = function(filledCell){
    board = new hps.prime.Board();  
    return board;
};